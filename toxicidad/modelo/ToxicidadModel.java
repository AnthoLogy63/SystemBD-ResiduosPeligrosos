package toxicidad.modelo;

/**
 * Representa un nivel de toxicidad.
 * Ejemplo:
 *   ToxCod = 1
 *   ToxNom = "Bajo"
 *   ToxEst = "A"
 */
public class ToxicidadModel {
    private String codigo;
    private String nombre;
    private String estado;

    public ToxicidadModel() {}

    public ToxicidadModel(String codigo, String nombre, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
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
}
