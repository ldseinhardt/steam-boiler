public class PumpScheduleControlItem
{

  private int id;

  private boolean status;

  public PumpScheduleControlItem(int id, boolean status)
  {
    this.id = id;
    this.status = status;
  }

  public PumpScheduleControlItem setId(int id)
  {
    this.id = id;
    return this;
  }

  public PumpScheduleControlItem setStatus(boolean status)
  {
    this.status = status;
    return this;
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
