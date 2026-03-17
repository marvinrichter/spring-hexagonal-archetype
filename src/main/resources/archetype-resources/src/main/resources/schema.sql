-- DDL for the hexagonal architecture demo application
-- This script is executed by Spring Boot's sql.init.mode=always

CREATE TABLE IF NOT EXISTS orders (
    id          UUID         NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    currency    VARCHAR(3)   NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT chk_orders_total_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_orders_status CHECK (status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);

COMMENT ON TABLE orders IS 'Persisted Order aggregates from the hexagonal domain layer';
COMMENT ON COLUMN orders.id IS 'UUID primary key — maps to OrderId value object';
COMMENT ON COLUMN orders.currency IS 'ISO 4217 currency code, e.g. EUR or USD';
COMMENT ON COLUMN orders.status IS 'Current lifecycle status of the order';
