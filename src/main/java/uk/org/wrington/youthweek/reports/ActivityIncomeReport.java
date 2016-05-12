/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.persistence.Query;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.controller.ChildController;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.Child;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "activityIncomeReport")
@ViewScoped
public class ActivityIncomeReport implements Serializable {

  public class ActivityInfo {

    private final Activity activity;
    private long numberBooked = 0;
    private final double income;

    public ActivityInfo(Activity a, long numberBooked) {
      this.activity = a;
      this.numberBooked = numberBooked;
      income = a.getCost() * numberBooked;
    }

    public Activity getActivity() {
      return activity;
    }

    public long getNumberBooked() {
      return numberBooked;
    }

    public double getIncome() {
      return income;
    }
  }

  public class ActivityDay {

    private final int dayNo;
    private final List<ActivityInfo> activityInfo = new ArrayList<>();
    private double dayIncome = 0d;

    public ActivityDay(int dayNo) {
      this.dayNo = dayNo;
    }

    public int getDayNo() {
      return dayNo;
    }

    public String getDayName() {
      return StaticValues.getDayLabel(dayNo);
    }

    public void add(ActivityInfo ai) {
      activityInfo.add(ai);
      dayIncome += ai.getIncome();
    }

    public List<ActivityInfo> getActivityInfo() {
      return activityInfo;
    }

    public double getDayIncome() {
      return dayIncome;
    }
  }
  // The controller.
  @ManagedProperty(value = "#{childController}")
  private ChildController childController;
  @EJB
  uk.org.wrington.youthweek.model.ActivityFacade ejbFacade;
  @EJB
  uk.org.wrington.youthweek.model.ActivityEntryFacade ejbFacadeEntry;

//  private Map<Integer, List<ActivityInfo>> activitiesPerDay;
  private List<ActivityDay> activityDays;
  private double allIncome = 0d;
  private int childrenPayingAdminFee = 0;
  private int childrenWithActivities = 0;
  // private final double[] dayIncome = new double[5];

  /**
   * Creates a new instance of ActivityIncomeReport
   */
  public ActivityIncomeReport() {
  }

  public void init() {
    //  System.out.println("Init Report");
    activityDays = new ArrayList<>();
    allIncome = 0d;
    
    // Get the activities for each day.
    for (int i = 1; i <= 5; ++i) {
      //   dayIncome[i - 1] = 0;
      ActivityDay day = new ActivityDay(i);
      List<Activity> activities = getActivities(i); //activityController.getActivitiesForDay(i);
      //  System.out.println("Found " + activities.size() + " activities for day "+ i);
//      List<ActivityInfo> infoList = new ArrayList<>();
      for (Activity a : activities) {
        ActivityInfo info = new ActivityInfo(a, getCountFor(a));
        day.add(info);
        allIncome += info.getIncome();
      }
      activityDays.add(day);
    }
    
    // Find all children with > 0 activities who are paying the admin fee.
    childrenPayingAdminFee = 0;
    childrenWithActivities = 0;
    List<Child> allChildren = childController.getItems();
    for (Child c : allChildren) {
      List<ActivityEntry> childActivities = c.getActivities();
      if (childActivities != null && childActivities.size() > 0) {
        ++childrenWithActivities;
        if (!c.getNoAdminFee()) {
          ++childrenPayingAdminFee;
        }
      }
    }
  }

//  public void setActivityController(ActivityController ac) {
//    this.activityController = ac;
//  }
//
  public void setChildController(ChildController cc) {
    this.childController = cc;
  }

  public List<ActivityDay> getDays() {
    return activityDays;
  }

//  public ActivityDay getDay(int dayNo) {
//    if (activityDays != null && dayNo <= activityDays.size()) {
//      return activityDays.get(dayNo - 1);
//    }
//    return null;
//  }
  public double getAllIncome() {
    return allIncome;
  }

//  public double getIncomeForDay(int dayNo) {
//    return dayIncome[dayNo - 1];
//  }
  private List<Activity> getActivities(int i) {
    return createQuery().setParameter("activityday", i).getResultList();
  }

  protected Query createQuery() {
    return ejbFacade.getEntityManager().createNamedQuery("Activity.findByDay");
  }

  private long getCountFor(Activity a) {
    return ejbFacadeEntry.countEntriesFor(a);
  }

  public long getTotalActivities() {
    long count = 0;
    for (ActivityDay day : activityDays) {
      count += day.getActivityInfo().size();
    }
    return count;
  }

//  private final String bgCols[] = { "lightblue", "yellow", "red", "green", "pink" };
//  private final String fgCols[] = { "black", "black", "black", "black", "black" };
//  
//  public String getDayBgColour(int dayNo) {
//    return bgCols[dayNo-1];
//  }
//
//  public String getDayTextColour(int dayNo) {
//    return fgCols[dayNo-1];
//  }
//
//  public String getDayActivitiesBgColour(int dayNo) {
//    return bgCols[dayNo-1];
//  }
//
//  public String getDayActivitiesTextColour(int dayNo) {
//    return fgCols[dayNo-1];
//  }

  public int getChildrenWithActivies() {
    return childrenWithActivities;
  }

  public int getChildrenPayingAdminFee() {
    return childrenPayingAdminFee;
  }
}
