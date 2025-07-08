-- Trigger que verifica si la cantidad de un traslado se acerca a la capacidad total del destino.
CREATE OR REPLACE FUNCTION verificar_capacidad_destino()
RETURNS TRIGGER AS $$
DECLARE
  capacidad_total NUMERIC;
  capacidad_usada NUMERIC;
  nueva_carga NUMERIC;
BEGIN
  -- Obtener capacidad total y capacidad usada del destino
  SELECT "DestCapacidad", COALESCE("DestCapUsada", 0)
  INTO capacidad_total, capacidad_usada
  FROM public."R1M_DESTINO"
  WHERE "DestCod" = NEW."DestCod";

  nueva_carga := capacidad_usada + NEW."TraCant";

  -- Advertencia si está a menos de 50 toneladas de llenarse pero sin pasarse
  IF nueva_carga >= capacidad_total - 50 AND nueva_carga <= capacidad_total THEN
    RAISE NOTICE 'Advertencia: El destino % está cerca de su límite. Carga proyectada: %, Capacidad máxima: %.',
      NEW."DestCod", nueva_carga, capacidad_total;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger asociado a la tabla de traslados
CREATE TRIGGER trg_verificar_capacidad_destino
BEFORE INSERT ON public."R1T_TRASLADO"
FOR EACH ROW
EXECUTE FUNCTION verificar_capacidad_destino();
