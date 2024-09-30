DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS books;


CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    year INT,
    category VARCHAR(255),
    author VARCHAR(255) NOT NULL
);


CREATE TABLE ratings(
    id SERIAL PRIMARY KEY,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    book_id INT REFERENCES books(id) ON DELETE CASCADE
);
