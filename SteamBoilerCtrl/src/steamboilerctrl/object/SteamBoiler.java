package steamboilerctrl.object;

import steamboilerctrl.object.util.BoilerOperationMode;
import steamboilerctrl.sensor.WaterSensor;
import steamboilerctrl.sensor.SteamSensor;

public class SteamBoiler extends PhysicalObject
{

  // Modo de operação
  private BoilerOperationMode mode;

  // Capacidade total de água
  private double capacity;

  // Nível atual de água
  private double nivel;

  // Produção de vapor (constante)
  private double steam;

  // Limites em (%)
  final private double INDEX_LIMIT_MAX = 0.80;
  final private double INDEX_LIMIT_NORMAL_MAX = 0.65;
  final private double INDEX_LIMIT_NORMAL_MIN = 0.20;
  final private double INDEX_LIMIT_MIN = 0.10;

  // número de bombas
  final private int PUMP_NUMBERS = 4;

  // bombas
  private Pump pumps[];

  // Sensor de vapor
  private SteamSensor steamSensor;

  // Sensor do nível de água
  private WaterSensor waterSensor;

  public SteamBoiler()
  {
    super();
    mode = BoilerOperationMode.INITIALIZATION;
    // Padrão em litros
    this.capacity = 100;
    this.nivel = 0;
    // Padrão em litros/segundo
    this.steam = 1.5;

    this.pumps = new Pump[PUMP_NUMBERS];

    for (int i = 0; i < PUMP_NUMBERS; i++) {
      pumps[i] = new Pump(this, i + 1);
    }

    this.steamSensor = new SteamSensor(this);
    this.waterSensor = new WaterSensor(this);
  }

  public SteamBoiler(double capacity, double steam, double waterFlow)
  {
    super();
    mode = BoilerOperationMode.INITIALIZATION;
    this.capacity = capacity;
    this.steam = steam;
    this.nivel = 0;

    this.pumps = new Pump[PUMP_NUMBERS];

    for (int i = 0; i < PUMP_NUMBERS; i++) {
      pumps[i] = new Pump(this, i + 1, waterFlow);
    }

    this.steamSensor = new SteamSensor(this);
    this.waterSensor = new WaterSensor(this);
  }

  public void setOperationMode(BoilerOperationMode mode)
  {
    this.mode = mode;
  }

  public void setCapacity(double capacity)
  {
    this.capacity = capacity;
  }

  public void setSteam(double steam)
  {
    this.steam = steam;
  }

  public void setNivel(double nivel)
  {
    this.nivel = nivel;
  }

  public void addWater(double water)
  {
    this.nivel += water;
  }

  public void producesStem()
  {
    this.nivel -= this.steam;
  }

  protected void action()
  {
    this.producesStem();
  }

  public BoilerOperationMode getOperationMode()
  {
    return this.mode;
  }

  public double getCapacity()
  {
    return this.capacity;
  }

  public double getSteam()
  {
    return this.steam;
  }

  public double getNivel()
  {
    return this.nivel;
  }

  public double getLimitMin()
  {
    return this.capacity * INDEX_LIMIT_MIN;
  }

  public double getLimitMax()
  {
    return this.capacity * INDEX_LIMIT_MAX;
  }

  public double getNormalLimitMin()
  {
    return this.capacity * INDEX_LIMIT_NORMAL_MIN;
  }

  public double getNormalLimitMax()
  {
    return this.capacity * INDEX_LIMIT_NORMAL_MAX;
  }

  public boolean isNormal()
  {
    return this.nivel >= this.getNormalLimitMin() &&
      this.nivel <= this.getNormalLimitMax();
  }

  public boolean isDrying()
  {
    return this.nivel <= this.getLimitMin();
  }

  public boolean isFlooding()
  {
    return this.nivel >= this.getLimitMax();
  }

  public int getNumberOfPumps()
  {
    return PUMP_NUMBERS;
  }

  public Pump[] getPumps()
  {
    return this.pumps;
  }

  public SteamSensor getSteamSensor()
  {
    return this.steamSensor;
  }

  public WaterSensor getWaterSensor()
  {
    return this.waterSensor;
  }

}
