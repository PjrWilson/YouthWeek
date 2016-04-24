/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import uk.org.wrington.youthweek.controller.ChildController;
import uk.org.wrington.youthweek.controller.ContactController;
import uk.org.wrington.youthweek.model.Child;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "childView")
@ViewScoped
public class ChildViewBean implements Serializable {

  // The controller.
  @ManagedProperty(value = "#{childController}")
  private ChildController childController;
  // Need to make sure this is created
  @ManagedProperty(value = "#{contactController}")
  private ContactController contactController;
//  @ManagedProperty(value = "#{activityController}")
//  private ActivityController activityController;
//  @ManagedProperty(value = "#{settings}")
//  private Settings settings;

  private Child selected = null;
  private Child created = null;

  private int rows = 25;

//  @PostConstruct
//  public void init() {
//    // Create the days.
//    for (int i = 1; i <= 5; ++i) {
//      activityDays.add(new ActivityViewDay(i, activityController));
//    }
//    // Select first
//    setSelectedDay(activityDays.get(0));
//  }
  public void setChildController(ChildController cc) {
    this.childController = cc;
  }

  public ChildController getChildController() {
    return childController;
  }

  public void setContactController(ContactController cc) {
    this.contactController = cc;
  }

  public ContactController getContactController() {
    return contactController;
  }

  public void setSelected(Child child) {
    selected = child;
  }

  public Child getSelected() {
    return selected;
  }

  public void prepareCreate() {
    System.out.println("ChildView:prepareCreate");
    created = new Child();
  }

  public Child getCreated() {
    return created;
  }

  public void finaliseCreate() {
    if (created != null) {
      System.out.println("ChildView:creating " + created.getFirstname());
      selected = childController.create(created);
      created = null;
    }
  }

  public List<Child> getChildren() {
    return childController.getItems();
  }

  public void updateSelected() {
    childController.update(selected);
  }

  public void deleteSelected() {
    childController.destroy(selected);
    selected = null;
  }

  public void prepareScheduleEditor() {
    // The child to be edited is selected.
    System.out.println("Edit schedule for " + selected.getFirstname());
    // Update list of activities available to this child.
    // childActivities = activityController.getItemsForSchoolYear(settings.getSchoolYearFor(selected, 0));
  }

  public boolean filterByActivityCount(Object value, Object filter, Locale locale) {
    Integer columnValue = (Integer) value;
    Integer filterValue = (Integer) filter;

    switch (filterValue) {
      case -1:
        return true;
      case 0:
        // Only if value is 0.
        return columnValue.equals(0);
      case 1:
        // If > 1.
        return columnValue.compareTo(0) > 0;
    }
    return true;
  }

  public boolean filterBySchoolYear(Object value, Object filter, Locale locale) {
    
    Integer columnValue = (Integer) value;
    Integer filterValue = (Integer) filter;

    switch (filterValue) {
      case -1:
        // All
        return true;
      case 1:
        return columnValue >= 0 && columnValue <= 2;
      case 2:
        return columnValue >= 3 && columnValue <= 6;
      case 3:
        return columnValue >= 7 && columnValue <= 9;
      case 4:
        return columnValue >= 10 && columnValue <= 11;
      case 5:
        return columnValue >= 12 && columnValue <= 13;
      case 6:
        return columnValue > 13;
    }
    return true;
  }
//  public String formatParentContact(Contact c) {
//    String s = null;
//    // Use firstname surname + email & emergency contact.
//    if (c != null) {
//      s = c.getFirstname() + " " + c.getSurname();
////      if (c.getEmail() != null && c.getEmail().length() > 0) {
////        s += "\nemail: " + c.getEmail();
////      }
//      if (c.getEmergencyPhone() != null && c.getEmergencyPhone().length() > 0) {
//        s += " (" + c.getEmergencyPhone() + ")";
//      }
//    }
//   // System.out.println("Parent: " + s);
//    return s;
//  }

  public int getRows() {
    return rows;
  }

  public void setRows(int i) {
    System.out.println("Rows set to " + i);
    rows = i;
  }
}
