package empresa.modelo;

public class EmpresaModel {
    private String nif;
    private String nombre;
    private String ciudadCod;
    private String actividad;
    private String observacion;
    private String estado; // A, I, *

    public EmpresaModel() {}

    public EmpresaModel(String nif, String nombre, String ciudadCod, String actividad, String observacion, String estado) {
        this.nif = nif;
        this.nombre = nombre;
        this.ciudadCod = ciudadCod;
        this.actividad = actividad;
        this.observacion = observacion;
        this.estado = estado;
    }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudadCod() { return ciudadCod; }
    public void setCiudadCod(String ciudadCod) { this.ciudadCod = ciudadCod; }

    public String getActividad() { return actividad; }
    public void setActividad(String actividad) { this.actividad = actividad; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCiudadCodigo() {
        return ciudadCod;
    }
}
