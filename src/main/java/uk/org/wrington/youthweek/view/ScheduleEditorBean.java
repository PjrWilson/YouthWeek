/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.controller.ActivityController;
import uk.org.wrington.youthweek.controller.ChildController;
import uk.org.wrington.youthweek.controller.ExtraItemController;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.ExtraItem;
import uk.org.wrington.youthweek.model.ExtraItemEntry;
import uk.org.wrington.youthweek.model.util.Basket;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean
@ViewScoped
public class ScheduleEditorBean implements Serializable {

  @ManagedProperty(value = "#{childView}")
  private ChildViewBean childView;
  @ManagedProperty(value = "#{activityController}")
  private ActivityController activityController;
  @ManagedProperty(value = "#{extraItemController}")
  private ExtraItemController extraItemController;
  @ManagedProperty(value = "#{settings}")
  private Settings settings;
  @ManagedProperty(value = "#{childController}")
  private ChildController childController;

  private Basket basket = null;

  private List<Activity> childActivities = null;

  private ActivityEntry selected = null;
  
  // private List<ActivityEntry> activityEntries; // = new ArrayList<>();
  //private List<ExtraItemEntry> extraItemEntries; // = new ArrayList<>();
//  private final Activity dummy = new Activity();
  private final ExtraItem dummyExtra = new ExtraItem();

  public ScheduleEditorBean() {
    System.out.println("ScheduleWizard()");
  //  dummy.setActivityid(Integer.MAX_VALUE);
  //  dummy.setName("");
  //  dummy.setCost(0d);
    dummyExtra.setItemid(Integer.MAX_VALUE);
    dummyExtra.setName("Select");
  }

//  @PostConstruct
  public void init() {
    System.out.println("Init schedule editor");
    basket = null;
    getBasket();
    //selected = childView.getSelected();
    childActivities = null;
//    activityEntries = basket.getChild().getActivities();
//    if (activityEntries == null) {
//      activityEntries = new ArrayList<>();
//    }
//    extraItemEntries = basket.getChild().getExtraItems();
//    if (extraItemEntries == null) {
//      extraItemEntries = new ArrayList<>();
//    }
  }

  public void setChildView(ChildViewBean cvb) {
    this.childView = cvb;
  }

  public void setActivityController(ActivityController ac) {
    this.activityController = ac;
  }

  public void setExtraItemController(ExtraItemController eic) {
    this.extraItemController = eic;
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
  public List<Activity> getChildActivities() {

    if (childActivities == null) {
      childActivities
              = activityController.getItemsForSchoolYear(settings.getSchoolYearFor(
                              getChild(), 0));
      System.out.println("Got " + childActivities.size() + " activities for " + childView.getSelected().getFirstname());
      // Sort by the activity day.
//      Comparator c = new Comparator() {
//
//        @Override
//        public int compare(Object o1, Object o2) {
//          // Activities.
//          Activity a1 = (Activity)o1;
//          Activity a2 = (Activity)o2;
//          return a1.getActivityDay().compareTo(a2.getActivityDay());
//        }
//      };
//      Collections.sort(childActivities, c);
    }
    return childActivities;
  }

  public List<ExtraItem> getExtraItems() {
    return extraItemController.getItems();
  }

  public Child getChild() {
    return getBasket().getChild();
  }

  public void prepareInsertActivity() {
    // Create new activity entry.
    ActivityEntry ae = new ActivityEntry();
 //   ae.setActivity(dummy);
    ae.setConsentFormSigned(Boolean.FALSE);
    basket.getActivityEntries().add(ae);
    selected = ae;
  }

  public void prepareInsertExtraItem() {
    // Create new item entry.
    ExtraItemEntry eie = new ExtraItemEntry();
    eie.setItem(dummyExtra);
    eie.setItemCount(0);
    eie.setFreeItem(Boolean.FALSE);
    basket.getExtraItemEntries().add(eie);
  }

  public void consentCheckboxListener(AjaxBehaviorEvent event) {
  }

  public void onActivityChange() {
    System.out.println("Activity has changed");
    basket.calculateActivityCost();
  }

  public void onItemChange() {
    System.out.println("Item has changed");
    basket.calculateExtrasCost();
  }

  public Basket getBasket() {

    Child c = null;
    if (childView.getSelected() != null) {
      c = childView.getSelected();
    }
    // See if there's any change
    if (basket != null) {
      if (basket.getChild() != null
              && !basket.getChild().equals(c)) {
        basket = null;
      }
    }
    if (basket == null) {
      basket = new Basket(c, settings);
    }

    return basket;
  }

  public Float getAdminFee() {
    return settings.getAdminFee();
  }

  public void save() {
    Child child = getBasket().getChild();
    // Remove any dummy items from activities.
    List<ActivityEntry> activities = getBasket().getActivityEntries();
    Iterator<ActivityEntry> iter = activities.iterator();
    while (iter.hasNext()) {
      ActivityEntry ae = iter.next();
      if (ae.getActivity().getActivityid() == Integer.MAX_VALUE) {
        iter.remove();
      } else {
        ae.setChild(child);
      }
    }
    // And extras
    List<ExtraItemEntry> extras = getBasket().getExtraItemEntries();
    Iterator<ExtraItemEntry> iter2 = extras.iterator();
    while (iter2.hasNext()) {
      ExtraItemEntry eie = iter2.next();
      if (eie.getItem() == dummyExtra) {
        iter2.remove();
      } else {
        eie.setChild(child);
      }
    }

    // Update the values in the child
    child.setActivities(activities);
    child.setExtraItems(extras);
    child.setNoAdminFee(getBasket().getNoAdminFee());
    childController.update(child);
  }

  public void removeActivity(ActivityEntry entry) {
    if (entry == selected) {
      selected = null;
    }
    getBasket().getActivityEntries().remove(entry);
    onActivityChange();
  }

  public void removeExtraItem(ExtraItemEntry entry) {
    System.out.println("Removing " + entry.toString());
    getBasket().getExtraItemEntries().remove(entry);
    onItemChange();
  }

  public List<ActivityEntry> getActivityEntries() {
    return getBasket().getActivityEntries();
  }

//  public void activitySelectionChanging(ValueChangeEvent event) {
//    System.out.println("Changing activity menu");
//    // Make sure that the new one is not already selected.
//    Activity newA = (Activity) event.getNewValue();
//    if (newA != dummy) {
//      for (ActivityEntry ae : getActivityEntries()) {
//        if (ae.getActivity().equals(newA)) {
//          // Already there.
//          throw new AbortProcessingException();
//        }
//      }
//    }
//  }

  public List<Activity> completeActivity(String query) {
    
    List<Activity> allActivities = getChildActivities();
    List<Activity> filteredActivities = new ArrayList<>();

    String queryLower = query.toLowerCase();
    for (Activity activity : allActivities) {
      if (activity.getName().toLowerCase().contains(queryLower)) {
        // See if its in the current selected activites.
        boolean add = true;
        for (ActivityEntry ae : this.getActivityEntries()) {
          if (ae.getActivity() == null || ae.getActivity().equals(activity)) {
            add = false;
            break;
          }
        }
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
  
  public ActivityEntry getSelected() {
    return selected;
  }
  
  public void setSelected(ActivityEntry e) {
    this.selected = e;
  }
}
