public class Pump extends PhysicalObject
{

  // Modo de operação
  private PumpOperationMode mode;

  private SteamBoiler steamBoiler;

  private int id;

  private PumpSensor funcionalSensor;

  // vazão em L/S
  private double waterFlow;

  public Pump(SteamBoiler steamBoiler, int id)
  {
    super();
    this.mode = PumpOperationMode.STOPPED;
    this.steamBoiler = steamBoiler;
    this.id = id;
    this.waterFlow = 0.75;
    this.funcionalSensor = new PumpSensor(this);
  }

  public Pump(SteamBoiler steamBoiler, int id, double waterFlow)
  {
    super();
    this.mode = PumpOperationMode.STOPPED;
    this.steamBoiler = steamBoiler;
    this.id = id;
    this.waterFlow = waterFlow;
    this.funcionalSensor = new PumpSensor(this);
  }

  protected Pump action()
  {
    steamBoiler.addWater(this.waterFlow);
    return this;
  }

  public Pump setId(int id)
  {
    this.id = id;
    return this;
  }

  public int getId()
  {
    return this.id;
  }

  public PumpSensor getFuncionalSensor()
  {
    return this.funcionalSensor;
  }

  public Pump setON()
  {
    this.status = true;
    this.mode = PumpOperationMode.INITIALIZING_STEP_1;
    return this;
  }

  public Pump setOFF()
  {
    this.status = false;
    this.mode = PumpOperationMode.STOPPED;
    return this;
  }

  public Pump setOperateMode(PumpOperationMode mode)
  {
    this.mode = mode;
    return this;
  }

  public PumpOperationMode getOperationMode()
  {
    return this.mode;
  }

  public Pump execute() {
    // caso o sensor esteja com problema, nada é executado
    return (this.mode == PumpOperationMode.WORKING) && this.working
      ? this.action()
      : this
      ;
  }

}
