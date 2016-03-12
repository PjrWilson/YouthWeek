/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityFacade;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.JsfUtil.PersistAction;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "activityController")
@SessionScoped
public class ActivityController implements Serializable {

  @EJB
  private uk.org.wrington.youthweek.model.ActivityFacade ejbFacade;
  @EJB
  private uk.org.wrington.youthweek.model.ActivityEntryFacade aeFacade;
  private List<Activity> items = null;


//  @ManagedProperty(value = "#{eventOccurrenceController}")
//  private EventOccurrenceController eventOccurrenceController;
  public ActivityController() {

  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private ActivityFacade getFacade() {
    return ejbFacade;
  }

  public void create(Activity newActivity) {
    System.out.println("CREATE");
    persist(PersistAction.CREATE, newActivity, ResourceBundle.getBundle("/Bundle").getString("EventTypeCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update(Activity activity) {
    Activity ret = persist(PersistAction.UPDATE, activity, ResourceBundle.getBundle("/Bundle").getString("EventTypeUpdated"));
    if (ret != null && items != null) {
      int index = items.indexOf(activity);
      if (index > -1) {
        items.set(index, ret);
      }
    }
  }

  public void destroy(Activity activity) {
    System.out.println("DESTROY");
    persist(PersistAction.DELETE, activity, ResourceBundle.getBundle("/Bundle").getString("EventTypeDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Activity> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  public void refreshItems() {
    items = null;
  }

  public List<Activity> getItemsForSchoolYear(Integer year) {
    //List<Activity> allActivities = getItems();
    List<Activity> allActivities = getFacade().getActivitiesForSchoolYear(year);
    List<Activity> ret = new ArrayList<>();
    for (Activity a : allActivities) {
      if (year.compareTo(a.getMinyear()) >= 0
              && year.compareTo(a.getMaxyear()) <= 0) {
        ret.add(a);
      }
    }
    return ret;
  }

  public long countEntriesFor(Activity a) {
    return aeFacade.countEntriesFor(a);
  }

  private Activity persist(PersistAction persistAction, Activity activity, String successMessage) {
    System.out.println("PERSIST");
    Activity ret = null;
    if (activity != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          ret = getFacade().edit(activity);
        } else {
          getFacade().remove(activity);
        }
        JsfUtil.addSuccessMessage(successMessage);
      } catch (EJBException ex) {
        String msg = "";
        Throwable cause = ex.getCause();
        if (cause != null) {
          msg = cause.getLocalizedMessage();
        }
        if (msg.length() > 0) {
          JsfUtil.addErrorMessage(msg);
        } else {
          JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
      } catch (Exception ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
      }
    }
    return ret;
  }

  public Activity getActivity(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Activity> getActivitiesForDay(java.lang.Integer dayNo) {
//    List<Activity> retList = new LinkedList<>();
//    List<Activity> all = getItems();
//    for (Activity a : all) {
//      if (a.getActivityDay().equals(dayNo)) {
//        retList.add(a);
//      }
//    }
//    return retList;
    return getFacade().getActivitiesForDay(dayNo);
  }

  public List<Activity> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Activity> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Activity.class)
  public static class ActivityControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      ActivityController controller = (ActivityController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "activityController");
      return controller.getActivity(getKey(value));
    }

    java.lang.Integer getKey(String value) {
      java.lang.Integer key;
      key = Integer.valueOf(value);
      return key;
    }

    String getStringKey(java.lang.Integer value) {
      StringBuilder sb = new StringBuilder();
      sb.append(value);
      return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
      if (object == null) {
        return null;
      }
      if (object instanceof Activity) {
        Activity o = (Activity) object;
        return getStringKey(o.getActivityid());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Activity.class.getName()});
        return null;
      }
    }

  }

}
