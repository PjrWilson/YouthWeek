/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.controller.ActivityController;
import uk.org.wrington.youthweek.controller.ChildController;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean
@ViewScoped
public class ActivityGroupingBean implements Serializable {

  @EJB
  private uk.org.wrington.youthweek.model.ActivityFacade ejbFacade;
  @EJB
  private uk.org.wrington.youthweek.model.ActivityEntryFacade ejbFacadeEntry;

  @ManagedProperty(value = "#{activityController}")
  private ActivityController activityController;
  @ManagedProperty(value = "#{settings}")
  private Settings settings;
  @ManagedProperty(value = "#{childController}")
  private ChildController childController;

  private List<Activity> sourceActivities = null;
  private List<Activity> targetActivities = null;

  private List<ActivityEntry> sourceChildren = null;
  private List<ActivityEntry> targetChildren = null;

  private Activity selectedTarget = null;
  private Activity selectedSource = null;

  private List<ActivityEntry> selectedSourceChildren = null;
  private List<ActivityEntry> selectedTargetChildren = null;

  public ActivityGroupingBean() {
  }

  public void setActivityController(ActivityController ac) {
    this.activityController = ac;
  }

  public void setChildController(ChildController cc) {
    this.childController = cc;
  }

  public void setSettings(Settings s) {
    this.settings = s;
  }

//  public List<ActivityEntry> getActivityEntries() {
////    System.out.println("Get Activity Entries " + activityEntries.size());
//    return basket.getChild().getActivities();
//  }
//  public List<ExtraItemEntry> getExtraItemEntries() {
//    return basket.getChild().getExtraItems();
//  }
//
  public List<Activity> getSourceActivities() {

    if (sourceActivities == null) {
      System.out.println("Getting source activities");
      sourceActivities
              = ejbFacade.getEntityManager().createQuery(
                      "SELECT e FROM Activity e WHERE e.activityday < 6 ORDER BY e.activityday").getResultList();
    }
    return sourceActivities;
  }

  public List<Activity> getTargetActivities() {

    if (targetActivities == null) {
      System.out.println("Getting source activities");
      targetActivities
              = ejbFacade.getEntityManager().createQuery(
                      "SELECT e FROM Activity e WHERE e.activityday < 6 ORDER BY e.activityday").getResultList();
    }
    return targetActivities;
  }

  public void onSourceActivityChange() {
    System.out.println("Source Activity has changed");
    sourceChildren = null;
  }

  public void onTargetActivityChange() {
    System.out.println("TargetActivity has changed");
    targetChildren = null;

  }

  public List<Activity> completeSourceActivity(String query) {

    List<Activity> allActivities = getSourceActivities();
    List<Activity> filteredActivities = new ArrayList<>();

    String queryLower = query.toLowerCase();
    for (Activity activity : allActivities) {
      if (activity.getName().toLowerCase().contains(queryLower)) {
        // See if its in the current selected activites.
        boolean add = !activity.equals(selectedTarget);
        if (add) {
          filteredActivities.add(activity);
        }
      }
    }

    return filteredActivities;
  }

  public List<Activity> completeTargetActivity(String query) {

    List<Activity> allActivities = getSourceActivities();
    List<Activity> filteredActivities = new ArrayList<>();

    String queryLower = query.toLowerCase();
    for (Activity activity : allActivities) {

      if (activity.getName().toLowerCase().contains(queryLower)) {
        // See if its in the current selected activites.
        boolean add = !activity.equals(selectedSource);
        if (add) {
          filteredActivities.add(activity);
        }
      }
    }

    return filteredActivities;
  }

  public String getGroup(Activity a) {
    if (a != null && a.getActivityDay() != null && a.getActivityDay() < 6) {
      return StaticValues.getDayLabel(a.getActivityDay());
    }
    return "";
  }

  public Activity getSelectedSource() {
    return selectedSource;
  }

  public void setSelectedSource(Activity a) {
    this.selectedSource = a;
  }

  public Activity getSelectedTarget() {
    return selectedTarget;
  }

  public void setSelectedTarget(Activity a) {
    this.selectedTarget = a;
  }

  public List<ActivityEntry> getSourceChildren() {
    System.out.println("Getting source children");
    if (sourceChildren == null && selectedSource != null) {
      sourceChildren = ejbFacadeEntry.getEntityManager().createQuery(
              "SELECT e FROM ActivityEntry e WHERE e.activity.activityid = " + selectedSource.getActivityid()
              + " ORDER BY e.child.dateOfBirth").getResultList();
    }
    return sourceChildren;
  }

  public List<ActivityEntry> getTargetChildren() {
    System.out.println("Getting target children");
    if (targetChildren == null && selectedTarget != null) {
      targetChildren = ejbFacadeEntry.getEntityManager().createQuery(
              "SELECT e FROM ActivityEntry e WHERE e.activity.activityid = " + selectedTarget.getActivityid()
              + " ORDER BY e.child.dateOfBirth").getResultList();
    }
    return targetChildren;
  }

  public List<ActivityEntry> getSelectedSourceChildren() {
    return selectedSourceChildren;
  }

  public void setSelectedSourceChildren(List<ActivityEntry> ae) {
    this.selectedSourceChildren = ae;
  }

  public List<ActivityEntry> getSelectedTargetChildren() {
    return selectedTargetChildren;
  }

  public void setSelectedTargetChildren(List<ActivityEntry> ae) {
    this.selectedTargetChildren = ae;
  }

  private boolean canMove(List<ActivityEntry> from, List<ActivityEntry> to, Activity toActivity) {
    for (ActivityEntry aeFrom : from) {
      // Make sure this child is not in the to list.
      if (to != null) {
        for (ActivityEntry aeTo : to) {
          if (aeFrom.getChild().equals(aeTo.getChild())) {
            return false;
          }
        }
      }
      // Check also that this target activity age range is ok.
      int schoolYear = settings.getSchoolYearFor(aeFrom.getChild(), 0);
      if (schoolYear < toActivity.getMinyear() || schoolYear > toActivity.getMaxyear()) {
        return false;
      }
    }
    return true;
  }

  public boolean canMoveRight() {
    // Source & Target needs to be set.
    if (selectedSource != null && selectedTarget != null) {
      // Make sure there are things selected.
      if (selectedSourceChildren != null && selectedSourceChildren.size() > 0) {
        // Make sure none of the selected source children are already in the target list.
        return canMove(selectedSourceChildren, targetChildren, selectedTarget);
      }
    }
    return false;
  }

  public boolean canMoveLeft() {
    // Source & Target needs to be set.
    if (selectedSource != null && selectedTarget != null) {
      // Make sure there are things selected.
      if (selectedTargetChildren != null && selectedTargetChildren.size() > 0) {
        // Make sure none of the selected source children are already in the target list.
        return canMove(selectedTargetChildren, sourceChildren, selectedSource);
      }
    }
    return false;
  }

  private void move(List<ActivityEntry> entries, Activity from, Activity to) {
    for (ActivityEntry ae : entries) {
      ae.setActivity(to);
      ActivityEntry newAe = ejbFacadeEntry.edit(ae);
      if (newAe != null) {
        JsfUtil.addSuccessMessage("Moved " + newAe.getChild().getFirstname()
                + " from " + from.getName()
                + " to " + to.getName());
      }
    }

  }

  public void moveRight() {
    if (selectedTarget == null) {
      return;
    }
    move(selectedSourceChildren, selectedSource, selectedTarget);
    sourceChildren = null;
    selectedSourceChildren = null;
    targetChildren = null;
  }

  public void moveLeft() {
    if (selectedSource == null) {
      return;
    }
    move(selectedTargetChildren, selectedTarget, selectedSource);
    sourceChildren = null;
    targetChildren = null;
    selectedTargetChildren = null;
  }

}
