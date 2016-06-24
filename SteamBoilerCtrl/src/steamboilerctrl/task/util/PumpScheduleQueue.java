package steamboilerctrl.task.util;

import java.util.*;

public class PumpScheduleQueue
{

  private Queue<PumpScheduleMessage> queue;

  public PumpScheduleQueue()
  {
    this.queue = new LinkedList<PumpScheduleMessage>();
  }

  public void add(int id, boolean status)
  {
    this.queue.add(new PumpScheduleMessage(id, status));
  }

  public void add(PumpScheduleMessage message)
  {
    this.queue.add(message);
  }

  public PumpScheduleMessage remove()
  {
    return this.queue.remove();
  }

  public PumpScheduleMessage element()
  {
    return this.queue.element();
  }

  public int size()
  {
    return this.queue.size();
  }

}
