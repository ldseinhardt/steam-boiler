import javax.realtime.*;

public class MainTaskControl extends Task
{

  private SteamBoiler steamBoiler;

  private PumpScheduleControl pumpScheduleControl;

  public MainTaskControl(PhysicalObject physicalObject,
    PumpScheduleControl pumpScheduleControl, SchedulingParameters scheduling,
    ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.steamBoiler = (SteamBoiler) physicalObject;
    this.pumpScheduleControl = pumpScheduleControl;
  }

  protected MainTaskControl execute()
  {
    // em testes
    switch (this.steamBoiler.getOperationMode()) {
      case INITIALIZATION:
        if (this.steamBoiler.getNivel() > this.steamBoiler.getNormalLimitMin()) {
          this.pumpScheduleControl.add(1, true);
          this.pumpScheduleControl.add(2, true);
          this.pumpScheduleControl.add(3, true);
          this.pumpScheduleControl.add(4, false);
          this.steamBoiler.setOperationMode(BoilerOperationMode.NORMAL);
        }
        break;
      case NORMAL:
        if (this.steamBoiler.getNivel() < this.steamBoiler.getNormalLimitMin()) {
          this.pumpScheduleControl.add(1, true);
          this.pumpScheduleControl.add(2, true);
          this.pumpScheduleControl.add(3, true);
          this.pumpScheduleControl.add(4, true);
        } else if (this.steamBoiler.getNivel() > this.steamBoiler.getNormalLimitMax()) {
          this.pumpScheduleControl.add(1, true);
          this.pumpScheduleControl.add(2, false);
          this.pumpScheduleControl.add(3, false);
          this.pumpScheduleControl.add(4, false);
        } else {
          this.pumpScheduleControl.add(1, false);
          this.pumpScheduleControl.add(2, true);
          this.pumpScheduleControl.add(3, true);
          this.pumpScheduleControl.add(4, true);
        }
        break;
      case DEGRADED:

        break;
      case EMERGENCY_STOP:

        break;
    }

    // test desliga a bomba 1
    //if (this.steamBoiler.getPumps()[0].getStatus()) {
    //  this.pumpScheduleControl.add(1, false);
    //  this.pumpScheduleControl.add(2, false);
    //}

    //System.out.println(" - [Controle principal]: ");

    return this;
  }

}
