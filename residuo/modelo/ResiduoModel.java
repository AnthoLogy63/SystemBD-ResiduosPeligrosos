package residuo.modelo;

public class ResiduoModel {
    private int resCod;
    private String empNif;
    private int resTox;
    private String tipoResCod;
    private String codResNorm;
    private double resCant;
    private String resObs;
    private char resEst;

    public ResiduoModel(int resCod, String empNif, int resTox, String tipoResCod, String codResNorm, double resCant, String resObs, char resEst) {
        this.resCod = resCod;
        this.empNif = empNif;
        this.resTox = resTox;
        this.tipoResCod = tipoResCod;
        this.codResNorm = codResNorm;
        this.resCant = resCant;
        this.resObs = resObs;
        this.resEst = resEst;
    }

    public char getResEst() { 
        return resEst; 
    }
    
    public void setResEst(char resEst) { 
        this.resEst = resEst; 
    }

    public int getResCod() {
        return resCod;
    }

    public void setResCod(int resCod) {
        this.resCod = resCod;
    }

    public String getEmpNif() {
        return empNif;
    }

    public void setEmpNif(String empNif) {
        this.empNif = empNif;
    }

    public int getResTox() {
        return resTox;
    }

    public void setResTox(int resTox) {
        this.resTox = resTox;
    }

    public String getTipoResCod() {
        return tipoResCod;
    }

    public void setTipoResCod(String tipoResCod) {
        this.tipoResCod = tipoResCod;
    }

    public String getCodResNorm() {
        return codResNorm;
    }

    public void setCodResNorm(String codResNorm) {
        this.codResNorm = codResNorm;
    }

    public double getResCant() {
        return resCant;
    }

    public void setResCant(double resCant) {
        this.resCant = resCant;
    }

    public String getResObs() {
        return resObs;
    }

    public void setResObs(String resObs) {
        this.resObs = resObs;
    }
}
