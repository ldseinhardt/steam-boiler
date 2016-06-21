import javax.realtime.*;

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
        
        mode = OperationMode.INITIALIZATION;
    }
    
    public static void main(String[] args) {
        SteamBoilerCtrl steamBoilerCtrl = new SteamBoilerCtrl();
        
        int pri = PriorityScheduler.instance().getMinPriority() + 10;

        PriorityParameters prip = new PriorityParameters(pri);

        RelativeTime period1s = new RelativeTime(1000, 0); //args: ms, ns

        RelativeTime period5s = new RelativeTime(5000, 0); //args: ms, ns

        PeriodicParameters perp1s = new PeriodicParameters(null, period1s, null, null, null, null);

        PeriodicParameters perp5s = new PeriodicParameters(null, period5s, null, null, null, null);

        RealtimeThread rt1 = new RealtimeThread(prip, perp1s) {
            public void run() {
                try {
                    while (waitForNextRelease()) {
                        steamBoilerCtrl.runPhysicalPlant();        
                    }
                } catch (Exception e) {

                }
            }
        };

        RealtimeThread rt2 = new RealtimeThread(prip, perp5s) {
            public void run() {
                try {
                    while (waitForNextRelease()) {
                        steamBoilerCtrl.runWaterCtrl();        
                    }
                } catch (Exception e) {

                }
            }
        };

        RealtimeThread rt3 = new RealtimeThread(prip, perp5s) {
            public void run() {
                try {
                    while (waitForNextRelease()) {
                        steamBoilerCtrl.pumpCtrl(1);        
                    }
                } catch (Exception e) {

                }
            }
        };

        RealtimeThread rt4 = new RealtimeThread(prip, perp5s) {
            public void run() {
                try {
                    while (waitForNextRelease()) {
                        steamBoilerCtrl.pumpCtrl(2);        
                    }
                } catch (Exception e) {

                }
            }
        };

        RealtimeThread rt5 = new RealtimeThread(prip, perp5s) {
            public void run() {
                try {
                    while (waitForNextRelease()) {
                        steamBoilerCtrl.pumpCtrl(3);        
                    }
                } catch (Exception e) {

                }
            }
        };

        RealtimeThread rt6 = new RealtimeThread(prip, perp5s) {
            public void run() {
                try {
                    while (waitForNextRelease()) {
                        steamBoilerCtrl.pumpCtrl(4);        
                    }
                } catch (Exception e) {

                }
            }
        };

        rt1.start();
        rt2.start();
        rt3.start();
        rt4.start();
        rt5.start();
        rt6.start();
    }
    
    private void pumpsOperate(int n) {
        for (int i = 0; i < PUMP_NUMBERS; i++) {
            pumps[i].setOFF();
        }
        for (int i = 0; i < n; i++) {
            pumps[i].setON();
        }
    }

    public void runPhysicalPlant() {
        for (Pump pump : pumps) {
            steamBoiler.addWater(pump.getWater());            
        }

        steamBoiler.producesStem();

        System.out.println("[Simulação planta física]: " + steamBoiler.getNivel());
    }

    public void runWaterCtrl() {
        int pumpsWorking = 0;
        for (int i = 0; i < PUMP_NUMBERS; i++) {
                if(pumps[i].isOK()){
                        pumpsWorking+=1;
                }
        }
        switch (mode) {
            case INITIALIZATION:
                if (steamBoiler.isNormal()) {
                    mode = OperationMode.NORMAL;
                }
                break;
            case NORMAL:
                if (steamBoiler.isNormal()) {
                    pumpsOperate(1);
                } else if (steamBoiler.isFlooding()) {
                    pumpsOperate(0);                        
                }
                
                if(pumpsWorking != PUMP_NUMBERS){
                    mode = OperationMode.DEGRADED;
                }
                break;
            case DEGRADED:
                if(!steamBoiler.isWorking()){
                   mode = OperationMode.RESCUE;
                }else{
                    if(pumpsWorking == PUMP_NUMBERS){
                        mode = OperationMode.NORMAL;
                    }else if(!steamBoiler.isOK()){
                        mode = OperationMode.EMERGENCY_STOP;
                    }else{
                        if (steamBoiler.isNormal()) {
                            pumpsOperate(1);
                        } else if (steamBoiler.isFlooding()) {
                            pumpsOperate(0);                        
                        }
                    }
                }

                break;
            case RESCUE:

                break;
            case EMERGENCY_STOP:
                System.exit(0);
                return;
        }
        System.out.println("[Controle de água]: [" + mode + "] - " + steamBoiler.getNivel());
    }

    public void pumpCtrl(int pump) {
        System.out.println("[Controle da bomba " + pump + "]");
        
        // PARADA DE 5S para inicializar as bombas
    }
    
}
