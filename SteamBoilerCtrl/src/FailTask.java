import java.util.Random;
import javax.realtime.*;

public class FailTask extends Task
{

  private SteamBoiler steamBoiler;

  private Random random;

  private double rate;

  final private int LIMIT = 1000;

  public FailTask(PhysicalObject physicalObject, double rate,
    SchedulingParameters scheduling, ReleaseParameters release)
  {
    super(physicalObject, scheduling, release);
    this.steamBoiler = (SteamBoiler) physicalObject;
    this.rate = LIMIT - (rate * LIMIT);
    this.random = new Random(System.currentTimeMillis());
  }

  protected FailTask execute()
  {
    int sorted = this.random.nextInt(LIMIT);

    if (sorted > rate) {
      PhysicalObject physicalObject = null;
      Sensor sensor = null;
      String name = "";

      switch (this.random.nextInt(3)) {
        case 0: // sensor de nível de agua
            sensor = steamBoiler.getWaterSensor();
            name = "Nível de água";
          break;
        case 1: // sensor de vapor
            sensor = steamBoiler.getSteamSensor();
            name = "Produção de vapor";
          break;
        case 2: // sensor de alguma bomba
            int pump = this.random.nextInt(steamBoiler.getNumberOfPumps());
            //sensor = steamBoiler.getPumps()[pump].getFuncionalSensor();
            //name = "Status da bomba " + (pump + 1);
            physicalObject = steamBoiler.getPumps()[pump];
            name = "Bomba " + (pump + 1);
          break;
      }

      boolean status = this.random.nextBoolean();

      if (sensor != null) {
        if (sensor.isWorking() == status) {
          System.out.println(" - [Simulação de falha]: (Sensor: " + name + ") Você acertou seu chute no mesmo local do hardware haha !!!");
        } else if (status) {
          System.out.println(" - [Simulação de falha]: (Sensor: " + name + ") Seu chute fez o sensor voltar de funcionar : )");
        } else {
          System.out.println(" - [Simulação de falha]: (Sensor: " + name + ") Seu chute fez o sensor parar de funcionar : (");
        }
        sensor.setWorking(status);
      }

      if (physicalObject != null) {
        if (physicalObject.isWorking() == status) {
          System.out.println(" - [Simulação de falha]: (Objeto da planta: " + name + ") Você acertou seu chute no mesmo local do hardware haha !!!");
        } else if (status) {
          System.out.println(" - [Simulação de falha]: (Objeto da planta: " + name + ") Seu chute fez o objeto voltar de funcionar : )");
        } else {
          System.out.println(" - [Simulação de falha]: (Objeto da planta: " + name + ") Seu chute fez o objeto parar de funcionar : (");
        }
        physicalObject.setWorking(status);
      }
    } else {
      System.out.println(" - [Simulação de falha]: Você errou seu chute no hardware haha !!!");
    }

    return this;
  }

}
