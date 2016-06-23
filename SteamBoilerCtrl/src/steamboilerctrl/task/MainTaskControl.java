package steamboilerctrl.task;

import steamboilerctrl.object.PhysicalObject;
import steamboilerctrl.object.SteamBoiler;
import steamboilerctrl.object.Pump;
import steamboilerctrl.object.util.BoilerOperationMode;
import steamboilerctrl.sensor.SteamSensor;
import steamboilerctrl.sensor.WaterSensor;
import steamboilerctrl.task.util.PumpScheduleQueue;
import javax.realtime.SchedulingParameters;
import javax.realtime.ReleaseParameters;

public class MainTaskControl extends Task
{

  // caldeira a vapor
  private SteamBoiler steamBoiler;

  // sensor de vapor
  private SteamSensor steamSensor;

  // sensor do nível de água
  private WaterSensor waterSensor;

  // fila de mensagens, que indicam o modo de operação das bombas
  private PumpScheduleQueue pumpScheduleQueue;

  // número de bombas
  private int numberOfPumps;

  // produção de vapor em litros/segundo
  private double steamProduce;

  // vazão de água em litros/segundo
  private double waterFlow;

  // nível de água atual
  private double nivel;

  public MainTaskControl(PhysicalObject physicalObject,
    PumpScheduleQueue pumpScheduleQueue, SchedulingParameters scheduling,
    ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.steamBoiler = (SteamBoiler) physicalObject;
    this.steamSensor = this.steamBoiler.getSteamSensor();
    this.waterSensor = this.steamBoiler.getWaterSensor();
    this.pumpScheduleQueue = pumpScheduleQueue;
    this.numberOfPumps = this.steamBoiler.getNumberOfPumps();
    this.steamProduce = this.steamBoiler.getSteam();
    this.waterFlow = this.steamBoiler.getPumps()[0].getWaterFlow();
    this.nivel = 0;

    // Não exibe mensagens de log da tarefa
    //this.setLog(false);
  }

  protected void execute()
  {
    this.log("");
    this.log(" - [Controle principal]:");
    /*
    double old = this.nivel;
    this.nivel = (double) this.steamBoiler.getWaterSensor().getValue();

    if (this.nivel >= (old + (this.countPumpsON() * this.waterFlow * 5))) {
      System.out.println(" * Correção Sensor: " + this.nivel + " para " + old);
      this.nivel = old;
    }
    if (this.nivel <= (old + (this.countPumpsON() * this.waterFlow * 5))) {
      System.out.println(" * Correção Sensor: " + this.nivel + " para " + old);
      this.nivel = old;
    }

    System.out.println("+++++++++++++++++++++" + this.nivel);
*/
    this.nivel = (double) this.steamBoiler.getNivel();

    switch (this.steamBoiler.getOperationMode()) {
      case INITIALIZATION:
        if (this.nivel >= this.steamBoiler.getNormalLimitMin()) {
          this.pumpScheduleQueue.add(1, true);
          this.pumpScheduleQueue.add(2, true);
          this.pumpScheduleQueue.add(3, true);
          this.pumpScheduleQueue.add(4, false);
          this.steamBoiler.setOperationMode(BoilerOperationMode.NORMAL);
          this.log("     * passando do modo de inicialização para o modo normal.");
        } else {
          this.log("     * continua no modo de inicialização.");
        }
        break;
      case NORMAL:
        if (this.nivel < this.steamBoiler.getNormalLimitMin()) {
          // abaixo
          this.pumpScheduleQueue.add(1, true);
          this.pumpScheduleQueue.add(2, true);
          this.pumpScheduleQueue.add(3, true);
          this.pumpScheduleQueue.add(4, true);
          this.log("     * modo normal [abaixo] (operar com todas as bombas).");
        } else if (this.nivel > this.steamBoiler.getNormalLimitMax()) {
          // acima
          this.pumpScheduleQueue.add(1, true);
          this.pumpScheduleQueue.add(2, false);
          this.pumpScheduleQueue.add(3, false);
          this.pumpScheduleQueue.add(4, false);
          this.log("     * modo normal [acima] (operar com uma bomba).");
        } else {
          // normal
          this.pumpScheduleQueue.add(1, true);
          this.pumpScheduleQueue.add(2, true);
          this.pumpScheduleQueue.add(3, true);
          this.pumpScheduleQueue.add(4, false);
          this.log("     * modo normal [normal] (operar com 3 as bombas).");
        }
        if (this.countPumpsON() != this.countPumpsEnabled()) {
          this.steamBoiler.setOperationMode(BoilerOperationMode.DEGRADED);
          this.log("     * passando do modo normal para degradado.");
        }
        break;
      case DEGRADED:
        if (this.countAllPumpsProblem() == this.numberOfPumps) {
          this.steamBoiler.setOperationMode(BoilerOperationMode.EMERGENCY_STOP);
          this.log("     * passando do modo degradado para o modo de emergência.");
        } else if (this.countAllPumpsProblem() == 0) {
          this.steamBoiler.setOperationMode(BoilerOperationMode.NORMAL);
          this.log("     * passando do modo degradado para o modo normal.");
        }
        if (this.nivel <= this.steamBoiler.getLimitMin()) {
          this.log("     * passando do modo degradado para o modo de emergência.");
        } else if (this.nivel >= this.steamBoiler.getLimitMax()) {
          this.log("     * passando do modo degradado para o modo de emergência.");
        } else {
          if (this.nivel < this.steamBoiler.getNormalLimitMin()) {
            this.pumpScheduleQueue.add(1, true);
            this.pumpScheduleQueue.add(2, true);
            this.pumpScheduleQueue.add(3, true);
            this.pumpScheduleQueue.add(4, true);
          } if (this.nivel > this.steamBoiler.getNormalLimitMax()) {
            this.pumpScheduleQueue.add(1, true);
            this.pumpScheduleQueue.add(2, true);
            this.pumpScheduleQueue.add(3, false);
            this.pumpScheduleQueue.add(4, false);
          } else {
            this.pumpScheduleQueue.add(1, true);
            this.pumpScheduleQueue.add(2, true);
            this.pumpScheduleQueue.add(3, true);
            this.pumpScheduleQueue.add(4, false);
          }
        }
        break;
      case EMERGENCY_STOP:
        this.log("     * parada de emergência.");
        this.steamBoiler.setOFF();
        this.pumpScheduleQueue.add(1, false);
        this.pumpScheduleQueue.add(2, false);
        this.pumpScheduleQueue.add(3, false);
        this.pumpScheduleQueue.add(4, false);
        this.log("     * caldeira desligada.");
        System.exit(1);
        break;
    }
  }

  private int countPumpsEnabled()
  {
    int i = 0;
    for (Pump pump : this.steamBoiler.getPumps()) {
      if (pump.getStatus() && pump.getFuncionalSensor().getValue()) {
        i++;
      }
    }
    return i;
  }

  private int countAllPumpsProblem()
  {
    int i = 0;
    for (Pump pump : this.steamBoiler.getPumps()) {
      if (!pump.getFuncionalSensor().getValue()) {
        i++;
      }
    }
    return i;
  }

  private int countPumpsON()
  {
    int i = 0;
    for (Pump pump : this.steamBoiler.getPumps()) {
      if (pump.getStatus()) {
        i++;
      }
    }
    return i;
  }

  private int countPumpsOFF()
  {
    int i = 0;
    for (Pump pump : this.steamBoiler.getPumps()) {
      if (!pump.getStatus()) {
        i++;
      }
    }
    return i;
  }

}
