import http from 'k6/http';
import { sleep } from 'k6';
import { BASE_URL, CACHE_MODE } from './config.js';
import { generateRandomIds, randomChoice, randomFloat } from './k6_utils.js';

// Popular product IDs (simulate realistic access)
const popularIds = generateRandomIds(5, 1, 10000000);

export const options = {
    stages: [
        { duration: '5s', target: 100 },  // ramp-up to 100 virtual users
        { duration: '15s', target: 100 }, // sustain 100 users
        { duration: '5s', target: 0 },    // ramp-down to 0 users
    ],
};

export default function () {
    // Pick a random product ID
    const productId = randomChoice(popularIds);

    // Generate random update data
    const updateData = {
        price: randomFloat(10, 999.99),
        description: `Updated ${Date.now()}`,
    };

    const url = `${BASE_URL}/api/products/${productId}?cacheMode=${CACHE_MODE}`;

    const response = http.put(url, JSON.stringify(updateData), {
        headers: { 'Content-Type': 'application/json' },
    });

    if (response.status !== 200 && response.status !== 404) {
        console.log(`PUT failed: ${response.status} for ID: ${productId}`);
    }

    sleep(0.5); // small pause between iterations
}
