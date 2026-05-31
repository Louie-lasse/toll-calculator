
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.util.*;

public class TollCalculator {

    /**
     * Calculate the total toll fee for one day
     *
     * @param vehicle - the vehicle
     * @param dates   - date and time of all passes on one day
     * @return - the total toll fee for that day
     */
    public int getTollFee(Vehicle vehicle, Date... dates) {
        if (dates.length == 0) {
            return 0;
        }
        if (vehicle.isTollFree()) {
            return 0;
        }
        List<LocalDateTime> dateTimes = new ArrayList<>(dates.length);
        for (Date date : dates) {
            dateTimes.add(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        LocalDateTime intervalStart = dateTimes.get(0);
        if (isTollFreeDate(intervalStart)) {
            return 0;
        }
        int intervalToll = 0;

        int total = 0;
        for (LocalDateTime ldt : dateTimes) {
            if (Duration.between(ldt, intervalStart).compareTo(Duration.ofHours(1)) < 0) {
                intervalToll = Math.max(intervalToll, vehicle.getTollFee(ldt));
                continue;
            }
            intervalStart = ldt;
            total += intervalToll;
            intervalToll = vehicle.getTollFee(ldt);
        }
        total += intervalToll;
        return total;
    }

    private Boolean isTollFreeDate(LocalDateTime ldt) {
        DayOfWeek dayOfWeek = ldt.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        MonthDay monthDay = MonthDay.from(ldt);
        return TollCalendar.getInstance().isTollFree(monthDay);
        // TODO: verify that this should be classed as a bug and removed
        // int year = calendar.get(Calendar.YEAR);
        // if (year != 2013) {
        // return false;
        // }
    }
}
