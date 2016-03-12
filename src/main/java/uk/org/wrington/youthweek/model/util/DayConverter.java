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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import uk.org.wrington.youthweek.app.StaticValues;

@FacesConverter("dayConverter")
public class DayConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
    if (value != null && value.trim().length() > 0) {
 //     System.out.println("value = " + value);
      switch (value) {
        case "Monday":
          return 1;
        case "Tuesday":
          return 2;
        case "Wednesday":
          return 3;
        case "Thursday":
          return 4;
        case "Friday":
          return 5;
        default:
          return Integer.parseInt(value);
      }
    }
    return null;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object object) {
//    System.out.println("valuein = " + object);
    if (object != null && object instanceof Integer) {
      return StaticValues.getDayLabel((Integer)object);
    } else {
      return null;
    }
  }

 }
