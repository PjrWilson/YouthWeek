/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.persistence.Query;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "activitiesTransportReport")
@ViewScoped
public class ActivitiesTransportReport extends ActivityIncomeReport {

  /**
   * Creates a new instance of ActivityIncomeReport
   */
  public ActivitiesTransportReport() {
  }

  @Override
  protected Query createQuery() {
   // System.out.println("createQuery for transport report");
    return ejbFacade.getEntityManager().createNamedQuery("Activity.withTransport");
  }
}
