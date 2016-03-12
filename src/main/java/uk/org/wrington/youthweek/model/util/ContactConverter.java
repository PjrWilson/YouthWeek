/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model.util;

/**
 *
 * @author wilson_pjr
 */
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import uk.org.wrington.youthweek.controller.ContactController;
import uk.org.wrington.youthweek.model.Contact;

@FacesConverter("contactConverter")
public class ContactConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
    if (value != null && value.trim().length() > 0 && !value.equals("Select One")) {
      System.out.println("value = " + value);
      try {
        ContactController contactController = (ContactController) fc.getExternalContext().getSessionMap().get("contactController");
        return contactController.getContact(Integer.parseInt(value));
      } catch (NumberFormatException e) {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid contact."));
      }
    } else {
      return null;
    }
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object object) {
    if (object != null && object instanceof Contact) {
      return ((Contact) object).getContactid().toString();
    } else {
      return null;
    }
  }

}
