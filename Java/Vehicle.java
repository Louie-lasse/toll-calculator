
public class Vehicle {

  private final VehicleType type;

  public Vehicle(VehicleType type) {
    this.type = type;
  }

  public String getType() {
    return type.getName();
  }

  public boolean isTollFree() {
    return this.type.isTollFree();
  }

}
