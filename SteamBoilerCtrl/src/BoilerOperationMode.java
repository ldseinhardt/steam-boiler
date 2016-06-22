public enum BoilerOperationMode
{

  INITIALIZATION (0),
  NORMAL         (1),
  DEGRADED       (2),
  EMERGENCY_STOP (3);

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
