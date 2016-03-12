/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.ExtraItemEntry;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
public class Basket {

  private final Child child;
  private double activityCost = 0;
  private double extrasCost = 0;
  private List<ActivityEntry> activityEntries;
  private List<ExtraItemEntry> extraItemEntries;
  private final Settings settings;
  private Boolean noAdminFee = false;
  
  public Basket(Child child, Settings settings) {
    System.out.println("New basket for " + child);
    this.child = child;
    this.settings = settings;
    if (child != null && child.getActivities() != null) {
      //child.setActivities(new ArrayList<ActivityEntry>());
      activityEntries = new ArrayList<>(child.getActivities());
      // Sort by the activity day.
      Comparator c = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
          // Activities.
          ActivityEntry a1 = (ActivityEntry) o1;
          ActivityEntry a2 = (ActivityEntry) o2;
          return a1.getActivity().getActivityDay().compareTo(a2.getActivity().getActivityDay());
        }
      };
      Collections.sort(activityEntries, c);

    } else {
      activityEntries = new ArrayList<>();
    }
    if (child != null && child.getExtraItems() != null) {
      //child.setExtraItems(new ArrayList<ExtraItemEntry>());
      extraItemEntries = new ArrayList<>(child.getExtraItems());
    } else {
      extraItemEntries = new ArrayList<>();
    }
    if (child != null && child.getNoAdminFee() != null) {
      noAdminFee = child.getNoAdminFee();
    }
    calculateActivityCost();
    calculateExtrasCost();
  }

//  public void setSettings(Settings s) {
//    this.settings = s;
//  }
  public Child getChild() {
    return child;
  }

  public final void calculateActivityCost() {
    activityCost = 0;
    for (ActivityEntry ae : getActivityEntries()) {
      // Update count/cost
      activityCost += ae.getActivity().getCost();
    }
  }

  public final void calculateExtrasCost() {
    extrasCost = 0;
    for (ExtraItemEntry eie : getExtraItemEntries()) {
      // Update count/cost
      extrasCost += eie.getCost();
    }
    System.out.println("Extras Cost = " + extrasCost);
  }

  public int getActivityCount() {
    return getActivityEntries().size();
  }

  public double getActivityCost() {
    return activityCost;
  }

  public Float getAdminFee() {
    Float f = (float) 0;
    if (getNoAdminFee() == false) {
      f = settings.getAdminFee();
    }
    return f;
  }

  public double getBasketTotal() {
    double total = activityCost + extrasCost + getAdminFee();
    return total;
  }

  public List<ActivityEntry> getActivityEntries() {
    //System.out.println("Get Activity Entries " + activityEntries.size());
    return activityEntries;
  }

  public List<ExtraItemEntry> getExtraItemEntries() {
    return extraItemEntries;
  }

  public Boolean getNoAdminFee() {
    return noAdminFee;
  }

  public void setNoAdminFee(Boolean b) {
    noAdminFee = b;
  }
}
