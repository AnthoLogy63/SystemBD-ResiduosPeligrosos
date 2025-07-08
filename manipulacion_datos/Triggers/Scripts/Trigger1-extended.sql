-- Trigger que detecta residuos con nivel de toxicidad alto y registra advertencia en LOG_ADVERTENCIAS.
CREATE OR REPLACE FUNCTION notificar_toxicidad_alta()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW."ResTox" = 3 THEN
    RAISE NOTICE 'Residuos altamente tóxicos detectados: ID %', NEW."ResCod";

    -- Registro en LOG_ADVERTENCIAS
    INSERT INTO public."LOG_ADVERTENCIAS" (
      "Origen", "Detalle", "TablaAfectada", "ClavePrimaria"
    ) VALUES (
      'trg_toxicidad_alta',
      'Residuos altamente tóxicos detectados: ID ' || NEW."ResCod",
      'R1T_RESIDUO',
      NEW."ResCod"::TEXT
    );
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger asociado a la tabla de residuos
CREATE TRIGGER trg_toxicidad_alta
AFTER INSERT ON public."R1T_RESIDUO"
FOR EACH ROW
EXECUTE FUNCTION notificar_toxicidad_alta();
