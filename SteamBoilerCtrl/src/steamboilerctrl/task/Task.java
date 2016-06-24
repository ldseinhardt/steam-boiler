package steamboilerctrl.task;

import steamboilerctrl.object.PhysicalObject;
import javax.realtime.RealtimeThread;
import javax.realtime.SchedulingParameters;
import javax.realtime.ReleaseParameters;

abstract public class Task extends RealtimeThread
{

  // objeto físico a qual a tarefa possui acesso
  protected PhysicalObject physicalObject;

  // exibe mensagens de log da tarefa ?
  protected boolean log;

  public Task()
  {
    super();
  }

  public Task(PhysicalObject physicalObject, SchedulingParameters scheduling,
    ReleaseParameters release)
  {
    super(scheduling, release);
    this.physicalObject = physicalObject;
    this.log = true;
  }

  public void setPhysicalObject(PhysicalObject physicalObject)
  {
    this.physicalObject = physicalObject;
  }

  public void run()
  {
    try {
      while (waitForNextRelease()) {
        this.execute();
      }
    } catch (Exception e) {
      System.out.println(" *** Problema *** - Tarefa: " + this.getClass().getSimpleName());
    }
  }

  abstract protected void execute();

  protected void setLog(boolean status)
  {
    this.log = status;
  }

  protected void log(String message)
  {
    if (this.log) {
      System.out.println(message);
    }
  }

}
