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
import javax.faces.view.ViewScoped;
import uk.org.wrington.youthweek.model.ActivityEntry;
import uk.org.wrington.youthweek.model.Child;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "childConsentFormReport")
@ViewScoped
public class ChildConsentFormReport implements Serializable {

  public class ChildContainer implements Comparable<ChildContainer> {

    private final Child child;
    private final List<ActivityEntry> activities = new ArrayList<>();
    
    public ChildContainer(Child child) {
      this.child = child;
    }
    
    @Override public boolean equals(Object o) {
      if (o instanceof ChildContainer) {
        return compareTo((ChildContainer)o) == 0;
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
    
    public void add(ActivityEntry ae) {
      activities.add(ae);
    }
    
    public List<ActivityEntry> getActivities() {
      return activities;
    }
    
    public Child getChild() {
      return child;
    }
  }
  
  List<ChildContainer> childList;

  @EJB
  private uk.org.wrington.youthweek.model.ChildFacade ejbFacade;
  @EJB
  private uk.org.wrington.youthweek.model.ActivityEntryFacade ejbFacadeEntry;

  /**
   * Creates a new instance of ActivityIncomeReport
   */
  public ChildConsentFormReport() {
  }

  public void init() {

    childList = new ArrayList<>();
    // Get the child definitions.
    List<ActivityEntry> entryList = getEntriesWithNoConsentForm();
    //
    if (entryList != null) {
      // Go through the list adding each child and the list of items.
      for (ActivityEntry ae : entryList) {
        //System.out.println("Checking " + ae.getChild().toString() + " in " + ae.getActivity().toString());
        // See if the child list contains this child.
        ChildContainer cc = new ChildContainer(ae.getChild());
        int index = childList.indexOf(cc);
        if (index == -1) {
          childList.add(cc);
        } else {
          cc = childList.get(index);
        }
        
        // Add this.
        cc.add(ae);
      }
    }
  }

  private List<ActivityEntry> getEntriesWithNoConsentForm() {
    return ejbFacade.getEntityManager().createNamedQuery("ActivityEntry.getEntriesWithNoConsent").getResultList();
  }

  public List<ChildContainer> getChildList() {
    return childList;
  }
}
