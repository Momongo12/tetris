-- Создание таблицы "users"
CREATE TABLE users (
   user_id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   username VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL
);

-- Создание индекса на столбец "email" в таблице "users"
CREATE INDEX idx_users_email ON users(email);

-- Создание таблицы "high_scores" с внешним ключом на таблицу "users"
CREATE TABLE high_scores (
     high_score_id SERIAL PRIMARY KEY,
     score INT NOT NULL,
     level INT NOT NULL,
     lines INT NOT NULL,
     user_id INT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Создание триггера для таблицы "high_scores"
CREATE OR REPLACE FUNCTION keep_top_scores()
    RETURNS TRIGGER AS $$
BEGIN
    -- Удаление записей, если количество записей больше 30
    IF (SELECT COUNT(*) FROM high_scores) > 30 THEN
        DELETE FROM high_scores
        WHERE high_score_id NOT IN (
            SELECT high_score_id FROM high_scores
            ORDER BY score DESC
            LIMIT 30
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Создание триггера на вставку для таблицы "high_scores"
CREATE TRIGGER keep_top_scores_trigger
    AFTER INSERT ON high_scores
    FOR EACH ROW
EXECUTE FUNCTION keep_top_scores();