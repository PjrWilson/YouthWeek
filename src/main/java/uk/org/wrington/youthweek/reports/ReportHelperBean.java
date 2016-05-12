/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.model.util.DateHelper;
import uk.org.wrington.youthweek.model.util.YwDayBean;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "reportHelperBean")
@SessionScoped
public class ReportHelperBean implements Serializable {

  @ManagedProperty(value = "#{ywDayBean}")
  private YwDayBean ywDayBean;
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

  public void setYwDayBean(YwDayBean ywDayBean) {
    this.ywDayBean = ywDayBean;
  }
  
  public List<Integer> getDays() {
    return Arrays.asList(1, 2, 3, 4, 5);
  }

  public String getDayName(int dayNumber) {
    return ywDayBean.getDayName(dayNumber);
  }

  public String getDayDate(int dayNumber) {
    return ywDayBean.getDayDate(dayNumber);
  }

  public String getDayNameAndDate(int dayNumber) {
    return ywDayBean.getDayNameAndDate(dayNumber);
  }

  public String getYear() {
    return ywDayBean.getYear();
  }

  public String getReportTitlePrefix() {
    return "Youth Week " + getYear();
  }
}
