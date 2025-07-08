DROP PROCEDURE IF EXISTS pa_alerta_saturacion_destinos;

CREATE OR REPLACE PROCEDURE pa_alerta_saturacion_destinos()
LANGUAGE plpgsql
AS $$
DECLARE
    destino RECORD;
    uso_percent NUMERIC;
    contador_saturados INTEGER := 0;
BEGIN
    -- Contar cuántos destinos superan el 80% de ocupación
    FOR destino IN
        SELECT 
            "DestCapacidad", 
            COALESCE("DestCapUsada", 0) AS cap_usada
        FROM "R1M_DESTINO"
    LOOP
        IF destino."DestCapacidad" > 0 AND
           (destino.cap_usada / destino."DestCapacidad") * 100 >= 80 THEN
            contador_saturados := contador_saturados + 1;
        END IF;
    END LOOP;

    RAISE NOTICE '== Diagnóstico de Saturación de Destinos ==';
    RAISE NOTICE 'Resumen: % destino(s) con más del 80%% de ocupación.', contador_saturados;

    -- Mostrar detalles de cada destino saturado
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
                RAISE NOTICE 'Destino % (%): %%% ocupado (% / % toneladas)',
                    destino."DestCod",
                    destino."DestNom",
                    uso_percent,
                    destino.capacidad_usada,
                    destino."DestCapacidad";
            END IF;
        END IF;
    END LOOP;

    RAISE NOTICE '== Fin del diagnóstico ==';
END;
$$;
