import http from 'k6/http';
import { sleep } from 'k6';
import { BASE_URL, CACHE_MODE } from './config.js';
import { generateRandomIds, randomChoice } from './k6_utils.js';

// Popular IDs (to simulate realistic traffic)
// Each virtual user gets its own IDs
// Total IDs = popularIds.length * number of virtual users
const popularIds = generateRandomIds(15, 1, 10000000);

export const options = {
    stages: [
        { duration: '5s', target: 400 },  // ramp-up to 400 VUs
        { duration: '15s', target: 400 }, // sustain 400 VUs
        { duration: '5s', target: 0 },    // ramp-down to 0 VUs
    ],
};

export default function () {
    // Pick a random product ID from the popular pool
    const productId = randomChoice(popularIds);

    const url = `${BASE_URL}/api/products/${productId}?cacheMode=${CACHE_MODE}`;
    const response = http.get(url);

    if (response.status !== 200 && response.status !== 404) {
        console.log(`GET failed: ${response.status} for ID: ${productId}`);
    } else {
        console.log(`GET success: ${response.status} for ID: ${productId}`);
    }

    sleep(0.5); // small pause between requests for more realistic load
}
