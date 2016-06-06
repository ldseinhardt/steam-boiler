package controlecaldeiravapor;

public class CaldeiraVapor {
    
    // limite de agua
    private double limite;
    
    // nivel de agua
    private double nivel;
    
    public CaldeiraVapor() {
        limite = 1000;
        nivel = 0;
    }
    
    public CaldeiraVapor(double nivel, double limite) {
        this.nivel = nivel;
        this.limite = limite;
    }
    
    public double getNivelAgua() {
        return nivel;
    }
    
    public double getLimiteAgua() {
        return limite;
    }
    
    public void setNivelAgua(double nivel) {
        this.nivel = nivel;
    }
    
    public void setLimiteAgua(double limite) {
        this.limite = limite;
    }
    
    public void addAgua(double agua) {
        nivel += agua;
    }
    
    public void remAgua(double agua) {
        nivel -= agua;
    }
    
}
