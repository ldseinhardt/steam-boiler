import java.util.Random;

abstract public class Sensor<T>
{

  protected PhysicalObject physicalObject;

  // Sensor funcionando corretamente
  protected boolean working;

  protected Random random;

  public Sensor(PhysicalObject physicalObject)
  {
    this.physicalObject = physicalObject;
    this.working = true;
    this.random = new Random(System.currentTimeMillis());
  }

  public Sensor setWorking(boolean status)
  {
    this.working = status;
    return this;
  }

  public boolean isWorking()
  {
    return this.working;
  }

  abstract protected T getRandomValue();

  abstract protected T getRealValue();

  public T getValue() {
    // caso o sensor esteja com problema, é retornado um valor aletorio
    return this.working
      ? this.getRealValue()
      : this.getRandomValue()
      ;
  }

}
