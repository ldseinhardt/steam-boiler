public class Pump {
    
    // ligada/desligada
    private boolean status;
    
    // com ou sem problema
    private boolean ok;
    
    // capacidade de bombear (litros/sec)
    private double CAPACITY = 10;
    
    public Pump()  {
        ok = true;
        status = true;
    }
    
    public void setON() {
        status = true;
    }
    
    public void setOFF() {
        status = false;
    }
    
    public double getWater() {
        return status
            ? CAPACITY
            : 0;
    }    
    
    public void setOK(boolean status) {
        ok = status;
    }
    
    public boolean isOK() {
        return ok;
    }
    
}