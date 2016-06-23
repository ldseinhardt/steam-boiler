package steamboilerctrl.sensor;

import steamboilerctrl.object.PhysicalObject;
import java.util.Random;

abstract public class Sensor<T>
{

  // objeto físico que o sensor realiza medições
  protected PhysicalObject physicalObject;

  // Sensor funcionando corretamente
  protected boolean working;

  // gerador de valores alatórios (simulação de falhas)
  protected Random random;

  public Sensor(PhysicalObject physicalObject)
  {
    this.physicalObject = physicalObject;
    this.working = true;
    this.random = new Random(System.currentTimeMillis());
  }

  public void setWorking(boolean status)
  {
    this.working = status;
  }

  public boolean isWorking()
  {
    return this.working;
  }

  abstract protected T getRandomValue();

  abstract protected T getRealValue();

  public T getValue()
  {
    // caso o sensor esteja com problema, é retornado um valor aletorio
    return this.working
      ? this.getRealValue()
      : this.getRandomValue()
      ;
  }

}
