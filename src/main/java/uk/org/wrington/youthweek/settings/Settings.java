/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.settings;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.joda.time.LocalDate;
import uk.org.wrington.youthweek.controller.SettingsController;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.SettingEntry;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean
@ApplicationScoped
public class Settings implements Serializable {
  
  LocalDate startDate = null;
  Float adminFee = 0f;
  Boolean disableDelete = null;
  String reportMessageHeader = null;
  String reportMessage = null;
  String ticketHeader = null;
  String ticketMessage = null;
  Boolean showOrganiserEmail = null;
  Map<String, String> dayColoursDefault = new HashMap<>();
  Map<String, String> dayColours = new HashMap<>();
  
  @ManagedProperty(value = "#{settingsController}")
  private SettingsController settingsController;
  @EJB
  private uk.org.wrington.youthweek.model.ExtraItemEntryFacade ejbExtraItemFacade;
  @EJB
  private uk.org.wrington.youthweek.model.ActivityEntryFacade ejbActivityFacade;
  
  @PostConstruct
  public void init() {
    dayColoursDefault.put("Monday", "add8e6");
    dayColoursDefault.put("Tuesday", "ffff00");
    dayColoursDefault.put("Wednesday", "ff0000");
    dayColoursDefault.put("Thursday", "00ff00");
    dayColoursDefault.put("Friday", "ffc0cb");
  }
  
  public void setSettingsController(SettingsController sc) {
    this.settingsController = sc;
  }
  
  public Date getStartDate() {
    
    if (startDate == null) {
      SettingEntry s = settingsController.getSetting("StartDate");
      if (s != null) {
        startDate = new LocalDate(Long.parseLong(s.getValue()));
      }
    }
    if (startDate != null) {
      return startDate.toDate();
    }
    return null;
  }
  
  public void setStartDate(Date startDate, boolean clearPreviousChoices) {
    this.startDate = LocalDate.fromDateFields(startDate);
    System.out.println("START DATE = " + this.startDate.toString());
    SettingEntry s = new SettingEntry();
    s.setKey("StartDate");
    s.setValue(String.valueOf(startDate.getTime()));
    settingsController.create(s);
    
    if (clearPreviousChoices) {
      ejbActivityFacade.deleteAll();
      ejbExtraItemFacade.deleteAll();
    }
  }  
  
  public Float getAdminFee() {
    if (adminFee == 0) {
      SettingEntry s = settingsController.getSetting("AdminFee");
      if (s != null) {
        adminFee = Float.parseFloat(s.getValue());
      }
    }
    return adminFee;
  }
  
  public void setAdminFee(Float adminFee) {
    this.adminFee = adminFee;
    System.out.println("ADMIN FEE = " + this.adminFee);
    SettingEntry s = new SettingEntry();
    s.setKey("AdminFee");
    s.setValue(adminFee.toString());
    settingsController.create(s);
  }

  // Calculate a year for the child
  public Integer getSchoolYearFor(Child c, Integer dayNo) {
    // Get the start date.
    LocalDate ywStart = new LocalDate(startDate);
    ywStart.plusDays(dayNo - 1);
    LocalDate dob = new LocalDate(c.getDateOfBirth());
    // Get the age.
    int age = ywStart.year().get() - dob.year().get();
    // School year is age - 4. If the dob is before september, add 1 to year.
    int schoolYear = age - 6;
    if (dob.getMonthOfYear() < 9) {
      ++schoolYear;
    }
    return schoolYear;
  }
  
  public Boolean getDisableDelete() {
    if (disableDelete == null) {
      SettingEntry s = settingsController.getSetting("DisableDelete");
      if (s != null) {
        disableDelete = Boolean.parseBoolean(s.getValue());
      } else {
        disableDelete = false;
      }
    }
    return disableDelete;
  }
  
  public void setDisableDelete(Boolean disableDelete) {
    this.disableDelete = disableDelete;
    System.out.println("HIDE DELETE = " + this.disableDelete);
    SettingEntry s = new SettingEntry();
    s.setKey("DisableDelete");
    s.setValue(disableDelete.toString());
    settingsController.create(s);
  }
  
  public String getReportMessageHeader() {
    if (reportMessageHeader == null) {
      SettingEntry s = settingsController.getSetting("ReportMessageHeader");
      if (s != null) {
        reportMessageHeader = s.getValue();
      }
    }
    return reportMessageHeader;
  }
  
  public void setReportMessageHeader(String msgHeader) {
    this.reportMessageHeader = msgHeader;
    SettingEntry s = new SettingEntry();
    s.setKey("ReportMessageHeader");
    s.setValue(msgHeader);
    settingsController.create(s);
  }
  
  public String getReportMessage() {
    if (reportMessage == null) {
      SettingEntry s = settingsController.getSetting("ReportMessage");
      if (s != null) {
        reportMessage = s.getValue();
      }
    }
    return reportMessage;
  }
  
  public void setReportMessage(String msg) {
    this.reportMessage = msg;
    SettingEntry s = new SettingEntry();
    s.setKey("ReportMessage");
    s.setValue(msg);
    settingsController.create(s);
  }
  
  public String getTicketHeader() {
    if (ticketHeader == null) {
      SettingEntry s = settingsController.getSetting("TicketHeader");
      if (s != null) {
        ticketHeader = s.getValue();
      }
    }
    return ticketHeader;
  }
  
  public void setTicketHeader(String msgHeader) {
    this.ticketHeader = msgHeader;
    SettingEntry s = new SettingEntry();
    s.setKey("TicketHeader");
    s.setValue(msgHeader);
    settingsController.create(s);
  }
  
  public String getTicketMessage() {
    if (ticketMessage == null) {
      SettingEntry s = settingsController.getSetting("TicketMessage");
      if (s != null) {
        ticketMessage = s.getValue();
      }
    }
    return ticketMessage;
  }
  
  public void setTicketMessage(String msg) {
    this.ticketMessage = msg;
    SettingEntry s = new SettingEntry();
    s.setKey("TicketMessage");
    s.setValue(msg);
    settingsController.create(s);
  }
  
  public boolean getShowOrganiserEmail() {
    if (showOrganiserEmail == null) {
      SettingEntry s = settingsController.getSetting("ShowOrganiserEmail");
      if (s != null) {
        showOrganiserEmail = Boolean.parseBoolean(s.getValue());
      } else {
        showOrganiserEmail = false;
      }
    }
    return showOrganiserEmail;
  }
  
  public void setShowOrganiserEmail(boolean show) {
    showOrganiserEmail = show;
    SettingEntry s = new SettingEntry();
    s.setKey("ShowOrganiserEmail");
    s.setValue(Boolean.toString(show));
    settingsController.create(s);
  }

  public String getColour(String day) {
    if (dayColours.containsKey(day)) {
      return dayColours.get(day);
    }
    // Get from the database.
    SettingEntry s = settingsController.getSetting(day + "Colour");
    if (s != null) {
      // Put into the map.
      dayColours.put(day, s.getValue());
    } else {
      // Put the default into the map
      dayColours.put(day, dayColoursDefault.get(day));
    }
    return dayColours.get(day);
  }
  
  public void setColour(String day, String colour) {
    dayColours.put(day, colour);
    SettingEntry s = new SettingEntry();
    s.setKey(day + "Colour");
    s.setValue(colour);
    settingsController.create(s);
  }
  
  public String getDayBgColour(int dayNo) {
    String colour = "ffffff";
    switch (dayNo) {
      case 1:
        colour = getColour("Monday");
        break;
      case 2:
        colour = getColour("Tuesday");
        break;
      case 3:
        colour = getColour("Wednesday");
        break;
      case 4:
        colour = getColour("Thursday");
        break;
      case 5:
        colour = getColour("Friday");
        break;
    }
    return "#"+colour;
  }
}
