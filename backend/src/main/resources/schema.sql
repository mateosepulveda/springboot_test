CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
    rut INTEGER NOT NULL
    address VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL
);

CREATE TABLE purchase (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES client(id),
    date_time TIMESTAMP
);

CREATE TABLE product_in_purchase (
    id SERIAL PRIMARY KEY,
    purchase_id INTEGER REFERENCES purchase(id),
    product_id INTEGER REFERENCES product(id),
    quantity INTEGER NOT NULL
);