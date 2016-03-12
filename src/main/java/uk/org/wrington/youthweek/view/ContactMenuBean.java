/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import uk.org.wrington.youthweek.controller.ContactController;
import uk.org.wrington.youthweek.model.Contact;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "contactMenuBean")
@Stateless
public class ContactMenuBean {

  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")
  @ManagedProperty(value = "#{contactController}")
  private ContactController contactController;

  private Contact selectedContact;

  public ContactController getContactController() {
    return contactController;
  }

  public void setContactController(ContactController contactController) {
    this.contactController = contactController;
  }

  // Get the list of contacts.
  public List<Contact> getContacts() {
    return contactController.getItems();
  }

  public void setSelectedContact(Contact c) {
    selectedContact = c;
  }

  public Contact getSelectedContact() {
    return selectedContact;
  }
}
