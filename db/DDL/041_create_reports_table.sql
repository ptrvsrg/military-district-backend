DO
$$
    BEGIN
        CREATE TYPE REPORT_PARAMETER AS
        (
            name             VARCHAR(255),
            query_for_values TEXT
        );

        COMMENT ON TYPE REPORT_PARAMETER IS 'Параметр отчёта';
        COMMENT ON COLUMN report_parameter.name IS 'Название';
        COMMENT ON COLUMN report_parameter.query_for_values IS 'Запрос для получения значений';
    EXCEPTION
        WHEN duplicate_object THEN NULL;
    END
$$;

CREATE TABLE IF NOT EXISTS reports
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)       NOT NULL UNIQUE,
    description TEXT,
    query       TEXT               NOT NULL,
    parameters  REPORT_PARAMETER[] NOT NULL,
    created_at  TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ        NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE reports IS 'Отчёты';

COMMENT ON COLUMN reports.id IS 'ID';
COMMENT ON COLUMN reports.name IS 'Имя';
COMMENT ON COLUMN reports.description IS 'Описание';
COMMENT ON COLUMN reports.query IS 'Запрос';
COMMENT ON COLUMN reports.parameters IS 'Параметры';
COMMENT ON COLUMN reports.created_at IS 'Дата создания';
COMMENT ON COLUMN reports.updated_at IS 'Дата обновления';