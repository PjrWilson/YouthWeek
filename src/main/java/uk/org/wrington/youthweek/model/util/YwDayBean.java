/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "ywDayBean")
@SessionScoped
public class YwDayBean implements Serializable {

  @ManagedProperty(value = "#{settings}")
  private Settings settings;

  /**
   * Creates a new instance of YwDayBean
   */
  public YwDayBean() {
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  public List<Integer> getDays() {
    return Arrays.asList(1, 2, 3, 4, 5);
  }

  public String getDayName(int dayNumber) {
    return StaticValues.getDayLabel(dayNumber);
  }

  public String getDayDate(int dayNumber) {
    if (settings != null) {
      Calendar c
              = DateHelper.getInstance().getCalendar(settings.getStartDate());
      c.add(Calendar.DAY_OF_YEAR, dayNumber - 1);
      return DateHelper.getInstance().formatDate("dd-MMM", c);
    }
    return "";
  }

  public String getDayNameAndDate(int dayNumber) {
    return StaticValues.getDayLabel(dayNumber) + " " + getDayDate(dayNumber);
  }

  public String getYear() {
    if (settings != null) {
      return DateHelper.getInstance().formatDate("YYYY", settings.getStartDate());
    }
    return "";
  }

}
