package steamboilerctrl.object.util;

public enum PumpOperationMode
{

  STOPPED             (0),
  INITIALIZING_STEP_1 (1),
  INITIALIZING_STEP_2 (2),
  INITIALIZING_STEP_3 (3),
  INITIALIZING_STEP_4 (4),
  INITIALIZING_STEP_5 (5),
  WORKING             (6);

  final private int mode;

  PumpOperationMode(int mode)
  {
    this.mode = mode;
  }

  public int getMode()
  {
    return this.mode;
  }

}
