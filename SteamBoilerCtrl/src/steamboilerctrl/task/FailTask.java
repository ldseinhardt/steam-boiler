package steamboilerctrl.task;

import steamboilerctrl.object.PhysicalObject;
import steamboilerctrl.object.SteamBoiler;
import steamboilerctrl.sensor.Sensor;
import java.util.Random;
import javax.realtime.SchedulingParameters;
import javax.realtime.ReleaseParameters;

public class FailTask extends Task
{

  // caldeira a vapor
  private SteamBoiler steamBoiler;

  // gerador de números aleatórios (simulação de falhas)
  private Random random;

  // taxa de falhas
  private double rate;

  // limite para o gerador de valores aleatórios
  final private int LIMIT = 1000;

  public FailTask(PhysicalObject physicalObject, double rate,
    SchedulingParameters scheduling, ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.steamBoiler = (SteamBoiler) physicalObject;
    this.rate = LIMIT - (rate * LIMIT);
    this.random = new Random(System.currentTimeMillis());

    // Não exibe mensagens de log da tarefa
    //this.setLog(false);
  }

  protected void execute()
  {
    this.log("");
    this.log(" - [Simulação de falha]:");

    // sorteia um número
    int sorted = this.random.nextInt(LIMIT);

    // se o número sorteado esta acima da taxa de falhas
    if (sorted > rate) {
      PhysicalObject physicalObject = null;
      Sensor sensor = null;
      String name = "";

      // sorteia oque deve falhar
      switch (this.random.nextInt(3)) {
        case 0: // sensor de nível de agua
            sensor = steamBoiler.getWaterSensor();
            name = "sensor do nível de água";
          break;
        case 1: // sensor de vapor
            sensor = steamBoiler.getSteamSensor();
            name = "sensor de produção de vapor";
          break;
        case 2: // sensor de alguma bomba
            // sorteia alguma bomba
            int pump = this.random.nextInt(steamBoiler.getNumberOfPumps());
            physicalObject = steamBoiler.getPumps()[pump];
            name = "bomba " + (pump + 1);
          break;
      }

      // sorteia se o objeto ira falhar, voltar a funcionar
      // ou possívelmente se manter no mesmo estado
      boolean status = this.random.nextBoolean();

      if (sensor != null) {
        if (sensor.isWorking() == status) {
          this.log("     * " + name + " mantem-se no mesmo estado de funcionalidade.");
        } else if (status) {
          this.log("     * " + name + " voltou a funcionar. : )");
        } else {
          this.log("     * " + name + " parou de funcionar. : (");
        }
        sensor.setWorking(status);
      }

      if (physicalObject != null) {
        if (physicalObject.isWorking() == status) {
          this.log("     * " + name + " mantem-se no mesmo estado de funcionalidade.");
        } else if (status) {
          this.log("     * " + name + " voltou a funcionar. : )");
        } else {
          this.log("     * " + name + " parou de funcionar. : (");
        }
        physicalObject.setWorking(status);
      }
    } else {
      this.log("     * você errou seu chute !!!");
    }
  }

}
