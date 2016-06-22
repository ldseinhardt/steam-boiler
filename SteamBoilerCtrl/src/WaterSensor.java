import java.util.Random;

public class WaterSensor extends Sensor<Double>
{

  public WaterSensor(SteamBoiler steamBoiler)
  {
    super(steamBoiler);
  }

  protected Double getRandomValue()
  {
    return this.random.nextDouble();
  }

  public Double getRealValue()
  {
    return ((SteamBoiler) physicalObject).getNivel();
  }

}
