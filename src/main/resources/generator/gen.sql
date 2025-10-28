INSERT INTO products (name, price, description, created_at, updated_at)
SELECT
    'Product ' || gs::text AS name,
    (random() * 1000)::numeric(10,2) AS price,
    'Description for product ' || gs::text AS description,
    NOW() AS created_at,
    NOW() AS updated_at
FROM generate_series(1, 10000000) AS gs;
