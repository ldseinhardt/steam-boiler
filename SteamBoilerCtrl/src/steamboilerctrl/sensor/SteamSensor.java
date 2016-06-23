package steamboilerctrl.sensor;

import steamboilerctrl.object.SteamBoiler;
import java.util.Random;

public class SteamSensor extends Sensor<Double>
{

  public SteamSensor(SteamBoiler steamBoiler)
  {
    super(steamBoiler);
  }

  protected Double getRandomValue()
  {
    return this.random.nextDouble();
  }

  protected Double getRealValue()
  {
    return ((SteamBoiler) physicalObject).getSteam();
  }

}
