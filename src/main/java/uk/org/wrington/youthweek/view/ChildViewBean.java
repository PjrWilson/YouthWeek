/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.SelectEvent;
import uk.org.wrington.youthweek.controller.ChildController;
import uk.org.wrington.youthweek.controller.ContactController;
import uk.org.wrington.youthweek.controller.ExtraItemController;
import uk.org.wrington.youthweek.export.ActivityExportGenerator;
import uk.org.wrington.youthweek.export.ChildExportGenerator;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.ActivityImportDetail;
import uk.org.wrington.youthweek.model.BookingImportDatabase;
import uk.org.wrington.youthweek.model.ChildBookingImport;
import uk.org.wrington.youthweek.model.Child;
import static uk.org.wrington.youthweek.model.Child_.activities;
import uk.org.wrington.youthweek.model.ExtraItem;
import uk.org.wrington.youthweek.model.ExtraItemEntry;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.YwDayBean;

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
  @ManagedProperty(value = "#{ywDayBean}")
  private YwDayBean ywDayBean;

  private Child selected = null;
  private Child created = null;

  private int rows = 25;
  private String sheet;
  @ManagedProperty(value = "#{extraItemController}")
  private ExtraItemController extraItemController;

  public ChildViewBean() {
    this.sheet = "0";
  }
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

  public void setExtraItemController(ExtraItemController eic) {
    this.extraItemController = eic;
  }

  public void setYwDayBean(YwDayBean ywDayBean) {
    this.ywDayBean = ywDayBean;
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

  public void export() throws IOException {
    System.out.println("Export Children");

    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();

    ChildExportGenerator export
            = new ChildExportGenerator(ywDayBean,
                    childController.getFacade());
    StringBuilder sb = export.generate();

    String fileName = export.getFilename();
    int contentLength = sb.length();

    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
    ec.setResponseContentType("text/csv"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
    ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

    OutputStream output = ec.getResponseOutputStream();
    PrintWriter pw = new PrintWriter(output);
    pw.print(sb.toString());
    pw.flush();

    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

    JsfUtil.addSuccessMessage("Exported stuff");

  }

  public void chooseSheet() {
    Map<String, Object> options = new HashMap<>();
    options.put("resizable", false);
    options.put("draggable", false);
    options.put("modal", true);
    PrimeFaces.current().dialog().openDynamic("/children/SelectSheet", options, null);
  }

  public void onSheetChosen(SelectEvent event) {
    System.out.println("Chosen sheet " + event.getObject());
    String sheet = (String) event.getObject();
    int sheetInt = Integer.parseInt(sheet);
    Child child = this.getSelected();
    if (child != null) {
      setActivitiesFromImport(child, sheetInt);
    }

  }

  private void setActivitiesFromImport(Child child, int sheetId) {

    int errors = 0;
    String message = "";

    // Get the import details.
    ChildBookingImport cbi = BookingImportDatabase.getInstance().findBySheetId(sheetId);
    if (cbi != null) {
      List<ActivityImportDetail> bookedActivities = cbi.getBookedActivities();
      // 
      List<ActivityEntry> lae = new LinkedList<>();
      List<ExtraItemEntry> extraItems = new LinkedList<>();

      for (ActivityImportDetail aid : bookedActivities) {
        Activity activity = aid.getActivity();
        if (activity != null) {
          // Create a detail.
          ActivityEntry ae = new ActivityEntry();
          ae.setChild(child);
          ae.setActivity(activity);
          ae.setCanProvideHelp(aid.getEventHelpProvided());
          ae.setConsentFormSigned(aid.getConsentProvided());
          lae.add(ae);
        } else {
          message += "Unable to find Activity Id " + aid.getEventId() + "<br/>";
          errors++;
        }
      }

      List<ExtraItem> bbqItemList = extraItemController.getItems();

      Map<String, Integer> bbqItems = cbi.getBbqItems();
      for (Entry<String, Integer> entry : bbqItems.entrySet()) {
        // Find the bbq item.
        ExtraItem ei = findItem(bbqItemList, entry.getKey());
        if (ei != null) {
          // Create an entry.
          ExtraItemEntry eie = new ExtraItemEntry();
          eie.setChild(child);
          eie.setItem(ei);
          eie.setFreeItem(Boolean.FALSE);
          eie.setItemCount(entry.getValue());
          extraItems.add(eie);
        } else {
          message += "Unable to find BBQ Item with key " + entry.getKey() + "<br/>";
          errors++;
        }
      }

      // Update the values in the child
      child.setActivities(lae);
      child.setExtraItems(extraItems);
      child.setNoAdminFee(!cbi.getRegFeeChargeable());
      childController.update(child);

      if (errors > 0) {
        String dlgMessage = "Errors found: " + errors + "<br/><br/>" + message;
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Result", dlgMessage);
        PrimeFaces.current().dialog().showMessageDynamic(fm);
      }
      
    } else {
      FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Sheet", "Sheet Id "
              + sheetId + " could not be found");
      PrimeFaces.current().dialog().showMessageDynamic(fm);

    }
  }
//  public void selectImportFromDialog(ChildBookingImport impt) {
//    PrimeFaces.current().dialog().closeDynamic(impt);
//  }

  public void selectImportFromDialog() {
    PrimeFaces.current().dialog().closeDynamic(sheet);
  }

  public void setSheet(String sheet) {
    this.sheet = sheet;
  }

  public String getSheet() {
    return sheet;
  }

  private ExtraItem findItem(List<ExtraItem> bbqItemList, String reportKey) {
    for (ExtraItem ei : bbqItemList) {
      if (ei.getReportKey() != null && ei.getReportKey().equals(reportKey)) {
        return ei;
      }
    }
    return null;
  }
}
