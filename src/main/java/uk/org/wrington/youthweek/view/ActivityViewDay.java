/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.Serializable;
import java.util.List;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.controller.ActivityController;
import uk.org.wrington.youthweek.model.Activity;

/**
 *
 * @author wilson_pjr
 */
public class ActivityViewDay implements Serializable {

  // The name of the day.
  private final int dayNumber;
  // List of activities.
  private List<Activity> activities = null;
  // Which Activity is selected in this day.
  private Activity selected = null;
  // Controller
  private final ActivityController controller;

  public ActivityViewDay(int dayNumber, ActivityController ac) {
    this.dayNumber = dayNumber;
    this.controller = ac;
  }

  public int getDayNumber() {
    return dayNumber;
  }

  public String getDayName() {
    return StaticValues.getDayLabel(dayNumber);
  }

  public void setActivities(List<Activity> activities) {
    this.activities = activities;
  }

  public List<Activity> getActivities() {
    if (activities == null) {
      setupActivities();
    }
    return activities;
  }

  private void setupActivities() {
//    activities = new ArrayList<>();
//    Activity a = new Activity();
//    a.setActivityid(1);
//    a.setActivityDay(dayNumber);
//    a.setName("Test Activity");
//    a.setConsentRequired(Boolean.TRUE);
//    Calendar c = Calendar.getInstance();
//    c.set(0, 0, 0, 9, 0, 0);
//    a.setStartTime(c.getTime());
//    c.set(0, 0, 0, 12, 0, 0);
//    a.setEndTime(c.getTime());
//    activities.add(a);
    //activities = controller.getActivitiesForDay(dayNumber);
  }

  public Activity getSelected() {
    return selected;
  }

  public void setSelected(Activity a) {
    selected = a;
  }

  public Activity createActivity() {
    System.out.println("Activity:prepareCreate");
    Activity created = new Activity();
    created.setActivityDay(dayNumber);
    return created;
  }

  @Override
  public boolean equals(Object o) {
    if (o != null && o instanceof ActivityViewDay) {
      return (((ActivityViewDay) o).dayNumber == dayNumber);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + this.dayNumber;
    return hash;
  }
}
