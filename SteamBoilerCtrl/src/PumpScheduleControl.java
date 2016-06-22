import java.util.*;

public class PumpScheduleControl
{

  private Queue<PumpScheduleControlItem> queue;

  public PumpScheduleControl()
  {
     this.queue = new LinkedList<PumpScheduleControlItem>();
  }

  public PumpScheduleControl add(int id, boolean status)
  {
    this.queue.add(new PumpScheduleControlItem(id, status));
    return this;
  }

  public PumpScheduleControlItem rem()
  {
    return this.queue.remove();
  }

  public PumpScheduleControlItem element()
  {
    return this.queue.element();
  }

  public int size()
  {
    return this.queue.size();
  }

}
