DROP FUNCTION IF EXISTS pa_alerta_saturacion_destinos_extended();

CREATE OR REPLACE FUNCTION pa_alerta_saturacion_destinos_extended()
RETURNS TABLE(
    tipo TEXT,
    campo1 TEXT,
    campo2 TEXT,
    campo3 NUMERIC,
    campo4 NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    destino RECORD;
    uso_percent NUMERIC;
    contador_saturados INTEGER := 0;
BEGIN
    -- Contar cuántos destinos están saturados
    FOR destino IN
        SELECT "DestCapacidad", COALESCE("DestCapUsada", 0) AS cap_usada
        FROM "R1M_DESTINO"
    LOOP
        IF destino."DestCapacidad" > 0 AND (destino.cap_usada / destino."DestCapacidad") * 100 >= 80 THEN
            contador_saturados := contador_saturados + 1;
        END IF;
    END LOOP;

    -- Resumen general
    RETURN NEXT ('RESUMEN', 'Destinos saturados', contador_saturados::TEXT, NULL, NULL);

    -- Detalle por destino
    FOR destino IN
        SELECT 
            d."DestCod",
            d."DestNom",
            d."DestCapacidad",
            COALESCE(d."DestCapUsada", 0) AS capacidad_usada
        FROM "R1M_DESTINO" d
    LOOP
        IF destino."DestCapacidad" > 0 THEN
            uso_percent := 100 * destino.capacidad_usada / destino."DestCapacidad";

            IF uso_percent >= 80 THEN
                RETURN NEXT (
                    'DETALLE',
                    destino."DestCod",
                    destino."DestNom",
                    destino.capacidad_usada,
                    uso_percent
                );
            END IF;
        END IF;
    END LOOP;

    RETURN;
END;
$$;
