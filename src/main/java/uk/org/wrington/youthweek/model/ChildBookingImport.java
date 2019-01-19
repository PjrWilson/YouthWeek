/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author phil
 */
public class ChildBookingImport {
  
  // The list of chosen activies for a child
  private final List<ActivityImportDetail> bookedActivities = new LinkedList<>();
  // Whether the registration fee is chargeable for this child
  private boolean regFeeChargeable = true;
  // The sheet number.
  private int sheetId = 0;
  // Map of BBQ keys to numbers.
  private Map<String, Integer> bbqItems = new TreeMap<>();
  //private List<ExtraItemEntry> bbqItems = new LinkedList<>();
  
  // The child
  private Child child;
  
  public void setRegFeeChargeable(boolean isChargeable) {
    regFeeChargeable = isChargeable;
  }

  public void setSheetId(int sheetId) {
    this.sheetId = sheetId;
  }

  public void add(ActivityImportDetail activityDetail) {
    bookedActivities.add(activityDetail);
  }

  public void addBbqItem(String key, int value) {
    bbqItems.put(key, value);
  }

  public boolean getRegFeeChargeable() {
    return regFeeChargeable;
  }

  public int getSheetId() {
    return sheetId;
  }

  public List<ActivityImportDetail> getBookedActivities() {
    return bookedActivities;
  }
  
  public String getActivities(int dayNo) {
    String s = "";
    for (ActivityImportDetail aid : bookedActivities) {
      Activity a = aid.getActivity();
      if (a != null && a.getActivityDay().equals(dayNo)) {
        s += a.getName() + " (" + aid.getEventId() + ") ";
      }
      else if (a == null && dayNo == 6) {
        s += "Unknown (" + aid.getEventId() + ") ";
      }
    }
    if (s.length() == 0) {
      s = "None";
    }
    return s;
  }
  
  public Map<String, Integer> getBbqItems() {
    return bbqItems;
  }
  
  public void setChild(Child c) {
    this.child = c;
    
    System.out.println("Set sheet id " + sheetId + " child to " + child.toString());
  }
  
  public Child getChild() {
    return child;
  }
}
