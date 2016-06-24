package steamboilerctrl;

import steamboilerctrl.object.SteamBoiler;
import steamboilerctrl.object.Pump;
import steamboilerctrl.task.PhysicalTask;
import steamboilerctrl.task.MainTaskControl;
import steamboilerctrl.task.PumpTaskControl;
import steamboilerctrl.task.FailTask;
import steamboilerctrl.task.util.PumpScheduleQueue;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;

public class Main
{
  // Probabilidade de erros (70%)
  private static double bug = 0.7;

  // Capacidade da caldeira em litros
  private static double boiler_capacity = 100;

  // Capacidade de produção de vapor (constante) em litros/segundo
  private static double boiler_steam = 1.5;

  // Vazão de cada bomba em litros/segundo
  private static double pump_waterFlow = 0.75;

  private SteamBoiler steamBoiler;

  public Main()
  {
    this.steamBoiler = new SteamBoiler(
      boiler_capacity,
      boiler_steam,
      pump_waterFlow
    );

    PhysicalTask physicalTask = new PhysicalTask(
      this.steamBoiler,
      new PriorityParameters(1),
      new PeriodicParameters(
        null,
        new RelativeTime(1000, 0),
        null,
        null,
        null,
        null
      )
    );

    System.out.println(" * inicializando caldeira ...");

    for (Pump pump : this.steamBoiler.getPumps()) {
      pump.setON();
      System.out.println(" - bomba" + pump.getId() + " ligada.");
    }

    System.out.println("");

    this.steamBoiler.setON();
    System.out.println(" - fogo ligado.");

    physicalTask.start();

    PumpScheduleQueue pumpScheduleQueue = new PumpScheduleQueue();

    MainTaskControl mainTaskControl = new MainTaskControl(
      this.steamBoiler,
      pumpScheduleQueue,
      new PriorityParameters(2),
      new PeriodicParameters(
        null,
        new RelativeTime(5000, 0),
        null,
        null,
        null,
        null
      )
    );

    System.out.println("");
    System.out.println(" * inicializando controle principal ...");
    mainTaskControl.start();

    PumpTaskControl pumpTaskControls[] = new PumpTaskControl[this.steamBoiler.getNumberOfPumps()];
    int i = 0;
    for (Pump pump : this.steamBoiler.getPumps()) {
      PumpTaskControl pumpTaskControl = pumpTaskControls[pump.getId() - 1];
      pumpTaskControl = new PumpTaskControl(
        pump,
        pumpScheduleQueue,
        new PriorityParameters(3 + i),
        new PeriodicParameters(
          null,
          new RelativeTime(5000, 0),
          null,
          null,
          null,
          null
        )
      );
      System.out.println("");
      System.out.println(" * inicializando controle da bomba" + pump.getId() + " ...");
      pumpTaskControl.start();
      i++;
    }

    FailTask failTask = new FailTask(
      this.steamBoiler,
      bug,
      new PriorityParameters(3 + i),
      new PeriodicParameters(
        null,
        new RelativeTime(10000, 0),
        null,
        null,
        null,
        null
      )
    );
    failTask.start();
  }

  public static void main(String[] args)
  {
    switch (args.length) {
      case 0:
        printConfigs(false);
        break;
      case 1:
        if (args[0].toLowerCase().contains("help")) {
          help();
        } else {
          try {
            bug = Double.parseDouble(args[0]);
            printConfigs(true);
          } catch(Exception e) {
            invalidArgs();
          }
        }
        break;
      case 4:
        try {
          bug = Double.parseDouble(args[0]);
          boiler_capacity = Double.parseDouble(args[1]);
          boiler_steam = Double.parseDouble(args[2]);
          pump_waterFlow = Double.parseDouble(args[3]);
          printConfigs(true);
        } catch(Exception e) {
          invalidArgs();
        }
        break;
      default:
        invalidArgs();
    }
    new Main();
  }

  private static void help()
  {
    System.out.println("Ajuda: ");
    System.out.println("");
    System.out.println("Passagem de parâmetros possiveis: ");
    System.out.println("1 ) bug");
    System.out.println("2 ) bug capacity steam waterFlow");
    System.out.println("");
    System.out.println("bug: probabilidade de falhas ocorrerem (double: [0-1]).");
    System.out.println("capacity: capacidade de água da caldeira em litros (double).");
    System.out.println("steam: capacidade de produção de vapor da caldeira em litros/segundo (double).");
    System.out.println("waterFlow: vazão de água de cada bomba em litros/segundo (double).");
    System.exit(0);
  }

  private static void invalidArgs()
  {
    System.out.println("Parâmetro(s) invalido(s)");
    System.exit(1);
  }

  private static void printConfigs(boolean status)
  {
    if (status) {
      System.out.println("SteamBoilerCtrl: configuração custom");
    } else {
      System.out.println("SteamBoilerCtrl: configuração padrão");
    }
    System.out.println(" * Probabilidade de erros: " + bug);
    System.out.println(" * Capacidade da água da caldeira: " + boiler_capacity + " litros");
    System.out.println(" * Produção de vapor da caldeira: " + boiler_steam + " litros/segundo");
    System.out.println(" * Vazão de água das bombas: " + pump_waterFlow + " litros/segundo");
    System.out.println("");
    System.out.println("");
  }

}
