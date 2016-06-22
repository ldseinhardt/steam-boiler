import javax.realtime.*;

public class PhysicalTask extends Task
{

  private SteamBoiler steamBoiler;

  private int count;

	public PhysicalTask(PhysicalObject physicalObject,
    SchedulingParameters scheduling, ReleaseParameters release)
	{
		super(physicalObject, scheduling, release);
    this.steamBoiler = (SteamBoiler) physicalObject;
    this.count = 0;
	}

	protected PhysicalTask execute()
	{
    for (Pump pump : this.steamBoiler.getPumps()) {
      pump.execute();

      switch (pump.getOperationMode()) {
        case INITIALIZING_STEP_1:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_2);
          System.out.println(" * aguardando inicialização da bomba" + (pump.getId()) + " (1s) ...");
          break;
        case INITIALIZING_STEP_2:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_3);
          System.out.println(" * aguardando inicialização da bomba" + (pump.getId()) + " (2s) ...");
          break;
        case INITIALIZING_STEP_3:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_4);
          System.out.println(" * aguardando inicialização da bomba" + (pump.getId()) + " (3s) ...");
          break;
        case INITIALIZING_STEP_4:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_5);
          System.out.println(" * aguardando inicialização da bomba" + (pump.getId()) + " (4s) ...");
          break;
        case INITIALIZING_STEP_5:
          pump.setOperateMode(PumpOperationMode.WORKING);
          System.out.println(" * aguardando inicialização da bomba" + (pump.getId()) + " (5s) ...");
          break;
      }
    }

    if (this.count >= 5) {
      this.steamBoiler.execute();
    }

    System.out.println(" - [Planta física]: (Modo: " + this.steamBoiler.getOperationMode() + ") (Nível: " + this.steamBoiler.getNivel() + "L = " + ((int) (this.steamBoiler.getNivel() / this.steamBoiler.getCapacity() * 100)) + "%)");

    this.count++;

    return this;
	}

}
