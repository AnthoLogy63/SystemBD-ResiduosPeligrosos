package transportista.modelo;

public class TransportistaModel {
    private String nif;           // EmpTransNif
    private String nombre;        // EmpTransNom
    private String ciudadCod;     // EmpTransCiu (FK)
    private String tipoCod;       // EmpTransTip (FK)
    private String estado;        // EmpTransEst
    private String observacion;   // EmpTransObs

    public TransportistaModel() {}

    public TransportistaModel(String nif, String nombre, String ciudadCod, String tipoCod, String estado, String observacion) {
        this.nif = nif;
        this.nombre = nombre;
        this.ciudadCod = ciudadCod;
        this.tipoCod = tipoCod;
        this.estado = estado;
        this.observacion = observacion;
    }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudadCod() { return ciudadCod; }
    public void setCiudadCod(String ciudadCod) { this.ciudadCod = ciudadCod; }

    public String getTipoCod() { return tipoCod; }
    public void setTipoCod(String tipoCod) { this.tipoCod = tipoCod; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
