package com.example.canteenapp.Util;

import static com.example.canteenapp.constant.EmailConstant.FROM;
import static com.example.canteenapp.constant.EmailConstant.GOOGLE_SMTP_PORT;
import static com.example.canteenapp.constant.EmailConstant.GOOGLE_SMTP_URL;
import static com.example.canteenapp.constant.EmailConstant.PASSWORD;
import static com.example.canteenapp.constant.EmailConstant.USERNAME;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.ocpsoft.prettytime.PrettyTime;

import java.net.InetAddress;
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

  public static boolean isInternetAvailable() {
    try {
      InetAddress ipAddr = InetAddress.getByName("google.com");
      return !ipAddr.equals("");

    } catch (Exception e) {
      return false;
    }
  }
  public static int getYearFromMilliSecond(long milliSeconds) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(milliSeconds);
    return c.get(Calendar.YEAR);
  }
  public static  String getYearAndDayFromMilliSecond(long milliSeconds) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(milliSeconds);
    return c.get(Calendar.YEAR)+" "+c.get(Calendar.DAY_OF_YEAR);
  }


  public static String ConvertMilliSecondsToPrettyTime(long milliSeconds) {
    PrettyTime p = new PrettyTime();
    return p.format(new Date(milliSeconds));
  }


  public static void sendMail(String message, String TO, String subject) {
    try {
      Email email = new SimpleEmail();
      email.setHostName(GOOGLE_SMTP_URL);
      email.setSmtpPort(GOOGLE_SMTP_PORT);
      email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
      email.setSSLOnConnect(true);
      email.setFrom(FROM);
      email.setSubject(subject);
      email.setMsg(message);
      email.addTo(TO);
      email.send();
    } catch (Exception e) {
      System.out.println(e);
    }

  }
}
