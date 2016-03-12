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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "CHILD")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Child.findAll", query = "SELECT e FROM Child e"),
  @NamedQuery(name = "Child.findAllSorted", query = "SELECT e FROM Child e ORDER BY e.surname ASC, e.firstname ASC"),
  @NamedQuery(name = "Child.findByChildid", query = "SELECT e FROM Child e WHERE e.childid = :childid")})
public class Child implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Basic(optional = false)
  @Column(name = "CHILDID")
  private Integer childid;
  @Size(max = 45)
  @Column(name = "FIRSTNAME")
  private String firstname;
  @Size(max = 45)
  @Column(name = "SURNAME")
  private String surname;
  @Basic(optional = false)
  @Column(name = "DOB")
  @Temporal(TemporalType.DATE)
  Date dateOfBirth;
  @Lob
  @Size(max = 256)
  @Column(name = "MEDICAL")
  private String medicalInfo;
  @Lob
  @Size(max = 256)
  @Column(name = "SPECIAL")
  private String specialNeeds;
  @Column(name = "TETANUS")
  private Boolean tetanusUpToDate;
  @Column(name = "NOADMINFEE")
  private Boolean noAdminFee;
  
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "child")
  private List<ActivityEntry> activities;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "child")
  private List<ExtraItemEntry> extraItems;

  @ManyToOne
  private Contact parentContact;

  public Child() {
    noAdminFee = false;
    tetanusUpToDate = false;
  }

  public Integer getChildid() {
    return childid;
  }

  public void setChildid(Integer childid) {
    this.childid = childid;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dob) {
    dateOfBirth = dob;
  }

  public String getMedicalInfo() {
    return medicalInfo;
  }

  public void setMedicalInfo(String medicalInfo) {
    this.medicalInfo = medicalInfo;
  }

  public String getSpecialNeeds() {
    return specialNeeds;
  }

  public void setSpecialNeeds(String specialNeeds) {
    this.specialNeeds = specialNeeds;
  }

  public Boolean getTetanusUpToDate() {
    return tetanusUpToDate;
  }

  public void setTetanusUpToDate(Boolean upToDate) {
    this.tetanusUpToDate = upToDate;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (childid != null ? childid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Child)) {
      return false;
    }
    Child other = (Child) object;
    if ((this.childid == null && other.childid != null) || (this.childid != null && !this.childid.equals(other.childid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    // return "uk.org.wrington.wywapp.model.Contact[ contactid=" + contactid + " ]";
    String s = firstname + " " + surname;
    return s;
  }

  public void setActivities(List<ActivityEntry> activities) {
    this.activities = activities;
    // Ensure child is set.
    for (ActivityEntry ae : this.activities) {
      ae.setChild(this);
    }
  }

  public List<ActivityEntry> getActivities() {
    return activities;
  }
  
  public void setExtraItems(List<ExtraItemEntry> extraItems) {
    this.extraItems = extraItems;
    // Ensure child is set.
    for (ExtraItemEntry eie : this.extraItems) {
      eie.setChild(this);
    }
  }
  
  public List<ExtraItemEntry> getExtraItems() {
    return extraItems;
  }
  
  public void setNoAdminFee(Boolean b) {
    noAdminFee = b;
  }
  
  public Boolean getNoAdminFee() {
    return noAdminFee;
  }
  
  public void setParentContact(Contact parentContact) {
    this.parentContact = parentContact;
  }
  
  public Contact getParentContact() {
    return parentContact;
  }
}
