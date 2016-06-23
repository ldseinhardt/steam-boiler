package steamboilerctrl.task;

import steamboilerctrl.object.PhysicalObject;
import steamboilerctrl.object.SteamBoiler;
import steamboilerctrl.object.Pump;
import steamboilerctrl.object.util.BoilerOperationMode;
import steamboilerctrl.object.util.PumpOperationMode;
import javax.realtime.SchedulingParameters;
import javax.realtime.ReleaseParameters;

public class PhysicalTask extends Task
{

  // caldeira a vapor
  private SteamBoiler steamBoiler;

  // contador de execuções da tarefa
  private int count;

  public PhysicalTask(PhysicalObject physicalObject,
    SchedulingParameters scheduling, ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.steamBoiler = (SteamBoiler) physicalObject;
    this.count = 0;

    // Não exibe mensagens de log da tarefa
    //this.setLog(false);
  }

  protected void execute()
  {
    this.log("");
    this.log(" - [Planta física]:");

    for (Pump pump : this.steamBoiler.getPumps()) {
      pump.execute();
      switch (pump.getOperationMode()) {
        case INITIALIZING_STEP_1:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_2);
          this.log("     * aguardando inicialização da bomba" + (pump.getId()) + " (1s) ...");
          break;
        case INITIALIZING_STEP_2:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_3);
          this.log("     * aguardando inicialização da bomba" + (pump.getId()) + " (2s) ...");
          break;
        case INITIALIZING_STEP_3:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_4);
          this.log("     * aguardando inicialização da bomba" + (pump.getId()) + " (3s) ...");
          break;
        case INITIALIZING_STEP_4:
          pump.setOperateMode(PumpOperationMode.INITIALIZING_STEP_5);
          this.log("     * aguardando inicialização da bomba" + (pump.getId()) + " (4s) ...");
          break;
        case INITIALIZING_STEP_5:
          pump.setOperateMode(PumpOperationMode.WORKING);
          this.log("     * aguardando inicialização da bomba" + (pump.getId()) + " (5s) ...");
          break;
      }
    }

    // inicialização da caldeira
    if (this.count >= 5) {
      this.steamBoiler.execute();
    }

    this.count++;

    int pct = (int) (this.steamBoiler.getNivel() / this.steamBoiler.getCapacity() * 100);
    this.log("     * modo: " + this.steamBoiler.getOperationMode());
    this.log("     * nível: " + this.steamBoiler.getNivel() + "L = " + pct + "%");
  }

}
