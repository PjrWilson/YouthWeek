/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.export;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import uk.org.wrington.youthweek.app.StaticValues;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.util.YwDayBean;

/**
 *
 * @author phil
 */
public class ActivityExportGenerator {

  YwDayBean ywDayBean;
  uk.org.wrington.youthweek.model.ActivityFacade ejbFacade;

  static String[] periodLabels
          = {
            "All Day",
            "Morning & Afternoon",
            "Morning",
            "Afternoon",
            "Aftornoon & Evening",
            "Evening"
          };

  static String[] csvFixedHeader
          = {
            "School Year",
            "Mon Date",
            "Tuesday Date",
            "Wednesday Date",
            "Thursday Date",
            "Friday Date"
          };

  static String[] csvRepeatingHeader
          = {" Id ",
            " Time ",
            " Event ",
            " Venue ",
            " Cost ",
            " Description ",
            " Notes ",
            " Constent Form ",
            " Restrictions ",};

  public static String FILENAME = "ActivityExport-%s.csv";

  public ActivityExportGenerator(YwDayBean ywDayBean,
          uk.org.wrington.youthweek.model.ActivityFacade ejbFacade) {
    this.ywDayBean = ywDayBean;
    this.ejbFacade = ejbFacade;
  }

  public String getFilename() {
    return String.format(FILENAME, ywDayBean.getYear());
  }

  public StringBuilder generate() {
    return writeCsv(generateActivityInformation());
  }

  private List<Activity> getActivities(int i) {
    return createQuery().setParameter("activityPeriod", i).getResultList();
  }

  protected Query createQuery() {
    return ejbFacade.getEntityManager().createNamedQuery("Activity.findForExport");
  }

  private StringBuilder writeCsv(Map<Integer, Map<Integer, List<Activity>>> activities) {

    StringBuilder sb = new StringBuilder();
    try {

      // Build the header.
      String[] header = buildCsvHeader();

      CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT
              .withHeader(header));

      // Print by year.
      for (int yearNo = 0; yearNo <= 13; ++yearNo) {
        List<String> entryList = new LinkedList<>();
        // Put the year in.
        entryList.add(Integer.toString(yearNo));
        // Put in the day dates.
        addDates(entryList);

        // Print Monday = Friday information.
        for (int day = 1; day <= 5; ++day) {
          printDay(activities.get(yearNo).get(day), entryList);
        }

        csvPrinter.printRecord(entryList);
      }

      csvPrinter.flush();

    } catch (IOException ex) {
      Logger.getLogger(ActivityExportGenerator.class.getName()).log(Level.SEVERE, null, ex);
    }

    return sb;
  }

  private Map<Integer, Map<Integer, List<Activity>>> generateActivityInformation() {

    Map<Integer, Map<Integer, List<Activity>>> activityMap = new TreeMap<>();

    // Populate each year & day.
    for (int year = 0; year <= 13; ++year) {
      Map<Integer, List<Activity>> dayMap = new TreeMap<>();

      for (int dayNo = 1; dayNo <= 5; ++dayNo) {
        List<Activity> activityList = new LinkedList<>();
        dayMap.put(dayNo, activityList);
      }

      activityMap.put(year, dayMap);
    }

    // Put only relevant activities in the map - day must be 1 - 5.
    // Activities that are not assigned to a day (resting), will be outside
    // this range.
    // Get activities for a particular time slot.
    for (String period : periodLabels) {
      // Get the number value for this label
      int periodValue = StaticValues.getPeriodIndex(period);
      // Get the activities in this period.
      List<Activity> activities = getActivities(periodValue);

      for (Activity a : activities) {
        if (a.getActivityDay() >= 1 && a.getActivityDay() <= 5) {
          for (int i = 0; i <= 13; ++i) {
            if (i >= a.getMinyear()
                    && i <= a.getMaxyear()) {
              // Can add cos we've already created.
              activityMap.get(i).get(a.getActivityDay()).add(a);
            }
          }
        }
      }
    }
    
    return activityMap;
  }

  private void printDay(List<Activity> activities, List<String> entryList) {
    // Print 10 activities. If there aren't 10 activities, print a null one.
    int activityNo = 0;
    for (Activity a : activities) {
      // Print it
      printActivity(a, entryList);
      ++activityNo;
    }

    for (; activityNo < 10; ++activityNo) {
      printNullActivity(entryList);
    }
  }

  private String[] buildCsvHeader() {
    // Setup from fixed header.
    List<String> headerList = new LinkedList<>(Arrays.asList(csvFixedHeader));
    // Then add day repeating entries.
    for (int dayNo = 1; dayNo <= 5; ++dayNo) {
      for (int entryNo = 1; entryNo <= 10; ++entryNo) {
        // Format the String as "DayName" + string + entryNp
        for (String s : csvRepeatingHeader) {
          String headerEntry = StaticValues.getDayLabel(dayNo)
                  + s + Integer.toString(entryNo);
          headerList.add(headerEntry);
        }
      }
    }

    // Make into an array.
    String[] ret = new String[headerList.size()];
    headerList.toArray(ret);
    return ret;
  }

  private void printActivity(Activity a, List<String> entryList) {
    entryList.add(a.getActivityid().toString());
    entryList.add(StaticValues.getPeriodLabel(a.getPeriod()));
    entryList.add(a.getName());
    entryList.add(a.getVenue());
    if (a.getCost() != null) {
      entryList.add(a.getCost().toString());
    } else {
      entryList.add("");
    }

    entryList.add(a.getDescription());
    entryList.add(a.getNotes());
    if (a.getConsentRequired() != null) {
      entryList.add((a.getConsentRequired().equals(Boolean.TRUE) ? "1" : "0"));
    } else {
      entryList.add("0");
    }
    entryList.add(a.getRestrictions());
  }

  private void printNullActivity(List<String> entryList) {
    for (int i = 0; i < csvRepeatingHeader.length; ++i) {
      entryList.add("");
    }
  }

  private void addDates(List<String> entryList) {
    for (int i = 1; i <= 5; ++i) {
      entryList.add(ywDayBean.getExportDayDate(i));
    }
  }
}
