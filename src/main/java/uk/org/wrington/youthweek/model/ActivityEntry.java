/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author wilson_pjr
 */
@Entity
@Table(name = "ACTIVITYENTRY")
@NamedQueries({
  @NamedQuery(name = "ActivityEntry.countEntries", query = "SELECT COUNT(e) FROM ActivityEntry e WHERE e.activity.activityid = :activityid"),
  @NamedQuery(name = "ActivityEntry.getEntriesWithNoConsent", 
          query = "SELECT e FROM ActivityEntry e WHERE e.consentFormSigned != TRUE AND e.activity.consentRequired = TRUE ORDER BY e.child.surname ASC, e.child.firstname ASC, e.activity.activityday ASC, e.activity.startTime ASC"),
  @NamedQuery(name = "ActivityEntry.countEntriesForChildAndDay", 
          query = "SELECT COUNT(e) FROM ActivityEntry e WHERE e.child.childid = :childid AND e.activity.activityday = :day"),
  @NamedQuery(name = "ActivityEntry.getEntriesForActivity", 
          query = "SELECT e FROM ActivityEntry e WHERE e.activity.activityid = :id ORDER BY e.child.surname ASC, e.child.firstname ASC"),
  @NamedQuery(name = "ActivityEntry.countEntriesForActivity", 
          query = "SELECT COUNT(e) FROM ActivityEntry e WHERE e.activity.activityid = :id"),
  @NamedQuery(name = "ActivityEntry.getEntriesForChildAndDay", 
          query = "SELECT e FROM ActivityEntry e WHERE e.child.childid = :childid AND e.activity.activityday = :day ORDER BY e.activity.startTime ASC"),
  @NamedQuery(name = "ActivityEntry.deleteAll", 
          query = "DELETE FROM ActivityEntry")})
public class ActivityEntry implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ACTIVITYID")
  private Activity activity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CHILDID")
  private Child child;

  // Can provide help
  @Column(name = "HELP")
  private Boolean canProvideHelp;
  // Consent form signed
  @Column(name = "CONSENT")
  private Boolean consentFormSigned;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ActivityEntry)) {
      return false;
    }
    ActivityEntry other = (ActivityEntry) object;
    // If this has no id, check the activities.
    if (this.id != null && other.id != null) {
      return this.id.equals(other.id);
    }
    if (this.activity != null && other.activity != null) {
      return this.activity.equals(other.activity);
    }
//    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
    return false;
    //  }
    //return true;
  }

  @Override
  public String toString() {
    return "uk.org.wrington.youthweek.model.ActivityEntry[ id=" + id + " ]";
  }

  public Activity getActivity() {
    if (activity == null) {
      activity = new Activity();
      activity.setActivityid(Integer.MAX_VALUE);
      activity.setName("");
      activity.setCost(0d);
    }
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public Child getChild() {
    return child;
  }

  public void setChild(Child child) {
    this.child = child;
  }

  public void setCanProvideHelp(Boolean help) {
    this.canProvideHelp = help;
  }

  public Boolean getCanProvideHelp() {
    return canProvideHelp;
  }

  public void setConsentFormSigned(Boolean isSigned) {
    this.consentFormSigned = isSigned;
  }

  public Boolean getConsentFormSigned() {
    return consentFormSigned;
  }

  public Boolean getConsentRequired() {
    return this.activity.getConsentRequired();
  }
}
