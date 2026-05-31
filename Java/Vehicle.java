import java.time.LocalDateTime;

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

    public int getTollFee(LocalDateTime ldt) {
        return this.type.getTollFee(ldt);
    }

}
