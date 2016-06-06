package controlecaldeiravapor;
 
public class Bomba {
    
    private double vasao;
    
    public Bomba()  {
        vasao = 1;
    }
    
    public Bomba(double vasao) {
        this.vasao = vasao;
    }
    
    public double getAgua() {
        return vasao;
    }
    
}
