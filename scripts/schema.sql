-- Создание таблицы "players"
CREATE TABLE players (
   player_id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   username VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   date_of_registration DATE
);

CREATE INDEX idx_users_email ON players(email);

-- Создание таблицы "high_scores" с внешним ключом на таблицу "players"
CREATE TABLE high_scores (
     high_score_id SERIAL PRIMARY KEY,
     max_score INT NOT NULL,
     max_level INT NOT NULL,
     max_lines INT NOT NULL,
     player_id INT NOT NULL,
     FOREIGN KEY (player_id) REFERENCES players(player_id)
);

CREATE INDEX idx_high_scores_user_id ON high_scores(player_id);

CREATE TABLE player_statistic (
    player_statistic_id SERIAL PRIMARY KEY,
    player_id INT NOT NULL,
    max_score INT NOT NULL DEFAULT 0,
    max_level INT NOT NULL DEFAULT 0,
    max_lines INT NOT NULL DEFAULT 0,
    average_score INT NOT NULL DEFAULT 0,
    number_of_games INT NOT NULL DEFAULT 0,
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

CREATE INDEX idx_player_statistic_user_id ON player_statistic(player_id);

-- Создание триггера для таблицы "high_scores"
CREATE OR REPLACE FUNCTION keep_top_scores()
    RETURNS TRIGGER AS $$
BEGIN
    -- Удаление записей, если количество записей больше 30
    IF (SELECT COUNT(*) FROM high_scores) > 30 THEN
        DELETE FROM high_scores
        WHERE high_score_id NOT IN (
            SELECT high_score_id FROM high_scores
            ORDER BY max_score DESC
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