CREATE TABLE public."LOG_ADVERTENCIAS" (
  "LogID" SERIAL PRIMARY KEY,
  "FechaHora" TIMESTAMP DEFAULT NOW(),
  "Origen" TEXT,         -- Nombre del trigger o evento
  "Detalle" TEXT,        -- Mensaje descriptivo
  "TablaAfectada" TEXT,  -- Ej: R1T_RESIDUO o R1T_TRASLADO
  "ClavePrimaria" TEXT,  -- Clave que identifique el registro (ej. ResCod, EmpNif|ResCod)
  "Usuario" TEXT NULL    -- Si deseas registrar usuario (opcional)
);
