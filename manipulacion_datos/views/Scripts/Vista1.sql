-- Vista que muestra los residuos generados por cada empresa, incluyendo su nombre, rubro y estado del residuo.
CREATE OR REPLACE VIEW vista_empresa_residuos AS
SELECT
    emp."EmpNif" AS "NIF_Empresa",
    emp."EmpNom" AS "Nombre_Empresa",
    emp."EmpAct" AS "Rubro",
    res."ResCod" AS "Codigo_Residuo",
    res."ResCant" AS "Cantidad_Tn",
    res."ResEst" AS "Estado_Residuo"
FROM
    "R1M_EMPRESA" emp
JOIN
    "R1T_RESIDUO" res ON emp."EmpNif" = res."EmpNif"
ORDER BY
    emp."EmpNom", res."ResCod";