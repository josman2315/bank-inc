-- Creación de la base de datos
CREATE DATABASE card_management_system;

-- Seleccionar la base de datos para usar
USE card_management_system;

-- Creación tabla cards
CREATE TABLE cards (
    card_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    holder_name VARCHAR(100),
    expiration_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    balance DOUBLE NOT NULL DEFAULT 0.0
);

-- Creación tabla transactions
CREATE TABLE transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    is_annulled BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_card
        FOREIGN KEY (card_id) 
        REFERENCES cards(card_id)
        ON DELETE CASCADE
);
