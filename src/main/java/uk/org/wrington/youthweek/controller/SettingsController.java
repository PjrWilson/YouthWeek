/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.controller;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.SettingEntry;
import uk.org.wrington.youthweek.model.SettingsFacade;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.JsfUtil.PersistAction;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "settingsController")
@ApplicationScoped
public class SettingsController implements Serializable {

  @EJB
  private uk.org.wrington.youthweek.model.SettingsFacade ejbFacade;
  private List<SettingEntry> items = null;

  public SettingsController() {

  }

//  public EventType getSelected() {
//    return selected;
//  }
//
//  public EventType getCreated() {
//    return created;
//  }
//
//  public void setSelected(EventType selected) {
//    this.selected = selected;
////    if (selected != null) {
////      eventOccurrenceController.getOccurrencesForType(selected);
////    }
//  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private SettingsFacade getFacade() {
    return ejbFacade;
  }

//  public EventType prepareCreate() {
//    created = new EventType();
//    initializeEmbeddableKey();
//    return created;
//  }

  public void create(SettingEntry newSettings) {
    System.out.println("CREATE");
    persist(PersistAction.CREATE, newSettings);
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update(SettingEntry settings) {
    persist(PersistAction.UPDATE, settings);
  }

  public void destroy(SettingEntry settings) {
    System.out.println("DESTROY");
    persist(PersistAction.DELETE, settings);
    if (!JsfUtil.isValidationFailed()) {
     // selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<SettingEntry> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  private void persist(PersistAction persistAction, SettingEntry settings) {
     System.out.println("PERSIST");
   if (settings != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          getFacade().edit(settings);
        } else {
          getFacade().remove(settings);
        }
        //JsfUtil.addSuccessMessage(successMessage);
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
  }

  public SettingEntry getSetting(java.lang.String id) {
    return getFacade().find(id);
  }
  
  public List<SettingEntry> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<SettingEntry> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = SettingEntry.class)
  public static class SettingsControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      SettingsController controller = (SettingsController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "settingsController");
      return controller.getSetting(value);
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
      if (object instanceof SettingEntry) {
        SettingEntry o = (SettingEntry) object;
        return o.getKey();
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Activity.class.getName()});
        return null;
      }
    }
  }
}
