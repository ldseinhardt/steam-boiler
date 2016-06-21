import java.util.Random;
public class SteamBoiler {
    
    //Medidor com ou sem problema
    private boolean working;
    
    // Nível atual de água
    private double nivel;
    
    // Produção de vapor (???)
    private final double STEAM_PER_SECOND = 1;
    
    // Limite mínimo de água para operar
    private final double LIMIT_MIN = 100;
    
    //Limite máximo de água para operar
    private final double LIMIT_MAX = 1000000;
    
    // Limite mínimo de água recomendado para operar
    private final double NORMAL_LIMIT_MIN = 1000;
    
    //Limite máximo de água recomendado para operar
    private final double NORMAL_LIMIT_MAX = 800000;
    
    public SteamBoiler() {
        nivel = 0;
        working = true;
    }
    
    public void addWater(double water) {
        nivel += water;
    }
    
    // transforma água em vapor
    public void producesStem() {
        nivel -= STEAM_PER_SECOND;
    }
    
    public double getProducesStem() {
        return STEAM_PER_SECOND;
    }
    
    public double getNivel() {
        if(this.working){
            return this.nivel;
        }else{
            // caso o medidor esteja com problema, é enviado um valor aletorio 
            //como nivel.
            Random randomVal = new Random();
            double errNivel = randomVal.nextDouble()*randomVal.nextInt(10000);
            return errNivel;
        }
    }
    
    public void setWorking(boolean status){
        this.working = status;
    }
    
    public boolean isNormal() {
        return nivel >= NORMAL_LIMIT_MIN && nivel <= NORMAL_LIMIT_MAX;
    }
    
    public boolean isOK() {
        return nivel >= LIMIT_MIN && nivel <= LIMIT_MAX;
    }
    
    public boolean isWorking(){
        return working;
    }
    
    public boolean isDrying(){
        return nivel <= NORMAL_LIMIT_MIN;
    }
    
    public boolean isFlooding(){
        return nivel >=NORMAL_LIMIT_MAX;
    }
}
