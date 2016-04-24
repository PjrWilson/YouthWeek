/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import uk.org.wrington.youthweek.app.StaticValues;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "schoolYearBean")
@ApplicationScoped
public class SchoolYearMenuBean {

  public List<Integer> getMinSchoolYears() {
    return StaticValues.getMinSchoolYears();
  }

  public List<Integer> getMaxSchoolYears() {
    return StaticValues.getMaxSchoolYears();
  }

  public String getMinSchoolYearLabel(Integer in) {
    return StaticValues.getMenuLabel(in);
  }

  public String getMaxSchoolYearLabel(Integer in) {
    return StaticValues.getMenuLabel(in);
  }

  public String getSchoolYearLabel(Integer in) {
    return StaticValues.getYearLabel(in);
  }

}
