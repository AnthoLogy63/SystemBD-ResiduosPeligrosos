DROP FUNCTION IF EXISTS pa_diagnostico_empresa_completo_extended(TEXT);

CREATE OR REPLACE FUNCTION pa_diagnostico_empresa_completo_extended(p_nif TEXT)
RETURNS TABLE (
    tipo TEXT,
    campo1 TEXT,
    campo2 TEXT,
    campo3 TEXT,
    campo4 TEXT,
    campo5 TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    total_toneladas NUMERIC := 0;
    info_empresa RECORD;
    residuo RECORD;
    diagnostico TEXT;
BEGIN
    -- Mostrar datos de la empresa
    FOR info_empresa IN
        SELECT * FROM vista_info_completa_empresa WHERE "NIF_Empresa" = p_nif
    LOOP
        RETURN NEXT ('EMPRESA',
            info_empresa."Nombre_Empresa",
            info_empresa."Rubro",
            info_empresa."Estado",
            info_empresa."Ciudad",
            info_empresa."Region");
    END LOOP;

    -- Mostrar residuos
    FOR residuo IN
        SELECT * FROM vista_empresa_residuos WHERE "NIF_Empresa" = p_nif
    LOOP
        RETURN NEXT ('RESIDUO',
            residuo."Codigo_Residuo",
            NULL,
            residuo."Cantidad_Tn"::TEXT,
            NULL,
            NULL);
    END LOOP;

    -- Calcular total
    SELECT SUM("Cantidad_Tn") INTO total_toneladas
    FROM vista_empresa_residuos
    WHERE "NIF_Empresa" = p_nif;

    IF total_toneladas IS NULL THEN
        total_toneladas := 0;
    END IF;

    IF total_toneladas > 200 THEN
        diagnostico := 'Alta producción';
    ELSIF total_toneladas >= 100 THEN
        diagnostico := 'Producción media';
    ELSE
        diagnostico := 'Producción baja';
    END IF;

    -- Mostrar diagnóstico
    RETURN NEXT ('DIAGNOSTICO',
        diagnostico,
        total_toneladas::TEXT,
        NULL, NULL, NULL);

END;
$$;
