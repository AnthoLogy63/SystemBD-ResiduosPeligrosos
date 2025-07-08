-- Vista que muestra información general de cada empresa, incluyendo ciudad y región donde se ubica.
CREATE OR REPLACE VIEW vista_info_completa_empresa AS
SELECT
    emp."EmpNif" AS "NIF_Empresa",
    emp."EmpNom" AS "Nombre_Empresa",
    emp."EmpAct" AS "Rubro",
    emp."EmpEst" AS "Estado",
    ciudad."CiudNom" AS "Ciudad",
    region."RegNom" AS "Region"
FROM
    "R1M_EMPRESA" emp
JOIN
    "GZZ_CIUDAD" ciudad ON emp."EmpCiu" = ciudad."CiudCod"
JOIN
    "GZZ_REGION" region ON ciudad."RegCod" = region."RegCod"
ORDER BY
    emp."EmpNom";
