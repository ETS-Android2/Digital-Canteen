package com.example.canteenapp.Util;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CanteenUtil {


  public static String ConvertMilliSecondsToFormattedDate(long milliSeconds) {
    String dateFormat = "dd-MM-yyyy hh:mm";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    return simpleDateFormat.format(calendar.getTime());
  }

  public static String ConvertMilliSecondsToPrettyTime(long milliSeconds) {
    PrettyTime p = new PrettyTime();
    return p.format(new Date(milliSeconds));
  }
}
