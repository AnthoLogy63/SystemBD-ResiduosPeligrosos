package traslado.modelo;

public class TrasladoModel {
    private String empNif;        // EmpNif: NIF Empresa
    private int resCod;           // ResCod: Código del Residuo (INT)
    private String fechaEnvio;    // TraFecEnv: Fecha de Envío (VARCHAR(8))
    private String destCod;       // DestCod: Código del Destino
    private String tipoEnvase;    // TraEnv: Tipo de Envase
    private String fechaLlegada;  // TraFecLlega: Fecha de Llegada (VARCHAR(8), puede ser null)
    private String tratamiento;   // TraTrat: Tipo de Tratamiento
    private double cantidad;      // TraCant: Cantidad trasladada
    private String observacion;   // TraObs: Observaciones
    private String transportista; // TransCod: Empresa Transportista
    private String estado;        // EstCod: Estado (A, I, *)

    public TrasladoModel() {}

    public TrasladoModel(String empNif, int resCod, String fechaEnvio, String destCod, String tipoEnvase,
                         String fechaLlegada, String tratamiento, double cantidad, String observacion,
                         String transportista, String estado) {
        this.empNif = empNif;
        this.resCod = resCod;
        this.fechaEnvio = fechaEnvio;
        this.destCod = destCod;
        this.tipoEnvase = tipoEnvase;
        this.fechaLlegada = fechaLlegada;
        this.tratamiento = tratamiento;
        this.cantidad = cantidad;
        this.observacion = observacion;
        this.transportista = transportista;
        this.estado = estado;
    }

    public String getEmpNif() { return empNif; }
    public void setEmpNif(String empNif) { this.empNif = empNif; }

    public int getResCod() { return resCod; }
    public void setResCod(int resCod) { this.resCod = resCod; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getDestCod() { return destCod; }
    public void setDestCod(String destCod) { this.destCod = destCod; }

    public String getTipoEnvase() { return tipoEnvase; }
    public void setTipoEnvase(String tipoEnvase) { this.tipoEnvase = tipoEnvase; }

    public String getFechaLlegada() { return fechaLlegada; }
    public void setFechaLlegada(String fechaLlegada) { this.fechaLlegada = fechaLlegada; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
