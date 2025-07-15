CREATE TABLE log_advertencias (
  logid INT AUTO_INCREMENT PRIMARY KEY,
  fechahora DATETIME DEFAULT CURRENT_TIMESTAMP,
  origen LONGTEXT,
  detalle LONGTEXT,
  tablafectada LONGTEXT,
  claveprimaria LONGTEXT,
  usuario LONGTEXT
);
