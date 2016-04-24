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
import uk.org.wrington.youthweek.model.Contact;
import uk.org.wrington.youthweek.model.ContactFacade;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.model.util.JsfUtil.PersistAction;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "contactController")
@SessionScoped
public class ContactController implements Serializable {

  @EJB
  private uk.org.wrington.youthweek.model.ContactFacade ejbFacade;
  private List<Contact> items = null;
  private List<Contact> filteredContacts = null;

  private Contact selected;
  private Contact created;

  public ContactController() {
    System.out.println("ContactController");
  }

  public Contact getSelected() {
    return selected;
  }

  public void setSelected(Contact selected) {
    this.selected = selected;
  }

  public Contact getCreated() {
    return created;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private ContactFacade getFacade() {
    return ejbFacade;
  }

  public Contact prepareCreate() {
    created = new Contact();
    initializeEmbeddableKey();
    return created;
  }

  public void create() {
    // Swap selected and created.
    Contact oldSelection = selected;
    selected = created;
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ContactCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
    selected = oldSelection;
    created = null;
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ContactUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ContactDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Contact> getItems() {
    if (items == null) {
      System.out.println("**** ContactController::findAll");
      items = getFacade().findAll();
    }
    return items;
  }

  public void refreshItems() {
    items = null;
  }

  private void persist(PersistAction persistAction, String successMessage) {
    if (selected != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          if (persistAction == PersistAction.CREATE) {
            getFacade().create(selected);
          } else {
            Contact update = getFacade().edit(selected);
            int index = items.indexOf(selected);
            if (index > -1) {
              items.set(index, update);
            }
          }
        } else {
          getFacade().remove(selected);
        }
        System.out.println("Key set to " + selected.getContactid());
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

  public Contact getContact(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Contact> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Contact> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Contact.class)
  public static class ContactControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      ContactController controller = (ContactController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "contactController");
      return controller.getContact(getKey(value));
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
      if (object instanceof Contact) {
        Contact o = (Contact) object;
        return getStringKey(o.getContactid());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Contact.class.getName()});
        return null;
      }
    }

  }

  public List<Contact> getFilteredContacts() {
    return filteredContacts;
  }

  public void setFilteredContacts(List<Contact> filteredContacts) {
    this.filteredContacts = filteredContacts;
  }

  public List<Contact> completeContact(String query) {
    List<Contact> allContacts = getItems();
    List<Contact> retContacts = new ArrayList<>();

    String queryLower = query.toLowerCase();
    for (Contact contact : allContacts) {
      if (contact.toString().toLowerCase().contains(queryLower)) {
        retContacts.add(contact);
      }
    }

    return retContacts;
  }

  public List<Contact> completeCommitteeContact(String query) {
    List<Contact> allContacts = getItems();
    List<Contact> retContacts = new ArrayList<>();

    String queryLower = query.toLowerCase();
    for (Contact contact : allContacts) {
      if (contact.toString().toLowerCase().contains(queryLower)
              && contact.getCommittee() != null && contact.getCommittee().equals(Boolean.TRUE)) {
        retContacts.add(contact);
      }
    }

    return retContacts;
  }

  public void toggleCommittee() {
    String message = "Added " + selected.getFirstname() + " to Committee";
    if (selected.getCommittee()) {
      message = "Removed " + selected.getFirstname() + " from Committee";
    }
    selected.setCommittee(!selected.getCommittee());
    persist(PersistAction.UPDATE, message);
//    JsfUtil.addSuccessMessage("Changed Committee Status for " + selected.getFirstname());
  }
}
