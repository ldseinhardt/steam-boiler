package steamboilerctrl.object;

import steamboilerctrl.object.util.PumpOperationMode;
import steamboilerctrl.sensor.PumpSensor;

public class Pump extends PhysicalObject
{

  // Modo de operação
  private PumpOperationMode mode;

  // caldeira a vapor
  private SteamBoiler steamBoiler;

  // identificador da bomba
  private int id;

  // sensor que detecta a funcionalidade da bomba
  private PumpSensor funcionalSensor;

  // vazão em litros/segundo
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

  protected void action()
  {
    steamBoiler.addWater(this.waterFlow);
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setON()
  {
    this.status = true;
    this.mode = PumpOperationMode.INITIALIZING_STEP_1;
  }

  public void setOFF()
  {
    this.status = false;
    this.mode = PumpOperationMode.STOPPED;
  }

  public void setOperateMode(PumpOperationMode mode)
  {
    this.mode = mode;
  }

  public void execute()
  {
    // caso o sensor esteja com problema, nada é executado
    if (this.mode == PumpOperationMode.WORKING && this.working) {
      this.action();
    }
  }

  public double getWaterFlow()
  {
    return this.waterFlow;
  }

  public int getId()
  {
    return this.id;
  }

  public PumpSensor getFuncionalSensor()
  {
    return this.funcionalSensor;
  }

  public PumpOperationMode getOperationMode()
  {
    return this.mode;
  }

  public SteamBoiler getSteamBoiler()
  {
    return this.steamBoiler;
  }

}
