package uk.org.wrington.youthweek.view;

/**
 *
 * @author phil
 */
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import uk.org.wrington.youthweek.controller.ActivityController;
import uk.org.wrington.youthweek.model.ChildBookingImport;
import uk.org.wrington.youthweek.imports.ChildBookingImporter;
import uk.org.wrington.youthweek.model.BookingImportDatabase;
import uk.org.wrington.youthweek.model.util.JsfUtil;

@ManagedBean(name = "bookingImportView")
@SessionScoped
public class BookingImportView {

  private ChildBookingImport selected;
  // The controller.
  @ManagedProperty(value = "#{activityController}")
  private ActivityController activityController;

  public void setActivityController(ActivityController ac) {
    this.activityController = ac;
  }

  public ActivityController getActivityController() {
    return activityController;
  }

  public void handleFileUpload(FileUploadEvent event) {
    //JsfUtil.addSuccessMessage("Uploaded " + event.getFile().getFileName());

    ChildBookingImporter importer = new ChildBookingImporter(activityController.getFacade());

    try {
      // Import the thing.
      List<ChildBookingImport> list = importer.readFile(event.getFile().getInputstream());
    //  JsfUtil.addSuccessMessage("Read " + list.size() + " records");
      String dlgMessage = "<br/><br/>Read " + list.size() + " records from " + event.getFile().getFileName() + "<br/><br/>";
      // Add to the internal list.
      int replaced = BookingImportDatabase.getInstance().addImports(list);
      if (replaced > 0) {
        //      JsfUtil.addWarningMessage(replaced + " existing records overwritten");
        dlgMessage += replaced + " duplicated records were found and overwritten";
      }
      
      List<String> errors = importer.getErrors();
      if (errors.size() > 0) {
        dlgMessage += "<br/><br/>Errors were found:<br/><br/>";
        for (String e : errors) {
          dlgMessage += e + "<br/>";
        }
      }
      FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result", dlgMessage);
      PrimeFaces.current().dialog().showMessageDynamic(fm);
    } catch (Exception ex) {
      Logger.getLogger(BookingImportView.class.getName()).log(Level.SEVERE, null, ex);
      //JsfUtil.addErrorMessage(ex, "Error reading " + event.getFile().getFileName());
      FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Result", 
              "Error reading " + event.getFile().getFileName() + "<br/><br/>" +
                      ex.getMessage());
      PrimeFaces.current().dialog().showMessageDynamic(fm);
    }
  }

  public List<ChildBookingImport> getImportedBookings() {
    return new LinkedList(BookingImportDatabase.getInstance().getImports());
  }

  public void clear() {
    BookingImportDatabase.getInstance().clear();
  }

  public ChildBookingImport getSelected() {
    return selected;
  }

  public void setSelected(ChildBookingImport selected) {
    this.selected = selected;
  }

  public void deleteSelected() {
    if (selected != null) {
      BookingImportDatabase.getInstance().getImports().remove(selected);
      selected = null;
      JsfUtil.addSuccessMessage("Deleted record");
    }
  }

  public void deleteAll() {
    if (BookingImportDatabase.getInstance().getImports().size() > 0) {
      BookingImportDatabase.getInstance().clear();
      selected = null;
      JsfUtil.addSuccessMessage("Deleted all records");
    }
  }

  public void refresh() {

  }
}
