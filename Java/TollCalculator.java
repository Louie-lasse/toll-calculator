
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

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
    LocalDateTime intervalStart = dates[0].toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    int intervalToll = 0;

    int total = 0;
    for (Date date : dates) {
      LocalDateTime ldt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
      if (Duration.between(ldt, intervalStart).compareTo(Duration.ofHours(1)) < 0) {
        intervalToll = Math.max(intervalToll, getTollFee(ldt));
        continue;
      }
      intervalStart = ldt;
      total += intervalToll;
      intervalToll = getTollFee(ldt);
    }
    total += intervalToll;
    return total;
  }

  public int getTollFee(final LocalDateTime ldt) {
    if (isTollFreeDate(ldt))
      return 0;
    int hour = ldt.getHour();
    int minute = ldt.getMinute();

    if (hour == 6)
      return minute < 30 ? 8 : 13;
    if (hour == 7)
      return 18;
    if (hour == 8)
      return minute < 30 ? 13 : 8;
    if (hour == 15)
      return minute < 30 ? 13 : 18;
    if (hour == 16)
      return 18;
    if (hour == 17)
      return 13;
    if (hour == 18 && minute < 30)
      return 8;
    return 0;
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
