/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wilson_pjr
 */
@Stateless
public class ActivityFacade extends AbstractFacade<Activity> {

  @PersistenceContext(unitName = "uk.org.wrington_YouthWeek")
  private EntityManager em;

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  public ActivityFacade() {
    super(Activity.class);
  }

  public List<Activity> getActivitiesForSchoolYear(int year) {
    return getEntityManager().createNamedQuery("Activity.findForYearSorted").
            setParameter("year", year).getResultList();
  }

  public List<Activity> getActivitiesForDay(int day) {
    return getEntityManager().createNamedQuery("Activity.findByDay").
            setParameter("activityday", day).getResultList();
  }

}
