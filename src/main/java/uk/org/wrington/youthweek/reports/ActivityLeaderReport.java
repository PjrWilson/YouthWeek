/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.ActivityEntry;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "activityLeaderReport")
@ViewScoped
public class ActivityLeaderReport implements Serializable {


  @EJB
  private uk.org.wrington.youthweek.model.ActivityFacade ejbFacade;
  @EJB
  private uk.org.wrington.youthweek.model.ActivityEntryFacade ejbFacadeEntry;

  private List<Activity> activityList;

  private String searchActivityName;
  boolean searchMonday = true;
  boolean searchTuesday = true;
  boolean searchWednesday = true;
  boolean searchThursday = true;
  boolean searchFriday = true;
  boolean orderByOrganiser = false;

  /**
   * Creates a new instance of ActivityIncomeReport
   */
  public ActivityLeaderReport() {
  }

  public void init() {
    String queryString = "SELECT e FROM Activity e " + buildWhereClause() + " ORDER BY ";
    if (orderByOrganiser) {
      queryString += "e.committeeMember.surname ASC, e.committeeMember.firstname ASC, ";
    }
    queryString += " e.activityday ASC, e.startTime ASC";
//    System.out.println(queryString);
    activityList = ejbFacade.getEntityManager().createQuery(queryString).getResultList();
  }

  public List<Activity> getActivityList() {
    return activityList;
  }

  private String buildWhereClause() {
    String clause = new String();
    if (searchActivityName != null && searchActivityName.length() > 0) {
      clause = addClause("UPPER(e.name) LIKE '" + searchActivityName.toUpperCase() + "'", clause);
    }

    if (searchMonday || searchTuesday || searchWednesday || searchThursday || searchFriday) {

      String dayNo = new String();

      if (searchMonday) {
        dayNo = addDayNo("1", dayNo);
      }
      if (searchTuesday) {
        dayNo = addDayNo("2", dayNo);
      }
      if (searchWednesday) {
        dayNo = addDayNo("3", dayNo);
      }
      if (searchThursday) {
        dayNo = addDayNo("4", dayNo);
      }
      if (searchFriday) {
        dayNo = addDayNo("5", dayNo);
      }

      clause = addClause("e.activityday IN (" + dayNo + ")", clause);
    }

    if (clause.length() > 0) {
      clause = "WHERE " + clause;
    }
    return clause;
  }

  private String addDayNo(String day, String clause) {
    if (clause.length() > 0) {
      clause += ",";
    }
    clause += day;
    return clause;
  }

  private String addClause(String s, String clause) {
    String ret = clause;
    if (clause != null && clause.length() > 0) {
      ret += " AND ";
    }
    ret += s;
    return ret;
  }

  public long getSize(Activity a) {
    return (long) ejbFacadeEntry.getEntityManager().
            createNamedQuery("ActivityEntry.countEntriesForActivity").
            setParameter("id", a.getActivityid()).getSingleResult();
  }

  public List<ActivityEntry> getEntries(Activity a) {
    return ejbFacadeEntry.getEntityManager().
            createNamedQuery("ActivityEntry.getEntriesForActivity").
            setParameter("id", a.getActivityid()).getResultList();
  }

  public String formatName(Activity a) {
    String s = a.getName();
    if (a.getVenue().length() > 0) {
      s += ", " + a.getVenue();
    }
    return s;
  }

  public void setSearchActivityName(String s) {
    searchActivityName = s;
  }

  public String getSearchActivityName() {
    return searchActivityName;
  }

  public void setSearchMonday(boolean b) {
    searchMonday = b;
  }

  public boolean getSearchMonday() {
    return searchMonday;
  }

  public void setSearchTuesday(boolean b) {
    searchTuesday = b;
  }

  public boolean getSearchTuesday() {
    return searchTuesday;
  }

  public void setSearchWednesday(boolean b) {
    searchWednesday = b;
  }

  public boolean getSearchWednesday() {
    return searchWednesday;
  }

  public void setSearchThursday(boolean b) {
    searchThursday = b;
  }

  public boolean getSearchThursday() {
    return searchThursday;
  }

  public void setSearchFriday(boolean b) {
    searchFriday = b;
  }

  public boolean getSearchFriday() {
    return searchFriday;
  }

  public void setOrderByOrganiser(boolean b) {
    orderByOrganiser = b;
  }

  public boolean getOrderByOrganiser() {
    return orderByOrganiser;
  }
}
