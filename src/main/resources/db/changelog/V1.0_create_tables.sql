CREATE TABLE IF NOT EXISTS book_tracking
(
    book_tracking_id SERIAL PRIMARY KEY,
    book_id          INT         NOT NULL,
    status           VARCHAR(20) NOT NULL,
    borrowed_at      TIMESTAMP,
    due_date         TIMESTAMP,
    borrowed_by      INT,
    reserved_by      INT,
    reserved_until   TIMESTAMP,
    is_deleted       BOOLEAN     NOT NULL DEFAULT FALSE,
    updated_at       TIMESTAMP   NOT NULL
);

CREATE TABLE book_tracking_history
(
    book_tracking_history_id SERIAL PRIMARY KEY,
    book_tracking_id         INT         NOT NULL,
    status                   VARCHAR(20) NOT NULL,
    changed_at               TIMESTAMP   NOT NULL,
    comment                  VARCHAR(255),
    CONSTRAINT fk_history_tracking
        FOREIGN KEY (book_tracking_id)
            REFERENCES book_tracking (book_tracking_id)
            ON DELETE CASCADE
);

