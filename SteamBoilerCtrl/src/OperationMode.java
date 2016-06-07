public enum OperationMode {
    
    INITIALIZATION(1), NORMAL(2), DEGRADED(3), RESCUE(4), EMERGENCY_STOP(5);

    private final int mode;

    OperationMode(int mode){
        this.mode = mode;
    }
        
    public int getMode(){
        return mode;
    }
    
}
