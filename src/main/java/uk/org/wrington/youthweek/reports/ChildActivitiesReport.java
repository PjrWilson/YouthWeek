/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.ExtraItemEntry;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "childActivitiesReport")
@ViewScoped
public class ChildActivitiesReport implements Serializable {

  public class ChildContainer implements Comparable<ChildContainer> {

    private final Child child;
    private double activitiesTotal;
    private double extrasTotal;

    public ChildContainer(Child child) {
      this.extrasTotal = 0;
      this.activitiesTotal = 0;
      this.child = child;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof ChildContainer) {
        return compareTo((ChildContainer) o) == 0;
      }
      return false;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 59 * hash + Objects.hashCode(this.child);
      return hash;
    }

    @Override
    public int compareTo(ChildContainer o) {
      // Compare by surname.
      int ret = child.getSurname().compareTo(o.getChild().getSurname());
      if (ret == 0) {
        // Surname is the same, just by firstname.
        ret = child.getFirstname().compareTo(o.getChild().getFirstname());
        // If still 0, use the id.
        if (ret == 0) {
          ret = child.getChildid().compareTo(o.getChild().getChildid());
        }
      }
      return ret;
    }

    public int countActivities(int dayNo) {
      // See how many there are where day is dayNo.
      int count = 0;
      for (ActivityEntry ae : child.getActivities()) {
        if (ae.getActivity().getActivityDay().equals(dayNo)) {
          ++count;
        }
      }
      return count;
    }

    public List<ActivityEntry> getActivities(int dayNo) {
      // Sort by start time.
      List<ActivityEntry> ret = new ArrayList<>();
      for (ActivityEntry ae : child.getActivities()) {
        if (ae.getActivity().getActivityDay().equals(dayNo)) {
          ret.add(ae);
        }
      }
      Collections.sort(ret, new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
          ActivityEntry ae1 = (ActivityEntry) o1;
          ActivityEntry ae2 = (ActivityEntry) o2;
          
          // Times might not be set.
          if (ae1.getActivity().getStartTime() != null &&
                  ae2.getActivity().getStartTime() != null) {
            return ae1.getActivity().getStartTime().compareTo(ae2.getActivity().getStartTime());
          }
          
          if (ae1.getActivity().getStartTime() != null) {
            return -1;
          }
          
          if (ae2.getActivity().getStartTime() != null) {
            return 1;
          }
          
          return 0;
        }
      });

      return ret;
    }

    public List<ExtraItemEntry> getExtras() {
      return child.getExtraItems();
    }

    public Child getChild() {
      return child;
    }

    public double getActivitiesTotal() {
      if (activitiesTotal == 0) {
        for (ActivityEntry ae : child.getActivities()) {
          activitiesTotal += ae.getActivity().getCost();
        }
      }
      return activitiesTotal;
    }

    public double getExtrasTotal() {
      if (extrasTotal == 0) {
        for (ExtraItemEntry eie : getExtras()) {
          extrasTotal += eie.getCost();
        }
      }
      return extrasTotal;
    }

    public double getChildTotal() {
      return getActivitiesTotal() + getChildAdminFee() + getExtrasTotal();
    }

    public double getChildAdminFee() {
      double fee = settings.getAdminFee();
      if (child.getNoAdminFee()) {
        fee = 0;
      }
      return fee;
    }

  }

  @ManagedProperty(value = "#{settings}")
  private Settings settings;

  @EJB
  private uk.org.wrington.youthweek.model.ChildFacade ejbFacade;

  private List<ChildContainer> childList;

  private String searchSurname;
  private String searchFirstname;
  boolean searchKS1 = true;
  boolean searchKS2 = true;
  boolean searchKS3 = true;
  boolean searchKS4 = true;

  /**
   * Creates a new instance of ActivityIncomeReport
   */
  public ChildActivitiesReport() {
  }

  public void setSettings(Settings s) {
    settings = s;
  }

  public void init() {
    System.out.println("Getting child list");
    childList = new ArrayList<>();
    //return ejbFacade.getEntityManager().createNamedQuery("Child.findAllSorted").getResultList();
    String queryString = "SELECT e FROM Child e " + buildWhereClause() + " ORDER BY e.surname ASC, e.firstname ASC";

    List<Child> lc = ejbFacade.getEntityManager().createQuery(queryString).getResultList();
    for (Child c : lc) {
      boolean add = false;
      if (searchKS1 != true || searchKS2 != true || searchKS3 != true || searchKS4 != true) {
        // Check year.
        int childYear = settings.getSchoolYearFor(c, 0);
        if ((searchKS1 && childYear >= 0 && childYear <= 2)
                || (searchKS2 && childYear >= 3 && childYear <= 6)
                || (searchKS3 && childYear >= 7 && childYear <= 9)
                || (searchKS4 && childYear >= 10 && childYear <= 11)) {
          add = true;
        }
      } else {
        add = true;
      }
      
      // Only add if there are activities.
      if (add)
      {
        add = c.getActivities() != null && c.getActivities().size() > 0;
      }
      if (add) {
        // Create a child container for each one.
        childList.add(new ChildContainer(c));
      }
    }
  }

  public List<ChildContainer> getChildList() {
    if (childList != null) {
      System.out.println("Get child list - size" + childList.size());
    }
    return childList;
  }

  private String buildWhereClause() {
    boolean haveClause = false;
    String clause = new String();
    if (searchSurname != null && searchSurname.length() > 0) {
      haveClause = true;
      clause = addClause("UPPER(e.surname) LIKE '" + searchSurname.toUpperCase() + "'", clause);
    }
    if (searchFirstname != null && searchFirstname.length() > 0) {
      haveClause = true;
      clause = addClause("UPPER(e.firstname) LIKE '" + searchFirstname.toUpperCase() + "'", clause);
    }
    if (haveClause) {
      clause = "WHERE " + clause;
    }
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

  public void setSearchSurname(String s) {
    searchSurname = s;
  }

  public String getSearchSurname() {
    return searchSurname;
  }

  public void setSearchFirstname(String s) {
    searchFirstname = s;
  }

  public String getSearchFirstname() {
    return searchFirstname;
  }

  public void setSearchKS1(boolean b) {
    searchKS1 = b;
  }

  public boolean getSearchKS1() {
    return searchKS1;
  }

  public void setSearchKS2(boolean b) {
    searchKS2 = b;
  }

  public boolean getSearchKS2() {
    return searchKS2;
  }

  public void setSearchKS3(boolean b) {
    searchKS3 = b;
  }

  public boolean getSearchKS3() {
    return searchKS3;
  }

  public void setSearchKS4(boolean b) {
    searchKS4 = b;
  }

  public boolean getSearchKS4() {
    return searchKS4;
  }
  
//  public String formatOrganiserName(Contact c) {
//    if (c != null) {
//      String s = c.getFirstname() + " " + c.getSurname();
//      if (c.getPhone() != null && c.getPhone().length() > 0) {
//        s += " " + c.getPhone();
//      }
//      if (settings.getShowOrganiserEmail() && c.getEmail() != null && c.getEmail().length() > 0) {
//        s += " email: " + c.getEmail();
//      }
//      return s;
//    }
//    return null;
//  }
}
