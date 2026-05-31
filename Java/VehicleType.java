public enum VehicleType {

  MOTORBIKE("Motorbike"),
  TRACTOR("Tractor"),
  EMERGENCY("Emergency"),
  DIPLOMAT("Diplomat"),
  FOREIGN("Foreign"),
  CAR("Car", false),
  MILITARY("Military");

  private final String name;
  private boolean tollFree;

  public boolean isTollFree() {
    return tollFree;
  }

  VehicleType(String name, boolean tollFree) {
    this.name = name;
    this.tollFree = tollFree;
  }

  VehicleType(String name) {
    this(name, true);
  }

  public String getName() {
    return name;
  }
}
