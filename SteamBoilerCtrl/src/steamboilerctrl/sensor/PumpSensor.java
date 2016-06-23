package steamboilerctrl.sensor;

import steamboilerctrl.object.Pump;
import java.util.Random;

public class PumpSensor extends Sensor<Boolean>
{

  public PumpSensor(Pump pump)
  {
    super(pump);
  }

  protected Boolean getRandomValue()
  {
    return this.random.nextBoolean();
  }

  protected Boolean getRealValue()
  {
    return physicalObject.isWorking();
  }

}
