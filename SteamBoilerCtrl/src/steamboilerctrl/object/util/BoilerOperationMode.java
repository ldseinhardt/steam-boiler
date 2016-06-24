package steamboilerctrl.object.util;

public enum BoilerOperationMode
{

  INITIALIZATION (0),
  NORMAL         (1),
  DEGRADED       (2),
  RESCUE         (3),
  EMERGENCY_STOP (4);

  final private int mode;

  BoilerOperationMode(int mode)
  {
    this.mode = mode;
  }

  public int getMode()
  {
    return this.mode;
  }

}
