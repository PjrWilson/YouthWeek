/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import uk.org.wrington.youthweek.controller.ActivityController;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.util.CopyUtil;
import uk.org.wrington.youthweek.model.util.DateHelper;
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
  private ActivityViewDay selectedDay = null;
  private Activity created = null;
  @ManagedProperty(value = "#{settings}")
  private Settings settings;

  @PostConstruct
  public void init() {
    // Create the days.
    for (int i = 1; i <= 6; ++i) {
      activityDays.add(new ActivityViewDay(i, activityController));
    }
    // Select first
    setSelectedDay(activityDays.get(0));
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

  // Get the days list.
  public List<ActivityViewDay> getActivityDays() {
    return activityDays;
  }

  public ActivityViewDay getSelectedDay() {
    return selectedDay;
  }

  public void setSelectedDay(ActivityViewDay day) {
    // Make sure there's a change
    if (!day.equals(selectedDay)) {
      selectedDay = day;
      // Make sure activities are set.
      selectedDay.setActivities(activityController.getActivitiesForDay(selectedDay.getDayNumber()));
    }
  }

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
    activityController.update(selectedDay.getSelected());
  }

  public void moveSelected(Integer newDay) {
    // Get the selected item in the selected day.
    Activity a = selectedDay.getSelected();

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
        selectedDay.getActivities().remove(a);
        selectedDay.setSelected(null);
      }
    }
  }

  public String getDayDate() {
    int dayNo = selectedDay.getDayNumber();
    System.out.println("getDayDate for " + selectedDay.getDayName());
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
    if (selectedDay != null) {
      selectedDay.setActivities(activityController.getActivitiesForDay(selectedDay.getDayNumber()));
    }
  }
}
