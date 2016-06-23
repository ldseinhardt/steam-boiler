package steamboilerctrl.task.util;

public class PumpScheduleMessage
{

  private int id;

  private boolean status;

  public PumpScheduleMessage(int id, boolean status)
  {
    this.id = id;
    this.status = status;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setStatus(boolean status)
  {
    this.status = status;
  }

  public int getId()
  {
    return this.id;
  }

  public boolean getStatus()
  {
    return this.status;
  }

}
