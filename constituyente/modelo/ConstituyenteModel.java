package constituyente.modelo;

public class ConstituyenteModel {
    private int codigo;
    private String nombre;
    private String observacion;
    private char estado;

    public ConstituyenteModel() {}

    public ConstituyenteModel(int codigo, String nombre, String observacion, char estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.observacion = observacion;
        this.estado = estado;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public char getEstado() { return estado; }
    public void setEstado(char estado) { this.estado = estado; }
}
