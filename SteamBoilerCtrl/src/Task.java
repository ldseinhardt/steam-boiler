import javax.realtime.*;

abstract public class Task extends RealtimeThread
{

	protected PhysicalObject physicalObject;

	public Task()
	{
		super();
	}

	public Task(PhysicalObject physicalObject, SchedulingParameters scheduling,
    ReleaseParameters release)
	{
		super(scheduling, release);
		this.physicalObject = physicalObject;
	}

	public Task setPhysicalObject(PhysicalObject physicalObject)
	{
    this.physicalObject = physicalObject;
    return this;
	}

	public void run()
	{
    try {
      while (waitForNextRelease()) {
        this.execute();
      }
    } catch (Exception e) {

    }
	}

  abstract protected Task execute();

}
