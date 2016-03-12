/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.joda.time.LocalDate;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "reportHelperBean")
@SessionScoped
public class ReportHelperBean implements Serializable {

  @ManagedProperty(value = "#{settings}")
  private Settings settings;

  /**
   * Creates a new instance of ReportHelperBean
   */
  public ReportHelperBean() {
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
      LocalDate ld = new LocalDate(settings.getStartDate().getTime());
      // Add day - 1.
//      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
      return format.format(ld.plusDays(dayNumber - 1).toDate());
    }
    return "";
  }

  public String getDayNameAndDate(int dayNumber) {
    return StaticValues.getDayLabel(dayNumber) + " " + getDayDate(dayNumber);
  }

  public String getYear() {
    if (settings != null) {
      SimpleDateFormat format = new SimpleDateFormat("YYYY");
      return format.format(settings.getStartDate());
    }
    return "";
  }
  
  public String getReportTitlePrefix() {
    return "Youth Week " + getYear();
  }
}
