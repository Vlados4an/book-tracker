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

INSERT INTO book_tracking (book_id, status, borrowed_at, due_date, borrowed_by, reserved_by, reserved_until, is_deleted,
                           updated_at)
VALUES (1, 'AVAILABLE', NULL, NULL, NULL, NULL, NULL, FALSE, CURRENT_TIMESTAMP),
       (2, 'BORROWED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL 14 DAY, 101, NULL, NULL, FALSE,
        CURRENT_TIMESTAMP),
       (3, 'RESERVED', NULL, NULL, NULL, 102, CURRENT_TIMESTAMP + INTERVAL 3 DAY, FALSE, CURRENT_TIMESTAMP),
       (4, 'MAINTENANCE', NULL, NULL, NULL, NULL, NULL, FALSE, CURRENT_TIMESTAMP),
       (5, 'LOST', NULL, NULL, NULL, NULL, NULL, TRUE, CURRENT_TIMESTAMP);

