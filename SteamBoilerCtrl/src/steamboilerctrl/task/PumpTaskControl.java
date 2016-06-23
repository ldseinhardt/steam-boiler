package steamboilerctrl.task;

import steamboilerctrl.object.PhysicalObject;
import steamboilerctrl.object.Pump;
import steamboilerctrl.object.util.PumpOperationMode;
import steamboilerctrl.sensor.PumpSensor;
import steamboilerctrl.task.util.PumpScheduleQueue;
import javax.realtime.SchedulingParameters;
import javax.realtime.ReleaseParameters;

public class PumpTaskControl extends Task
{
  // bomba
  private Pump pump;

  // id da bomba
  private int id;

  // sensor da bomba
  private PumpSensor sensor;

  // fila de mensagens, que indicam o modo de operação das bombas
  private PumpScheduleQueue pumpScheduleQueue;

  public PumpTaskControl(PhysicalObject physicalObject,
    PumpScheduleQueue pumpScheduleQueue, SchedulingParameters scheduling,
    ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.pump = (Pump) physicalObject;
    this.id = this.pump.getId();
    this.sensor = this.pump.getFuncionalSensor();
    this.pumpScheduleQueue = pumpScheduleQueue;

    // Não exibe mensagens de log da tarefa
    //this.setLog(false);
  }

  protected void execute()
  {
    this.log("");
    this.log(" - [Controle da bomba " + this.id + "]:");

    String description = "";
    if (this.pump.getOperationMode() == PumpOperationMode.STOPPED) {
      description += "parada/";
    } else if (this.pump.getOperationMode() == PumpOperationMode.WORKING) {
      description += "ligada/";
    } else {
      description += "inicializando/";
    }
    if (this.sensor.getValue()) {
      description += "funcional";
    } else {
      description += "com problema";
    }

    // verifica se tem solicitação para ligar/desligar alguma bomba
    if (this.pumpScheduleQueue.size() > 0 && this.pumpScheduleQueue.element().getId() == this.id) {
      if (this.pumpScheduleQueue.element().getStatus() == this.pump.getStatus()) {
        this.log("     * " + description + ".");
      } else {
        if (this.pumpScheduleQueue.element().getStatus()) {
          this.pump.setON();
          this.log("     * inicializada ...");
        } else {
          this.pump.setOFF();
          this.log("     * desligada.");
        }
      }
      this.pumpScheduleQueue.remove();
    } else {
      this.log("     * " + description + ".");
    }
  }

}
