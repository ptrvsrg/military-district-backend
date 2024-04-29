CREATE OR REPLACE FUNCTION gen_report_privates_and_sergeants_by_rank_and_formations(
    rank_name VARCHAR(255),
    army_name VARCHAR(255),
    brigade_name VARCHAR(255),
    corps_name VARCHAR(255),
    division_name VARCHAR(255),
    unit_name VARCHAR(255)
)
    RETURNS TABLE
            (
                PRIVATE_OR_SERGEANT_MBN  VARCHAR(255),
                PRIVATE_OR_SERGEANT_NAME VARCHAR(255),
                RANK                     VARCHAR(255)
            )
AS
$body$
WITH privates_and_sergeants AS (SELECT m.mbn                         AS private_or_sergeant_mbn,
                                       CONCAT(m.last_name, ' ', m.first_name,
                                              CASE
                                                  WHEN m.middle_name IS NULL THEN ''
                                                  ELSE CONCAT(' ', m.middle_name)
                                                  END)::VARCHAR(255) AS private_or_sergeant_name,
                                       r.name                        AS rank,
                                       m.unit_name                   AS unit_name
                                FROM militaries m
                                         JOIN ranks r ON r.id = m.rank_id
                                         JOIN rank_categories rc ON rc.id = r.rank_category_id
                                WHERE rc.name = 'Рядовой и сержантский состав')
SELECT DISTINCT ps.private_or_sergeant_mbn  AS private_or_sergeant_mbn,
                ps.private_or_sergeant_name AS private_or_sergeant_name,
                ps.rank                     AS rank
FROM privates_and_sergeants ps
         LEFT JOIN units u ON u.name = ps.unit_name
         LEFT JOIN brigades_units bu ON bu.unit_id = u.id
         LEFT JOIN corps_units cu ON cu.unit_id = u.id
         LEFT JOIN divisions_units du ON du.unit_id = u.id
         LEFT JOIN armies_brigades ab ON ab.brigade_id = bu.brigade_id
         LEFT JOIN armies_corps ac ON ac.corps_id = cu.corps_id
         LEFT JOIN armies_divisions ad ON ad.division_id = du.division_id
         LEFT JOIN brigades b ON b.id = bu.brigade_id
         LEFT JOIN corps c ON c.id = cu.corps_id
         LEFT JOIN divisions d ON d.id = du.division_id
         LEFT JOIN armies a ON (a.id = ab.army_id OR a.id = ac.army_id OR a.id = ad.army_id)
WHERE ($1 IS NULL OR ps.rank = $1)
  AND ($2 IS NULL OR u.name = $2)
  AND ($3 IS NULL OR b.name = $3)
  AND ($4 IS NULL OR c.name = $4)
  AND ($5 IS NULL OR d.name = $5)
  AND ($6 IS NULL OR a.name = $6)
ORDER BY private_or_sergeant_mbn;
$body$
    LANGUAGE sql;