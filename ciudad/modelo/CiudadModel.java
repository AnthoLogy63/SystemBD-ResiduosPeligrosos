package ciudad.modelo;

public class CiudadModel {
    private String codigo;
    private String nombre;
    private String estado;
    private String regionCodigo;

    public CiudadModel() {}

    public CiudadModel(String codigo, String nombre, String estado, String regionCodigo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
        this.regionCodigo = regionCodigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRegionCodigo() {
        return regionCodigo;
    }

    public void setRegionCodigo(String regionCodigo) {
        this.regionCodigo = regionCodigo;
    }
}
