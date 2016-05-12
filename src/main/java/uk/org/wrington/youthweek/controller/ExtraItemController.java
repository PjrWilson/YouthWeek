/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.controller;

import java.io.Serializable;
import java.text.MessageFormat;
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
import uk.org.wrington.youthweek.model.ExtraItem;
import uk.org.wrington.youthweek.model.ExtraItemFacade;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.JsfUtil.PersistAction;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "extraItemController")
@SessionScoped
public class ExtraItemController implements Serializable {

  @EJB
  private uk.org.wrington.youthweek.model.ExtraItemFacade ejbFacade;
  private List<ExtraItem> items = null;

  public ExtraItemController() {

  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private ExtraItemFacade getFacade() {
    return ejbFacade;
  }

  public void create(ExtraItem newItem) {
    System.out.println("CREATE");
    persist(PersistAction.CREATE, newItem, 
            MessageFormat.format(
                    ResourceBundle.getBundle("/Bundle").getString("ExtraItemCreated"),
                    newItem.getName()));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update(ExtraItem updateItem) {
    persist(PersistAction.UPDATE, updateItem, 
            MessageFormat.format(
                    ResourceBundle.getBundle("/Bundle").getString("ExtraItemUpdated"),
                    updateItem.getName()));
    items = null;
  }

  public void destroy(ExtraItem destroyItem) {
    System.out.println("DESTROY");
    persist(PersistAction.DELETE, destroyItem, 
            MessageFormat.format(
                    ResourceBundle.getBundle("/Bundle").getString("ExtraItemDeleted"),
                    destroyItem.getName()));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<ExtraItem> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  private void persist(PersistAction persistAction, ExtraItem item, String successMessage) {
     System.out.println("PERSIST");
   if (item != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          if (persistAction == PersistAction.CREATE) {
            getFacade().create(item);
          } else {
            getFacade().edit(item);
          }
        } else {
          getFacade().remove(item);
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
  }

  public ExtraItem getItem(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<ExtraItem> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<ExtraItem> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = ExtraItem.class)
  public static class ExtraItemControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      ExtraItemController controller = (ExtraItemController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "extraItemController");
      return controller.getItem(getKey(value));
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
      if (object instanceof ExtraItem) {
        ExtraItem o = (ExtraItem) object;
        return getStringKey(o.getItemid());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Activity.class.getName()});
        return null;
      }
    }

  }

}
