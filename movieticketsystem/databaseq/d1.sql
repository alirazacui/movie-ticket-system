-- 1. Create the Database


-- Use the database
USE movie_managment;

-- 2. Create the `movies` table
CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration INT,
    available_seats INT NOT NULL,
    ticket_price DECIMAL(10, 2) NOT NULL
);

-- 3. Create the `tickets` table
CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    movie_id INT,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE
);

-- 4. Create the `users` table (optional, for admin authentication)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

SELECT * FROM movies;

