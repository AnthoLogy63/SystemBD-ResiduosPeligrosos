DROP PROCEDURE IF EXISTS pa_diagnostico_empresa_completo(TEXT);

CREATE OR REPLACE PROCEDURE pa_diagnostico_empresa_completo(IN p_nif TEXT)
LANGUAGE plpgsql
AS $$
DECLARE
    total_toneladas NUMERIC := 0;
    info_empresa RECORD;
    residuo RECORD;
BEGIN
    RAISE NOTICE '== Información General de la Empresa ==';

    FOR info_empresa IN
        SELECT * FROM vista_info_completa_empresa WHERE "NIF_Empresa" = p_nif
    LOOP
        RAISE NOTICE 'Empresa: %, Rubro: %, Estado: %, Ciudad: %, Región: %',
            info_empresa."Nombre_Empresa",
            info_empresa."Rubro",
            info_empresa."Estado",
            info_empresa."Ciudad",
            info_empresa."Region";
    END LOOP;

    RAISE NOTICE '== Residuos Registrados por la Empresa ==';

    FOR residuo IN
        SELECT * FROM vista_empresa_residuos WHERE "NIF_Empresa" = p_nif
    LOOP
        RAISE NOTICE 'Residuo %: % toneladas',
            residuo."Codigo_Residuo",
            residuo."Cantidad_Tn";
    END LOOP;

    SELECT SUM("Cantidad_Tn")
    INTO total_toneladas
    FROM vista_empresa_residuos
    WHERE "NIF_Empresa" = p_nif;

    IF total_toneladas IS NULL THEN
        total_toneladas := 0;
    END IF;

    IF total_toneladas > 200 THEN
        RAISE NOTICE 'Diagnóstico: Alta producción (% toneladas registradas)', total_toneladas;
    ELSIF total_toneladas >= 100 THEN
        RAISE NOTICE 'Diagnóstico: Producción media (% toneladas registradas)', total_toneladas;
    ELSE
        RAISE NOTICE 'Diagnóstico: Producción baja (% toneladas registradas)', total_toneladas;
    END IF;
END;
$$;
