CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE address (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    uf  VARCHAR(100) NULL,
    event_id UUID NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
)