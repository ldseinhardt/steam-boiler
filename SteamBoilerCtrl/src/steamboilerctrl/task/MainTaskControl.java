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

  // última medição do nível de água
  private double old;

  // última diferença no nível de água
  private double olddif;

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
    this.old = 0;
    this.olddif = 0;

    // Não exibe mensagens de log da tarefa
    //this.setLog(false);
  }

  private void waterMeasureControl()
  {
    // !!! Medição real
    //this.nivel = (double) this.steamBoiler.getNivel();

    //*
    this.old = this.nivel;
    this.nivel = (double) this.waterSensor.getValue();

    this.log("     * Medição de água: " + this.old + " -> " + this.nivel);

    double max = ((this.waterFlow * this.numberOfPumps) - (this.steamProduce)) * 5;

    if (this.old != 0 && (this.nivel > this.old + max || this.nivel < this.old - max)) {
      this.log("     * Correção na medição de água: " + this.nivel + " -> " + this.old);
      this.nivel = this.old + this.olddif;
      if (this.steamBoiler.getOperationMode() != BoilerOperationMode.RESCUE && this.steamBoiler.getOperationMode() != BoilerOperationMode.EMERGENCY_STOP) {
        this.log("     * passando do modo " + this.steamBoiler.getOperationMode() + " para o modo RESCUE.");
        this.steamBoiler.setOperationMode(BoilerOperationMode.RESCUE);
      }
    } else {
      this.olddif = this.nivel - this.old;
      if (this.steamBoiler.getOperationMode() == BoilerOperationMode.RESCUE) {
        this.log("     * passando do modo RESCUE para o modo DEGRADED.");
        this.steamBoiler.setOperationMode(BoilerOperationMode.DEGRADED);
      }
    }
    //*/
  }

  private void steamControl()
  {
    if (this.steamProduce != this.steamSensor.getValue()) {
      this.log("     * risco de explosão, passando do modo " + this.steamBoiler.getOperationMode() + " para o modo EMERGENCY_STOP.");
      this.steamBoiler.setOperationMode(BoilerOperationMode.EMERGENCY_STOP);
    }
  }

  private void pumpsControl()
  {
    int problem = this.countPumpsON() - this.countPumpsEnabled();
    if (problem == 0) {
      if(this.steamBoiler.getOperationMode() == BoilerOperationMode.DEGRADED) {
        this.log("     * bomba(s) com falha(s): " + problem + ", passando do modo DEGRADED para o modo NORMAL.");
        this.steamBoiler.setOperationMode(BoilerOperationMode.NORMAL);
      }
    } else if(this.steamBoiler.getOperationMode() != BoilerOperationMode.RESCUE && this.steamBoiler.getOperationMode() != BoilerOperationMode.DEGRADED) {
      this.log("     * bomba(s) com falha(s): " + problem + ", passando do modo " + this.steamBoiler.getOperationMode() + " para o modo DEGRADED.");
      this.steamBoiler.setOperationMode(BoilerOperationMode.DEGRADED);
    }
  }

  protected void execute()
  {
    this.log("\n - [Controle principal]:");

    this.waterMeasureControl();

    switch (this.steamBoiler.getOperationMode()) {
      case INITIALIZATION:
          this.steamControl();
          this.pumpsControl();
          if (this.nivel >= this.steamBoiler.getNormalLimitMin()) {
            this.log("     * passando do modo de INITIALIZATION para o modo NORMAL.");
            this.steamBoiler.setOperationMode(BoilerOperationMode.NORMAL);
          } else {
            this.log("     * continuar no modo INITIALIZATION.");
          }
        break;
      case NORMAL:
          this.steamControl();
          this.pumpsControl();
          if (this.nivel < this.steamBoiler.getNormalLimitMin()) {
            // abaixo
            this.pumpsSchedule(1);
            this.log("     * continuar no modo NORMAL (abaixo) (operar com todoas as bombas).");
          } else if (this.nivel > this.steamBoiler.getNormalLimitMax()) {
            // acima
            this.pumpsSchedule(-1);
            this.log("     * continuar no modo NORMAL (acima) (operar com nenhuma bomba).");
          } else {
            // normal
            this.pumpsSchedule(0);
            this.log("     * continuar no modo NORMAL (manter nível) (operar com as bombas necessárias).");
          }
        break;
      case DEGRADED:
          this.steamControl();
          this.pumpsControl();
          if (this.nivel <= this.steamBoiler.getLimitMin()) {
            this.log("     * passando do modo DEGRADED para o modo de EMERGENCY_STOP.");
            this.steamBoiler.setOperationMode(BoilerOperationMode.EMERGENCY_STOP);
          } else if (this.nivel >= this.steamBoiler.getLimitMax()) {
            this.log("     * passando do modo DEGRADED para o modo de EMERGENCY_STOP.");
            this.steamBoiler.setOperationMode(BoilerOperationMode.EMERGENCY_STOP);
          } else if (this.nivel < this.steamBoiler.getNormalLimitMin()) {
            // abaixo
            this.pumpsSchedule(1);
            this.log("     * continuar no modo DEGRADED (abaixo) (operar com todoas as bombas).");
          } else if (this.nivel > this.steamBoiler.getNormalLimitMax()) {
            // acima
            this.pumpsSchedule(-1);
            this.log("     * continuar no modo DEGRADED (acima) (operar com nenhuma bomba).");
          } else {
            // normal
            this.pumpsSchedule(0);
            this.log("     * continuar no modo DEGRADED (manter nível) (operar com as bombas necessárias).");
          }
        break;
      //*
        case RESCUE:
            this.steamControl();
            this.pumpsControl();
            if (this.nivel <= this.steamBoiler.getLimitMin()) {
              this.log("     * passando do modo RESCUE para o modo de EMERGENCY_STOP.");
              this.steamBoiler.setOperationMode(BoilerOperationMode.EMERGENCY_STOP);
            } else if (this.nivel >= this.steamBoiler.getLimitMax()) {
              this.log("     * passando do modo RESCUE para o modo de EMERGENCY_STOP.");
              this.steamBoiler.setOperationMode(BoilerOperationMode.EMERGENCY_STOP);
            } else if (this.nivel < this.steamBoiler.getNormalLimitMin()) {
              // abaixo
              this.pumpsSchedule(1);
              this.log("     * continuar no modo RESCUE (abaixo) (operar com todoas as bombas).");
            } else if (this.nivel > this.steamBoiler.getNormalLimitMax()) {
              // acima
              this.pumpsSchedule(-1);
              this.log("     * continuar no modo RESCUE (acima) (operar com nenhuma bomba).");
            } else {
              // normal
              this.pumpsSchedule(0);
              this.log("     * continuar no modo RESCUE (manter nível) (operar com as bombas necessárias).");
            }
        break;
      //*/
      case EMERGENCY_STOP:
          this.log("     * parada de emergência.\n     * caldeira desligada.\n     * bombas desligadas.");
          this.steamBoiler.setOFF();
          for (int i = 0; i < this.numberOfPumps; i++) {
            this.steamBoiler.getPumps()[i].setOFF();
          }
          System.exit(1);
        break;
    }
  }

  // -1 = nenhuma bomba
  //  0 =  bombas necessárias
  //  1 = todas as bombas
  private void pumpsSchedule(int status)
  {
    int diff = this.pumpsScheduleNumber(status) - this.countPumpsEnabled();

    /*
    for (int i = 0; i < this.numberOfPumps; i++) {
      if (!this.steamBoiler.getPumps()[i].getFuncionalSensor().getValue()) {
        this.pumpScheduleQueue.add(i +1, false);
      }
    }
    */

    if (diff > 0) { //+
      for (int i = 0; i < this.numberOfPumps; i++) {
        if (diff <= 0) {
          break;
        }
        if (this.steamBoiler.getPumps()[i].getStatus()) {
          continue;
        } else if (this.steamBoiler.getPumps()[i].getFuncionalSensor().getValue()) {
          this.pumpScheduleQueue.add(i + 1, true);
          diff--;
        }
      }
    } else if (diff < 0) { //-
      diff = this.numberOfPumps - Math.abs(diff);
      for (int i = 0; i < this.numberOfPumps; i++) {
        if (diff > 0 && this.steamBoiler.getPumps()[i].getStatus() && this.steamBoiler.getPumps()[i].getFuncionalSensor().getValue()) {
          diff--;
          continue;
        }
        this.pumpScheduleQueue.add(i + 1, false);
      }
    }
  }

  private int pumpsScheduleNumber(int status)
  {
    if (status < 0) {
      return 0;
    } else if (status > 0) {
      return this.numberOfPumps;
    }
    int n = 1;
    for (int i = 0; i < this.numberOfPumps; i++) {
      if ((this.waterFlow * n) < this.steamProduce) {
        n++;
      } else {
        break;
      }
    }
    return n;
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

}
