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
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import uk.org.wrington.youthweek.app.StaticValues;

@FacesConverter("periodConverter")
public class PeriodConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
    int index = 0;
    if (value != null && value.trim().length() > 0) {
 //     System.out.println("value = " + value);
      List<String> values = StaticValues.getPeriodLabels();
      for (String s : values) {
        if (s.equals(value)) {
          return index; 
        }
        ++index;
      }
    }
    return -1;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object object) {
//    System.out.println("valuein = " + object);
    if (object != null && object instanceof String) {
      return StaticValues.getPeriodLabel((Integer)object);
    } else {
      return null;
    }
  }

 }
