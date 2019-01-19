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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.Contact;
import uk.org.wrington.youthweek.model.util.DateHelper;
import uk.org.wrington.youthweek.model.util.YwDayBean;

/**
 *
 * @author phil
 */
public class ChildExportGenerator {

  YwDayBean ywDayBean;
  uk.org.wrington.youthweek.model.ChildFacade ejbFacade;

  static String[] csvFixedHeader
          = {
            "Child's Year",
            "Child's First Name",
            "Child's Surname",
            "Child's Date of Birth",
            "Child's Medical Conditions",
            "Child's Special Needs",
            "Parent's First Name",
            "Parent's Surname",
            "Parent's Address",
            "Parent's Email",
            "Parent's Phone Number",
            "Parent's Emergency Phone Number"
          };

  public static String FILENAME = "ChildExport-%s.csv";

  public ChildExportGenerator(YwDayBean ywDayBean,
          uk.org.wrington.youthweek.model.ChildFacade ejbFacade) {
    this.ywDayBean = ywDayBean;
    this.ejbFacade = ejbFacade;
  }

  public String getFilename() {
    return String.format(FILENAME, ywDayBean.getYear());
  }

  public StringBuilder generate() {
    return writeCsv(generateChildInformation());
  }

  private List<Child> getChildren() {
    return createQuery().getResultList();
  }

  protected Query createQuery() {
    return ejbFacade.getEntityManager().createNamedQuery("Child.findAllSorted");
  }

  private StringBuilder writeCsv(List<Child> children) {

    StringBuilder sb = new StringBuilder();
    try {

      // Build the header.
      String[] header = buildCsvHeader();

      CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT
              .withHeader(header));

      // Print all.
      for (Child c : children) {
        int childYear = ywDayBean.getYearForChild(c);
        if (childYear >= 0 && childYear <= 13) {
          List<String> entryList = new LinkedList<>();
          entryList.add(Integer.toString(childYear));
          printChild(c, entryList);
          csvPrinter.printRecord(entryList);
        }
      }
      
      csvPrinter.flush();

    } catch (IOException ex) {
      Logger.getLogger(ChildExportGenerator.class.getName()).log(Level.SEVERE, null, ex);
    }

    return sb;
  }

  private List<Child> generateChildInformation() {

    List<Child> children = getChildren();
    // Possibly filter out if school years are outside a certain limit
    
    return children;
  }

  private String[] buildCsvHeader() {
    // Setup from fixed header.
    List<String> headerList = new LinkedList<>(Arrays.asList(csvFixedHeader));

    // Make into an array.
    String[] ret = new String[headerList.size()];
    headerList.toArray(ret);
    return ret;
  }

  private void printChild(Child c, List<String> entryList) {
    entryList.add(c.getFirstname());
    entryList.add(c.getSurname());
    entryList.add(DateHelper.getInstance().formatDate("dd/MM/YYYY", c.getDateOfBirth()));
    entryList.add(c.getMedicalInfo());
    entryList.add(c.getSpecialNeeds());
    Contact parent = c.getParentContact();
    if (parent != null) {
      entryList.add(parent.getFirstname());
      entryList.add(parent.getSurname());
      entryList.add(parent.getAddress());
      entryList.add(parent.getEmail());
      entryList.add(parent.getPhone());
      entryList.add(parent.getEmergencyPhone());
    }
    else {
      for (int i = 0; i < 6; ++i) {
        entryList.add("");
      }
    }
  }
}
