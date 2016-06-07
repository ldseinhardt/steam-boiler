public class SteamBoiler {
    
    // Nível atual de água
    private double nivel;
    
    // Limite mínimo de água para operar (???)
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
        return nivel;
    }
    
    public boolean isNormal() {
        return nivel >= NORMAL_LIMIT_MIN && nivel <= NORMAL_LIMIT_MAX;
    }
    
    public boolean isOK() {
        return nivel >= LIMIT_MIN && nivel <= LIMIT_MAX;
    }
   
}
