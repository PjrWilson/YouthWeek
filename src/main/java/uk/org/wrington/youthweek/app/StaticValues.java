/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.app;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wilson_pjr
 */
public class StaticValues {

  private static final List<Integer> minSchoolYears = Arrays.asList(-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
  private static final List<Integer> maxSchoolYears = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 99);
  private static final String MONDAY = "Monday";
  private static final String TUESDAY = "Tuesday";
  private static final String WEDNESDAY = "Wednesday";
  private static final String THURSDAY = "Thursday";
  private static final String FRIDAY = "Friday";
  private static final List<String> periodLabels = Arrays.asList("Morning", "Afternoon", "Evening", "All Day", "Morning & Afternoon", "Aftornoon & Evening");
  private static List<Integer> periodValues = new LinkedList<>();
  
  public static List<Integer> getMinSchoolYears() {
    return minSchoolYears;
  }

  public static List<Integer> getMaxSchoolYears() {
    return maxSchoolYears;
  }

  public static String getMenuLabel(Integer in) {
    
    String label = "";
    if (in != null) {
      switch (in) {
        case -1:
        case 99:
          label = "Any";
          break;
        case 0:
          label = "Reception";
          break;
        default:
          label = in.toString();
          break;
      }
    }
    return label;
  }

  public static String getPeriodLabel(Integer in) {
    if (in != null && in >= 0) {
      return periodLabels.get(in);
    }
    return "Not Set";
  }
  
  public static int getPeriodIndex(String in) {
    return periodLabels.indexOf(in);
  }
  
  public static List<Integer> getPeriodValues() {
    if (periodValues.isEmpty()) {
      int index = 0;
      for (String s : getPeriodLabels()) {
        periodValues.add(index++);
      }
    }
    return periodValues;
  }

  public static List<String> getPeriodLabels() {
    return periodLabels;
  }
  
  public static String getYearLabel(Integer in) {
    
    String label = "";
    if (in != null) {
      switch (in) {
        case 0:
          label = "Reception";
          break;
        default:
          label = in.toString();
          break;
      }
      // If > 13, not in school anymore
      if (in > 13 || in < 0) {
        label = "N/A";
       }
    }
    return label;
  }

  public static String getDayLabel(Integer in) {
    if (in == null) {
      return "Unknown day";
    }

    switch (in) {
      case 1:
        return MONDAY;
      case 2:
        return TUESDAY;
      case 3:
        return WEDNESDAY;
      case 4:
        return THURSDAY;
      case 5:
        return FRIDAY;
      default:
        return "Unassigned";
    }
  }

}
