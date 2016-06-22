import javax.realtime.*;

public class PumpTaskControl extends Task
{

  private Pump pump;

  private PumpScheduleControl pumpScheduleControl;

  public PumpTaskControl(PhysicalObject physicalObject,
    PumpScheduleControl pumpScheduleControl, SchedulingParameters scheduling,
    ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.pump = (Pump) physicalObject;
    this.pumpScheduleControl = pumpScheduleControl;
  }

  protected PumpTaskControl execute()
  {
    int id = this.pump.getId();
    if (this.pumpScheduleControl.size() > 0 &&
      this.pumpScheduleControl.element().getId() == id) {
      if (this.pumpScheduleControl.element().getStatus() == this.pump.getStatus()) {
        System.out.println(" - [Controle da bomba (" + id + ")]: Nada a fazer.");
      } else {
        if (this.pumpScheduleControl.element().getStatus()) {
          this.pump.setON();
          System.out.println(" - [Controle da bomba (" + id + ")]: bomba inicializada ...");
        } else {
          this.pump.setOFF();
          System.out.println(" - [Controle da bomba (" + id + ")]: bomba desligada.");
        }
      }
      this.pumpScheduleControl.rem();
    } else {
      System.out.println(" - [Controle da bomba (" + id + ")]: Nada a fazer.");
    }
    return this;
  }

}
