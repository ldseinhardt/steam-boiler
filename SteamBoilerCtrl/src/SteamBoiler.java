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

  final private int PUMP_NUMBERS = 4;

  private Pump pumps[];

  // Sensores
  private SteamSensor steamSensor;
  private WaterSensor waterSensor;

  public SteamBoiler()
  {
    super();
    mode = BoilerOperationMode.INITIALIZATION;
    // Padrão 200,00 L
    this.capacity = 200;
    this.nivel = 0;
    // Padrão 1,5 L/S
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
  }

  public SteamBoiler setOperationMode(BoilerOperationMode mode)
  {
    this.mode = mode;
    return this;
  }

  public SteamBoiler setCapacity(double capacity)
  {
    this.capacity = capacity;
    return this;
  }

  public SteamBoiler setSteam(double steam)
  {
    this.steam = steam;
    return this;
  }

  public SteamBoiler setNivel(double nivel)
  {
    this.nivel = nivel;
    return this;
  }

  public SteamBoiler addWater(double water)
  {
    this.nivel += water;
    return this;
  }

  public SteamBoiler producesStem()
  {
    this.nivel -= this.steam;
    return this;
  }

  protected SteamBoiler action()
  {
    this.producesStem();
    return this;
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
