CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE coupon (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    discount  INTEGER NOT NULL,
    valid TIMESTAMP NOT NULL,
    event_id UUID NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
)