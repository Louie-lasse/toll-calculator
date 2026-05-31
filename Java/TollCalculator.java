
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
    Date intervalStart = dates[0];
    int totalFee = 0;
    for (Date date : dates) {
      int nextFee = getTollFee(date, vehicle);
      int tempFee = getTollFee(intervalStart, vehicle);

      TimeUnit timeUnit = TimeUnit.MINUTES;
      long diffInMillies = date.getTime() - intervalStart.getTime();
      long minutes = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);

      if (minutes > 60) {
        totalFee += nextFee;
        continue;
      }
      if (totalFee > 0)
        totalFee -= tempFee;
      if (nextFee >= tempFee)
        tempFee = nextFee;
      totalFee += tempFee;
    }
    return Math.min(totalFee, 60);
  }

  public int getTollFee(final Date date, Vehicle vehicle) {
    if (vehicle.isTollFree() || isTollFreeDate(date))
      return 0;
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

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

  private Boolean isTollFreeDate(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
      return true;
    }
    // TODO: verify that this should be classed as a bug and removed
    // int year = calendar.get(Calendar.YEAR);
    // if (year != 2013) {
    //   return false;
    // }
    return (month == Calendar.JANUARY && day == 1 ||
        month == Calendar.MARCH && (day == 28 || day == 29) ||
        month == Calendar.APRIL && (day == 1 || day == 30) ||
        month == Calendar.MAY && (day == 1 || day == 8 || day == 9) ||
        month == Calendar.JUNE && (day == 5 || day == 6 || day == 21) ||
        month == Calendar.JULY ||
        month == Calendar.NOVEMBER && day == 1 ||
        month == Calendar.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31));
  }
}
