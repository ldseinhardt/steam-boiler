public class SteamBoilerCtrl {

    private SteamBoiler steamBoiler;
    
    private Pump pumps[];
    
    private final int PUMP_NUMBERS = 4;
    
    private OperationMode mode;
    
    public SteamBoilerCtrl() {
        System.out.println("SteamBoilerCtrl");
        
        steamBoiler = new SteamBoiler();
        
        pumps = new Pump[PUMP_NUMBERS];
        
        for (int i = 0; i < PUMP_NUMBERS; i++) {
            pumps[i] = new Pump();
        }
        
        // PARADA DE 5S
        
        mode = OperationMode.INITIALIZATION;
    }
    
    public static void main(String[] args) {
        new SteamBoilerCtrl().run();  
    }
    
    private void pumpsOperate(int n) {
        for (int i = 0; i < PUMP_NUMBERS; i++) {
            pumps[i].setOFF();
        }
        for (int i = 0; i < n; i++) {
            pumps[i].setON();
        }
    }
    
    public void run() {
        while (true) { 
            for (Pump pump : pumps) {
                steamBoiler.addWater(pump.getWater());            
            }

            steamBoiler.producesStem();
            
            System.out.println("[" + mode + "]" + steamBoiler.getNivel());
        
            switch (mode) {
                case INITIALIZATION:
                    if (steamBoiler.isNormal()) {
                        mode = OperationMode.NORMAL;
                    }
                    break;
                case NORMAL:
                    if (steamBoiler.isNormal()) {
                        pumpsOperate(1);
                    } else if (!steamBoiler.isOK()) {
                        pumpsOperate(0);                        
                    }                    
                    break;
                case DEGRADED:

                    break;
                case RESCUE:

                    break;
                case EMERGENCY_STOP:
                    return;
            } 
        }
    }
    
}
