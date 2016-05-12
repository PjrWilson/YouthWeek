/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model.util;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.Contact;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "ywNameHelper")
@SessionScoped
public class YwNameHelper implements Serializable {

  @ManagedProperty(value = "#{settings}")
  private Settings settings;

  /**
   * Creates a new instance of YwDayBean
   */
  public YwNameHelper() {
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  public String formatContactName(Contact c) {
    if (c != null) {
      return c.getFirstname() + " " + c.getSurname();
    }
    return "";
  }

  public String formatContactNameWithEmergencyTel(Contact c) {
    String s = formatContactName(c);
    if (s.length() > 0 && c.getEmergencyPhone().length() > 0) {
      s += " " + c.getEmergencyPhone();
    }
    return s;
  }

  public String formatContactName(Contact c, boolean includeTel) {
    String s = formatContactName(c);
    if (includeTel && s.length() > 0 && c.getPhone().length() > 0) {
      s += " " + c.getPhone();
    }
    return s;
  }

  public String formatContactName(Contact c, boolean includeTel, boolean includeEmail) {
    String s = formatContactName(c, includeTel);
    if (includeEmail && s.length() > 0 && c.getEmail().length() > 0) {
      s += " email:" + c.getEmail();
    }
    return s;
  }

  public String formatActivityNameWithVenue(Activity a) {
    String s = a.getName();
    if (a.getVenue().length() > 0) {
      s += ", " + a.getVenue();
    }
    return s;
  }

}
