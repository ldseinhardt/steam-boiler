package steamboilerctrl.object;

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

  public void setON()
  {
    this.status = true;
  }

  public void setOFF()
  {
    this.status = false;
  }

  public void setWorking(boolean status)
  {
    this.working = status;
  }

  public void execute()
  {
    // caso o sensor esteja com problema, nada é executado
    if (this.status && this.working) {
      this.action();
    }
  }

  abstract protected void action();

  public boolean getStatus()
  {
    return this.status;
  }

  public boolean isWorking()
  {
    return this.working;
  }

}
