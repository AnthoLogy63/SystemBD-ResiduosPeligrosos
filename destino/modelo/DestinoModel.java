package destino.modelo;

public class DestinoModel {
    private String codigo;        // DestCod
    private String nombre;        // DestNom
    private String ciudadCod;     // DestCiu (FK)
    private String regionCod;     // RegCod (FK)
    private String tipoResiduo;   // DestTipoResCod (FK)
    private double capacidad;     // DestCapacidad
    private double capacidadUsada;// DestCapUsada
    private String fechaCierre;   // DestFecCierreEstim (formato: yyyy-MM-dd)
    private String aviso;         // DestAviso
    private String estado;        // DestEst (A/I/*)
    private String observacion;   // DestObs

    public DestinoModel() {}

    public DestinoModel(String codigo, String nombre, String ciudadCod, String regionCod, String tipoResiduo,
                        double capacidad, double capacidadUsada, String fechaCierre,
                        String aviso, String estado, String observacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudadCod = ciudadCod;
        this.regionCod = regionCod;
        this.tipoResiduo = tipoResiduo;
        this.capacidad = capacidad;
        this.capacidadUsada = capacidadUsada;
        this.fechaCierre = fechaCierre;
        this.aviso = aviso;
        this.estado = estado;
        this.observacion = observacion;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudadCod() { return ciudadCod; }
    public void setCiudadCod(String ciudadCod) { this.ciudadCod = ciudadCod; }

    public String getRegionCod() { return regionCod; }
    public void setRegionCod(String regionCod) { this.regionCod = regionCod; }

    public String getTipoResiduo() { return tipoResiduo; }
    public void setTipoResiduo(String tipoResiduo) { this.tipoResiduo = tipoResiduo; }

    public double getCapacidad() { return capacidad; }
    public void setCapacidad(double capacidad) { this.capacidad = capacidad; }

    public double getCapacidadUsada() { return capacidadUsada; }
    public void setCapacidadUsada(double capacidadUsada) { this.capacidadUsada = capacidadUsada; }

    public String getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(String fechaCierre) { this.fechaCierre = fechaCierre; }

    public String getAviso() { return aviso; }
    public void setAviso(String aviso) { this.aviso = aviso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
