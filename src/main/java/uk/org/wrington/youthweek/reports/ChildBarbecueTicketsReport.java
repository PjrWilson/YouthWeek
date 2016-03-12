/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import uk.org.wrington.youthweek.model.Activity;
import uk.org.wrington.youthweek.model.Child;
import uk.org.wrington.youthweek.model.ExtraItemEntry;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "childBarbecueTicketsReport")
@ViewScoped
public class ChildBarbecueTicketsReport implements Serializable {

  public class TicketRow {

    private final List<String> rowEntries;

    public TicketRow(List<String> rowEntries) {
      this.rowEntries = rowEntries;
    }

    public List<String> getRowEntries() {
      return rowEntries;
    }
  }

  public class ChildContainer implements Comparable<ChildContainer> {

    private final Child child;
    List<TicketRow> tickets;

    public ChildContainer(Child child) {
      this.child = child;
      tickets = new ArrayList<>();
      List<ExtraItemEntry> extras = child.getExtraItems();
      int size = extras.size();
      List<String> rowEntries = new ArrayList<>();
      for (int i = 0; i < size; ++i) {
        int itemCount = extras.get(i).getItemCount();
        for (int j = 0; j < itemCount; ++j) {
          rowEntries.add(extras.get(i).getItem().getName());
        }
      }
      size = rowEntries.size();
      for (int i = 0; i < size; i+=3) {
        List<String> row = new ArrayList<>();
        for (int j = 0; j < 3; ++j) {
          if (i + j < size) {
            row.add(rowEntries.get(i + j));
          } else {
            row.add(null);
          }
        }
        tickets.add(new TicketRow(row));
      }
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

    public List<TicketRow> getRows() {
      return tickets;
    }

    public Child getChild() {
      return child;
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
  public ChildBarbecueTicketsReport() {
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
      if (add && c.getExtraItems() != null && c.getExtraItems().size() > 0) {
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

  public String formatName(Activity a) {
    String s = a.getName();
    if (a.getVenue().length() > 0) {
      s += ", " + a.getVenue();
    }
    return s;
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
}
