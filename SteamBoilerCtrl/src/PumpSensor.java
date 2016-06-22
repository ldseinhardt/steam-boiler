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

  public Boolean getRealValue()
  {
    return physicalObject.getStatus();
  }

}
