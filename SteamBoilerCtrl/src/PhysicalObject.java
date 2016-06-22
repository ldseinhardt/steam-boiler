abstract public class PhysicalObject
{

  // Objeto físico operando ligado/desligado
  protected boolean status;

  // Objeto físico funcionando corretamente
  protected boolean working;

  public PhysicalObject()
  {
    this.status = false;
    this.working = true;
  }

  public PhysicalObject(boolean status)
  {
    this.status = status;
    this.working = true;
  }

  public PhysicalObject setON()
  {
    this.status = true;
    return this;
  }

  public PhysicalObject setOFF()
  {
    this.status = false;
    return this;
  }

  public PhysicalObject setWorking(boolean status)
  {
    this.working = status;
    return this;
  }

  public boolean getStatus()
  {
    return this.status;
  }

  public boolean isWorking()
  {
    return this.working;
  }

  abstract protected PhysicalObject action();

  public PhysicalObject execute() {
    // caso o sensor esteja com problema, nada é executado
    return this.status && this.working
      ? this.action()
      : this
      ;
  }

}
