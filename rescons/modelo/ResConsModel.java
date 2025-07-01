package rescons.modelo;

public class ResConsModel {
    private String empNif;        // EmpNif (PK)
    private int resCod;           // ResCod (PK)
    private int conCod;           // ConCod (PK)
    private double cantidad;      // ResConCan
    private String estado;        // ResConEst

    public ResConsModel() {}

    public ResConsModel(String empNif, int resCod, int conCod, double cantidad, String estado) {
        this.empNif = empNif;
        this.resCod = resCod;
        this.conCod = conCod;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public String getEmpNif() {
        return empNif;
    }

    public void setEmpNif(String empNif) {
        this.empNif = empNif;
    }

    public int getResCod() {
        return resCod;
    }

    public void setResCod(int resCod) {
        this.resCod = resCod;
    }

    public int getConCod() {
        return conCod;
    }

    public void setConCod(int conCod) {
        this.conCod = conCod;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("ResCons[Empresa=%s, Residuo=%d, Constituyente=%d, Cantidad=%.2f, Estado=%s]",
                empNif, resCod, conCod, cantidad, estado);
    }
}
