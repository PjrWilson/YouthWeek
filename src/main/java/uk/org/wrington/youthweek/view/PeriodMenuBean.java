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
@ManagedBean(name = "periodBean")
@ApplicationScoped
public class PeriodMenuBean {

  public String getPeriodLabel(Integer in) {
    return StaticValues.getPeriodLabel(in);
  }
  
  public List<Integer> getPeriodValues() {
    return StaticValues.getPeriodValues();
  }

}
