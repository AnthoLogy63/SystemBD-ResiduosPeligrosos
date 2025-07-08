CREATE OR REPLACE VIEW vista_total_residuos_por_tipo_y_toxicidad AS
SELECT
    tipo."TipoResNom" AS "Tipo_Residuo",
    tox."ToxNom" AS "Nivel_Toxicidad",
    SUM(res."ResCant") AS "Total_Toneladas",
    COUNT(DISTINCT res."EmpNif") AS "Numero_Empresas"
FROM
    "R1T_RESIDUO" res
JOIN
    "GZZ_TIPORESIDUO" tipo ON res."TipoResCod" = tipo."TipoResCod"
JOIN
    "GZZ_TOXICIDAD" tox ON res."ResTox" = tox."ToxCod"
GROUP BY
    tipo."TipoResNom",
    tox."ToxNom"
ORDER BY
    "Total_Toneladas" DESC;
