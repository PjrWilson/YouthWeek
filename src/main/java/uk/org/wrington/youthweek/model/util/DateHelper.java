/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author phil
 */
public class DateHelper {

  private static DateHelper mInstance = null;

  private Calendar mBaseCalendar = null;
  //private SimpleDateFormat mDateFormatter = null;
  
  public static DateHelper getInstance() {
    if (mInstance == null) {
      mInstance = new DateHelper();
    }
    return mInstance;
  }
  
  private DateHelper() {
    mBaseCalendar = new GregorianCalendar(
            TimeZone.getTimeZone("Europe/London"));
  }
  
  public Calendar getCalendar() {
    return (Calendar) mBaseCalendar.clone();
  }
  
  public String formatDate(String format, Calendar date) {
    return formatDate(format, date.getTime());
  } 

  public String formatDate(String format, Date date) {
    SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
    dateFormatter.setCalendar(mBaseCalendar);
    return dateFormatter.format(date);
  } 
  
  public Calendar getCalendar(Date startDate) {
    Calendar c = getCalendar();
    c.setTime(startDate);
    return c;
  }
}
