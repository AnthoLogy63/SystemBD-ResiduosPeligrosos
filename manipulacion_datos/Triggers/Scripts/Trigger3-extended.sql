-- Trigger que lanza una advertencia si se usa un código de residuo marcado como inactivo y la registra en LOG_ADVERTENCIAS.
CREATE OR REPLACE FUNCTION advertencia_codigo_residuo_inactivo()
RETURNS TRIGGER AS $$  
DECLARE
  estado_codigo TEXT;
BEGIN
  -- Consultar el estado del código de residuo usado
  SELECT "CodResEst" INTO estado_codigo
  FROM public."GZZ_CODIGORESIDUO"
  WHERE "CodResNorm" = NEW."CodResNorm";

  -- Verificar si no está activo
  IF estado_codigo IS DISTINCT FROM 'A' THEN
    RAISE NOTICE 'Advertencia: Se está usando un código de residuo inactivo: %', NEW."CodResNorm";

    -- Registro en LOG_ADVERTENCIAS
    INSERT INTO public."LOG_ADVERTENCIAS" (
      "Origen", "Detalle", "TablaAfectada", "ClavePrimaria"
    ) VALUES (
      'trg_residuo_con_codigo_inactivo',
      'Código de residuo inactivo detectado: ' || NEW."CodResNorm",
      'R1T_RESIDUO',
      NEW."ResCod"::TEXT
    );
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger asociado a la tabla de residuos
CREATE TRIGGER trg_residuo_con_codigo_inactivo
BEFORE INSERT OR UPDATE ON public."R1T_RESIDUO"
FOR EACH ROW
EXECUTE FUNCTION advertencia_codigo_residuo_inactivo();
