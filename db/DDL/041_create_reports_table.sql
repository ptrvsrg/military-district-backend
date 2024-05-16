CREATE TABLE IF NOT EXISTS reports
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL UNIQUE,
    description TEXT,
    query       TEXT           NOT NULL,
    parameters  VARCHAR(255)[] NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE reports IS 'Отчёты';

COMMENT ON COLUMN reports.id IS 'ID';
COMMENT ON COLUMN reports.name IS 'Имя';
COMMENT ON COLUMN reports.description IS 'Описание';
COMMENT ON COLUMN reports.query IS 'Запрос';
COMMENT ON COLUMN reports.parameters IS 'Параметры';
COMMENT ON COLUMN reports.created_at IS 'Дата создания';
COMMENT ON COLUMN reports.updated_at IS 'Дата обновления';