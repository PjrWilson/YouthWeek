/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilson_pjr
 */
@Entity
@Table(name = "ACTIVITY")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Activity.findAll", query = "SELECT e FROM Activity e"),
  @NamedQuery(name = "Activity.findAllSorted", query = "SELECT e FROM Activity e ORDER BY e.activityday"),
  @NamedQuery(name = "Activity.findForExport", query = "SELECT e FROM Activity e WHERE e.period = :activityPeriod ORDER BY e.name"),
  @NamedQuery(name = "Activity.withTransport", query = "SELECT e FROM Activity e WHERE e.activityday = :activityday AND e.travelTimeRequired = TRUE ORDER BY e.startTime"),
  @NamedQuery(name = "Activity.findForYearSorted", query = "SELECT e FROM Activity e WHERE e.activityday < 6 AND e.minyear <= :year AND e.maxyear >= :year ORDER BY e.activityday"),
  @NamedQuery(name = "Activity.findByDay", query = "SELECT e FROM Activity e WHERE e.activityday = :activityday ORDER BY e.startTime ASC, e.name ASC"),
  @NamedQuery(name = "Activity.findByActivityid", query = "SELECT e FROM Activity e WHERE e.activityid = :activityid")})
public class Activity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Basic(optional = false)
  @Column(name = "ACTIVITYID")
  private Integer activityid;
  @Basic(optional = false)
  @Column(name = "ACTIVITYDAY")
  private Integer activityday;
  @Basic(optional = false)
  @Column(name = "STARTTIME")
  @Temporal(TemporalType.TIME)
  Date startTime;
  @Basic(optional = false)
  @Column(name = "ENDTIME")
  @Temporal(TemporalType.TIME)
  Date endTime;

  @Column(name = "ACTIVITYSTARTTIME")
  @Temporal(TemporalType.TIME)
  Date activityStartTime;
  @Basic(optional = false)
  @Column(name = "ACTIVITYENDTIME")
  @Temporal(TemporalType.TIME)
  Date activityEndTime;

  @Size(max = 45)
  @Column(name = "NAME")
  private String name;
  @Lob
  @Size(max = 256)
  @Column(name = "DESCRIPTION")
  private String description;
  @Lob
  @Size(max = 256)
  @Column(name = "NOTES")
  private String notes;
  @Lob
  @Size(max = 256)
  @Column(name = "LEADERNOTES")
  private String leaderNotes;
  @Column(name = "COST")
  private Double cost = 0.0;
  @Column(name = "MINYEAR")
  private Integer minyear = -1;
  @Column(name = "MAXYEAR")
  private Integer maxyear = 99;
  @Lob
  @Size(max = 128)
  @Column(name = "VENUE")
  private String venue;
  @Column(name = "CONSENT")
  private Boolean consentRequired = Boolean.FALSE;
  @Column(name = "TRAVEL")
  private Boolean travelTimeRequired = Boolean.FALSE;

  @ManyToMany
  private List<Contact> organisers;

  @ManyToOne
  private Contact committeeMember;

  @Lob
  @Size(max = 256)
  @Column(name = "RESTRICTIONS")
  private String restrictions;

  @Column(name = "TIMEPERIOD")
  private Integer period;
 
  public Activity() {
    this.period = 0;
  }

  public Activity(Integer activityid) {
    this.period = 0;
    this.activityid = activityid;
  }

  public Integer getActivityid() {
    return activityid;
  }

  public void setActivityid(Integer activityid) {
    this.activityid = activityid;
  }

  public Integer getActivityDay() {
    return activityday;
  }

  public void setActivityDay(Integer day) {
    this.activityday = day;
  }

  public Date getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Date d) {
    this.startTime = d;
  }

  public Date getEndTime() {
    return this.endTime;
  }

  public void setEndTime(Date d) {
    this.endTime = d;
  }

  public Date getActivityStartTime() {
    return this.activityStartTime;
  }

  public void setActivityStartTime(Date d) {
    this.activityStartTime = d;
  }

  public Date getActivityEndTime() {
    return this.activityEndTime;
  }

  public void setActivityEndTime(Date d) {
    this.activityEndTime = d;
  }

  public Boolean getTravelTimeRequired() {
    if (travelTimeRequired == null) {
      return Boolean.FALSE;
    }
    return travelTimeRequired;
  }

  public void setTravelTimeRequired(Boolean b) {
    travelTimeRequired = b;
    if (travelTimeRequired == null) {
      travelTimeRequired = Boolean.FALSE;
    }
    if (travelTimeRequired.equals(Boolean.FALSE)) {
      activityStartTime = null;
      activityEndTime = null;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String s) {
    description = s;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String s) {
    notes = s;
  }

  public String getLeaderNotes() {
    return leaderNotes;
  }

  public void setLeaderNotes(String s) {
    leaderNotes = s;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

//  public String getEventminyear() {
//    switch (eventminyear) {
//      case -1:
//        return "Any";
//      case 0:
//        return "Reception";
//      default:
//        return String.valueOf(eventminyear);
//    }
//  }
  public Integer getMinyear() {
    return minyear;
  }

  public void setminyear(Integer minYear) {
    minyear = minYear;
  }

//  public void setEventminyear(String minYear) {
//    if (minYear.equalsIgnoreCase("any")) {
//      setEventminyear(-1);
//    } else if (minYear.equalsIgnoreCase("r")) {
//      setEventminyear(0);
//    } else {
//      setEventminyear(Integer.parseInt(minYear));
//    }
//  }
  public List<Contact> getOrganisers() {
    return organisers;
  }

  public void setOrganisers(List<Contact> organisers) {
    this.organisers = organisers;
  }

  public Contact getCommitteeMember() {
    return committeeMember;
  }

  public void setCommitteeMember(Contact c) {
    this.committeeMember = c;
  }

  public Integer getMaxyear() {
    return maxyear;
  }

//  public String getEventmaxyear() {
//    switch (eventmaxyear) {
//      case 99:
//        return "Any";
//      case 0:
//        return "Reception";
//      default:
//        return String.valueOf(eventmaxyear);
//    }
//  }
  public void setMaxyear(Integer maxYear) {
    maxyear = maxYear;
  }

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public Boolean getConsentRequired() {
    return consentRequired;
  }

  public void setConsentRequired(Boolean b) {
    this.consentRequired = b;
  }
  
  public void setRestrictions(String restrictions) {
    this.restrictions = restrictions;
  }
  
  public String getRestrictions() {
    return restrictions;
  }
  
  public void setPeriod(Integer period) {
    this.period = period;
  }
  
  public Integer getPeriod() {
    return period;
  }
  
//  public void setEventmaxyear(String maxYear) {
//    if (maxYear.equalsIgnoreCase("any")) {
//      setEventmaxyear(99);
//    } else if (maxYear.equalsIgnoreCase("r")) {
//      setEventmaxyear(0);
//    } else {
//      setEventmaxyear(Integer.parseInt(maxYear));
//    }
//  }
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (activityid != null ? activityid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Activity)) {
      return false;
    }
    Activity other = (Activity) object;
    if ((this.activityid == null && other.activityid != null) || (this.activityid != null && !this.activityid.equals(other.activityid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "uk.org.wrington.wywapp.model.Activiy[ activityid=" + activityid + " ]";
  }

}
