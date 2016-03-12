/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.app;

import java.util.Arrays;
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

  public static List<Integer> getMinSchoolYears() {
    return minSchoolYears;
  }

  public static List<Integer> getMaxSchoolYears() {
    return maxSchoolYears;
  }

  public static String getMenuLabel(Integer in) {
    if (in != null) {
      switch (in) {
        case -1:
        case 99:
          return "Any";
        case 0:
          return "Reception";
        default:
          return in.toString();
      }
    }
    return "";
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
