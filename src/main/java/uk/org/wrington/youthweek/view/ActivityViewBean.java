/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import uk.org.wrington.youthweek.controller.ActivityController;
import uk.org.wrington.youthweek.export.ActivityExportGenerator;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.util.CopyUtil;
import uk.org.wrington.youthweek.model.util.DateHelper;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.YwDayBean;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "activityView")
@SessionScoped
public class ActivityViewBean implements Serializable {

  // The controller.
  @ManagedProperty(value = "#{activityController}")
  private ActivityController activityController;

  // List of Day entries.
  private final List<ActivityViewDay> activityDays = new ArrayList<>();
  private int dayValue = 0;
//  private ActivityViewDay selectedDay = null;
  private Activity created = null;
  @ManagedProperty(value = "#{settings}")
  private Settings settings;
  @ManagedProperty(value = "#{ywDayBean}")
  private YwDayBean ywDayBean;


  @PostConstruct
  public void init() {
    // Create the days.
    for (int i = 1; i <= 6; ++i) {
      activityDays.add(new ActivityViewDay(i, activityController));
    }
    // Select first
    setDayValue(1);
  }

  public int getDayValue() {
    return dayValue;
  }
  
  public void setDayValue(int dayValue) {
    System.out.println("Set Day Value to " + dayValue);
    // Make sure there's a change
    if (this.dayValue != dayValue) {
      this.dayValue = dayValue;
      // Make sure activities are set.
      ActivityViewDay selectedDay = getSelectedDay();
      selectedDay.setActivities(activityController.getActivitiesForDay(selectedDay.getDayNumber()));
    }
  }
  
  public void setActivityController(ActivityController ac) {
    this.activityController = ac;
  }

  public ActivityController getActivityController() {
    return activityController;
  }

  public void setSettings(Settings s) {
    this.settings = s;
  }

  public void setYwDayBean(YwDayBean ywDayBean) {
    this.ywDayBean = ywDayBean;
  }

  // Get the days list.
  public List<ActivityViewDay> getActivityDays() {
    return activityDays;
  }

  public ActivityViewDay getSelectedDay() {
    return activityDays.get(dayValue-1);
  }

//  public void setSelectedDay(ActivityViewDay day) {
//    // Make sure there's a change
//    if (!day.equals(selectedDay)) {
//      selectedDay = day;
//      // Make sure activities are set.
//      selectedDay.setActivities(activityController.getActivitiesForDay(selectedDay.getDayNumber()));
//    }
//  }

  public void prepareCreate(ActivityViewDay day) {
    System.out.println("ActivtyDayView:prepareCreate");
    if (day != null) {
      created = day.createActivity();
    }
  }

  public void prepareCopy(ActivityViewDay day) {
    System.out.println("ActivtyDayView:prepareCopy");
    if (day != null) {
      created = CopyUtil.clone(day.getSelected());
    }
  }

  public Activity getCreated() {
    return created;
  }

  public void deleteSelected() {
    ActivityViewDay selectedDay = getSelectedDay();
    if (selectedDay != null) {
      Activity activity = selectedDay.getSelected();
      activityController.destroy(activity);
      selectedDay.setSelected(null);
      // Make sure activities are set.
      selectedDay.setActivities(activityController.getActivitiesForDay(selectedDay.getDayNumber()));
    }
  }

  public void finaliseCreate() {
    if (created != null) {
      ActivityViewDay day = activityDays.get(created.getActivityDay() - 1);
      System.out.println("ActivtyDayView:creating " + created.getName() + " on " + day.getDayName());
      activityController.create(created);
      day.setActivities(activityController.getActivitiesForDay(day.getDayNumber()));
      created = null;
    }
  }

  public void update() {
    activityController.update(getSelectedDay().getSelected());
  }

  public void moveSelected(Integer newDay) {
    // Get the selected item in the selected day.
    Activity a = getSelectedDay().getSelected();

    // Check day is within range
    if (newDay >= 1 && newDay <= 6) {
      // See if its different.
      if (!a.getActivityDay().equals(newDay)) {
        a.setActivityDay(newDay);
        // This needs to be updated.
        // Get the new day.
        ActivityViewDay newDayView = activityDays.get(newDay - 1);
        // Unset its activities so that they are refreshed on change
        //newDayView.setActivities(null);
        if (newDayView.getActivities() != null) {
          newDayView.getActivities().add(a);
        }
        activityController.update(a);
        // Reset activities for current day.
        //selectedDay.setActivities(activityController.getActivitiesForDay(selectedDay.getDayNumber()));
        getSelectedDay().getActivities().remove(a);
        getSelectedDay().setSelected(null);
      }
    }
  }

  public String getDayDate() {
    int dayNo = getSelectedDay().getDayNumber();
    System.out.println("getDayDate for " + getSelectedDay().getDayName());
    if (dayNo >= 1 && dayNo <= 5) {
      Calendar c = DateHelper.getInstance().getCalendar(settings.getStartDate());
      // Add day - 1.
      c.add(Calendar.DAY_OF_YEAR, dayNo - 1);
      return DateHelper.getInstance().formatDate("dd/MM/yyyy", c);
    }
    return "";
  }

  public void refresh() {
    // Update selected.
    if (getSelectedDay() != null) {
      getSelectedDay().setActivities(activityController.getActivitiesForDay(getSelectedDay().getDayNumber()));
    }
  }

  public void export() throws IOException {
    System.out.println("Export Activities");

    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();

    ActivityExportGenerator export = 
            new ActivityExportGenerator(ywDayBean,
            activityController.getFacade());
    StringBuilder sb = export.generate();
    
    String fileName = export.getFilename();
    int contentLength = sb.length();

    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
    ec.setResponseContentType("text/csv"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
    ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

    OutputStream output = ec.getResponseOutputStream();
    PrintWriter pw = new PrintWriter(output);
    pw.print(sb.toString());
    pw.flush();
    
    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

    JsfUtil.addSuccessMessage("Exported stuff");
  }

  public void dayValueChange() {
    System.out.println("Day Value Change to " + this.dayValue);
  }
}