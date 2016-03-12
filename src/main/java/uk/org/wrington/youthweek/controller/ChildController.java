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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.ChildFacade;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.JsfUtil.PersistAction;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "childController")
@SessionScoped
public class ChildController implements Serializable {

  @EJB
  private uk.org.wrington.youthweek.model.ChildFacade ejbFacade;
  private List<Child> items = null;
 /// private EventType selected;
//  private EventType created;

//  @ManagedProperty(value = "#{eventOccurrenceController}")
//  private EventOccurrenceController eventOccurrenceController;
  public ChildController() {

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

  private ChildFacade getFacade() {
    return ejbFacade;
  }

//  public EventType prepareCreate() {
//    created = new EventType();
//    initializeEmbeddableKey();
//    return created;
//  }
  public Child create(Child newChild) {
    System.out.println("CREATE");
    Child ret = persist(PersistAction.CREATE, newChild, ResourceBundle.getBundle("/Bundle").getString("ChildCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
    return ret;
  }

  public void update(Child child) {
    Child ret = persist(PersistAction.UPDATE, child, ResourceBundle.getBundle("/Bundle").getString("ChildUpdated"));
    if (ret != null) {
      int index = items.indexOf(child);
      if (index > -1) {
        items.set(index, ret);
      }
    }
  }

  public void destroy(Child child) {
    System.out.println("DESTROY");
    persist(PersistAction.DELETE, child, ResourceBundle.getBundle("/Bundle").getString("ChildDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      // selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Child> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  public void refreshItems() {
    items = null;
  }
  
  private Child persist(PersistAction persistAction, Child child, String successMessage) {
    System.out.println("PERSIST");
    Child ret = null;
    if (child != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          ret = getFacade().edit(child);
        } else {
          getFacade().remove(child);
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

  public Child getChild(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Child> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Child> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Child.class)
  public static class ChildControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      ChildController controller = (ChildController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "childController");
      return controller.getChild(getKey(value));
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
      if (object instanceof Child) {
        Child o = (Child) object;
        return getStringKey(o.getChildid());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Activity.class.getName()});
        return null;
      }
    }

  }

}
