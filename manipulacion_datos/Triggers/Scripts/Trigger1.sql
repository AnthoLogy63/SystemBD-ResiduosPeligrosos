-- Trigger que detecta residuos con nivel de toxicidad alto y lanza una advertencia al registrarlos.
CREATE OR REPLACE FUNCTION notificar_toxicidad_alta()
RETURNS TRIGGER AS $$
BEGIN
  -- Condición: Si el nivel de toxicidad es 3 (Alto)
  IF NEW."ResTox" = 3 THEN
    -- Acción: Mostrar mensaje de advertencia  
    RAISE NOTICE 'Residuos altamente tóxicos detectados: ID %', NEW."ResCod";
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger asociado a la tabla de residuos
CREATE TRIGGER trg_toxicidad_alta
AFTER INSERT ON public."R1T_RESIDUO"
FOR EACH ROW
EXECUTE FUNCTION notificar_toxicidad_alta();
