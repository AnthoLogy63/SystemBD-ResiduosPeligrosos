-- Vista que muestra los 30 traslados más recientes de residuos, incluyendo datos de la empresa, tipo de residuo, toxicidad, transportista y destino final.
CREATE OR REPLACE VIEW vista_historial_traslados_detallado AS
SELECT *
FROM (
    SELECT
        tra."TraFecEnv" AS "Fecha_Envio",
        emp."EmpNom" AS "Empresa",
        emp."EmpAct" AS "Rubro",
        res."ResCod" AS "Codigo_Residuo",
        tipo."TipoResNom" AS "Tipo_Residuo",
        tox."ToxNom" AS "Nivel_Toxicidad",
        tra."TraCant" AS "Cantidad_Trasladada_Tn",
        transp."EmpTransNom" AS "Empresa_Transporte",
        dest."DestNom" AS "Destino_Final"
    FROM
        "R1T_TRASLADO" tra
    JOIN
        "R1T_RESIDUO" res ON tra."ResCod" = res."ResCod"
    JOIN
        "R1M_EMPRESA" emp ON res."EmpNif" = emp."EmpNif"
    JOIN
        "GZZ_TIPORESIDUO" tipo ON res."TipoResCod" = tipo."TipoResCod"
    JOIN
        "GZZ_TOXICIDAD" tox ON res."ResTox" = tox."ToxCod"
    JOIN
        "R1M_DESTINO" dest ON tra."DestCod" = dest."DestCod"
    JOIN
        "R1M_TRANSPORTISTA" transp ON tra."TransCod" = transp."EmpTransNif"
    ORDER BY
        tra."TraFecEnv" DESC
    LIMIT 30
) AS ultimos_traslados;