/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.imports;

import uk.org.wrington.youthweek.model.ChildBookingImport;
import uk.org.wrington.youthweek.model.ActivityImportDetail;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityFacade;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.util.YwDayBean;

/**
 *
 * @author phil
 */
public class ChildBookingImporter {

  private class SheetException extends Exception {

    public SheetException(String msg) {
      super(msg);
    }
  };

  private final ActivityFacade activityFacade;
  private static final String FILE_NAME = "File name";
  private static final String BBQ_FREE_PIG = "BBQ.CommitteePigRoast";
  private static final String BBQ_HOT_DOG = "BBQ.HotDog";
  private static final String BBQ_PIG = "BBQ.PigRoast";
  private static final String BBQ_VEG_HOT_DOG = "BBQ.VeggieHotDog";

  private List<String> errors = new LinkedList<>();
          
//  static String[] csvFixedHeader
//          = {
//            "File name",
//            BBQ_FREE_PIG,
//            BBQ_HOT_DOG,
//            BBQ_PIG,
//            "BBQ.SheetId",
//            BBQ_VEG_HOT_DOG
//          };
  // BBQ Items.
  static final String[] bbqItems
          = {
            BBQ_FREE_PIG, BBQ_HOT_DOG, BBQ_PIG, BBQ_VEG_HOT_DOG
          };

  static int EVENT_CONSENT = 0;
  static int EVENT_HELP = 1;
  static int EVENT_ID = 2;
  static int EVENT_SELECTED = 4;
  static int EVENT_SHEET = 5;

  static String[] csvRepeatingHeader
          = {
            ".EventConsent",
            ".EventHelp",
            ".EventId",
            ".EventRestrictions",
            ".EventSelected",
            ".SheetId"
          };

  static String REG_FEE_CHARGEABLE = "RegistrationFee.Chargeable";
  // static String REG_FEE_SHEET = "RegistrationFee.SheetId";
  static String SHHET_ID = "Sheet.Id";

  public ChildBookingImporter(ActivityFacade facade) {
    this.activityFacade = facade;
  }

  public List<ChildBookingImport> readFile(InputStream is) throws IOException {

    Reader in = new InputStreamReader(is);
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.
            withFirstRecordAsHeader().withDelimiter(';').parse(in);

    List<ChildBookingImport> retList = new LinkedList<>();

    int rowNo = 0;
    
    for (CSVRecord record : records) {

      ++rowNo;
      
      try {
        ChildBookingImport cbi = new ChildBookingImport();

        // Get the registration fee. True if not specified,
        boolean regFeeChargeable = true;
        String value = record.get(REG_FEE_CHARGEABLE);
        if (value != null && value.length() > 0) {
          regFeeChargeable
                  = Boolean.parseBoolean(
                          value);
        }
        cbi.setRegFeeChargeable(regFeeChargeable);

        // And the sheet - must be set.
        value = record.get(SHHET_ID);
        if (value != null && value.length() > 0) {
          cbi.setSheetId(
                  Integer.parseInt(
                          value));
        } else {
          // Not a good sheet.
     //     value = record.get(FILE_NAME);
          throw new SheetException("Entry at row " + rowNo + " has no Sheet Id.");
        }

        // Read the bbq items
        for (String s : bbqItems) {
          // See if its something.
          value = record.get(s);
          if (value != null && value.length() > 0) {
            // Parse it
            cbi.addBbqItem(s, Integer.parseInt(value));
          }
        }

        // Process the activity data. Find entries for each day.
        for (int dayNo = 1; dayNo <= 5; ++dayNo) {
          for (int entryNo = 1; entryNo <= 10; ++entryNo) {
            // Format the String to get the index into the records.
            value = getEntry(dayNo, entryNo, EVENT_SELECTED, record);
            if (value.length() > 0) {

              // Try to resolve from the database
              // Use this event.
              ActivityImportDetail detail
                      = new ActivityImportDetail(
                              trueIfNotEmpty(getEntry(dayNo, entryNo, EVENT_CONSENT, record)),
                              trueIfNotEmpty(getEntry(dayNo, entryNo, EVENT_HELP, record)),
                              getInteger(getEntry(dayNo, entryNo, EVENT_ID, record)));

              detail.setActivity(getActivity(detail.getEventId()));

              cbi.add(detail);
            }
          }
        }

        // Add to the returned list.
        retList.add(cbi);
      }
      catch (SheetException e) {
        errors.add(e.getMessage());
      }
    }
    return retList;
  }

  public List<String> getErrors() {
    return errors;
  }
  
  private Activity getActivity(int activityId) {
    List<Activity> activity = activityFacade.getEntityManager().
            createNamedQuery("Activity.findByActivityid").
            setParameter("activityid", activityId).getResultList();
    // Should be one only.
    if (activity != null && activity.size() == 1) {
      return activity.get(0);
    }
    return null;
  }

  /**
   * Check whether an entry is true. This is for those that are signified by a
   * non-empty value in a column
   *
   * @param s
   * @return
   */
  private boolean trueIfNotEmpty(String s) {
    return s.length() > 0;
  }

  private int getInteger(String s) {
    return Integer.parseInt(s);
  }

  private String getEntry(int dayNo, int entryNo, int headerIndex, CSVRecord record) {
    String key = makeFixedHeader(dayNo, entryNo,
            csvRepeatingHeader[headerIndex]);
    return record.get(key);
  }

//  private String[] buildCsvHeader() {
//    // Setup from fixed header.
//    List<String> headerList = new LinkedList<>(Arrays.asList(csvFixedHeader));
//    // Then add day repeating entries.
//    for (int dayNo = 1; dayNo <= 5; ++dayNo) {
//      // Add the Registgration bits before day3
//      if (dayNo == 3) {
//        headerList.add(REG_FEE_CHARGEABLE);
//        headerList.add(REG_FEE_SHEET);
//      }
//      for (int entryNo = 1; entryNo <= 10; ++entryNo) {
//        // Format the String as "DayName" + string + entryNp
//        for (String s : csvRepeatingHeader) {
//          headerList.add(makeFixedHeader(dayNo, entryNo, s));
//        }
//      }
//    }
//
//    // Make into an array.
//    String[] ret = new String[headerList.size()];
//    headerList.toArray(ret);
//    return ret;
//  }
  private static final String[] alphaDayNames = {
    "Friday",
    "Monday",
    "Thursday",
    "Tuesday",
    "Wednesday"
  };

  private String getDayLabel(int dayNo) {
    return alphaDayNames[dayNo - 1];
  }

  private String makeFixedHeader(int dayNo, int entryNo, String header) {
    return getDayLabel(dayNo)
            + Integer.toString(entryNo) + header;
  }

}
