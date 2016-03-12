/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilson_pjr
 */
@Entity
@Table(name = "CONTACT")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Contact.findAll", query = "SELECT e FROM Contact e")})
public class Contact implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Basic(optional = false)
  @Column(name = "CONTACTID")
  private Integer contactid;
  @Size(max = 45)
  @Column(name = "FIRSTNAME")
  private String firstname;
  @Size(max = 45)
  @Column(name = "SURNAME")
  private String surname;
  @Lob
  @Size(max = 256)
  @Column(name = "ADDRESS")
  private String address;
  @Size(max = 50)
  @Column(name = "EMAIL")
  private String email;
  @Size(max = 25)
  @Column(name = "PHONE")
  private String phone;
  @Size(max = 50)
  @Column(name = "EMERGPHONE")
  private String emergencyPhone;

  @Lob
  @Size(max = 256)
  @Column(name = "NOTES")
  private String notes;
  @Column(name = "ISCOMMITEE")
  private Boolean committee;
  
  public Contact() {
  }

  public Integer getContactid() {
    return contactid;
  }

  public void setContactid(Integer contactid) {
    this.contactid = contactid;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String s) {
    address = s;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Boolean getCommittee() {
    return committee;
  }
  
  public void setCommittee(Boolean b) {
    committee = b;
  }
  
  public String getEmergencyPhone() {
    return emergencyPhone;
  }

  public void setEmergencyPhone(String emergencyPhone) {
    this.emergencyPhone = emergencyPhone;
  }
  
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (contactid != null ? contactid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Contact)) {
      return false;
    }
    Contact other = (Contact) object;
    if ((this.contactid == null && other.contactid != null) || (this.contactid != null && !this.contactid.equals(other.contactid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    // return "uk.org.wrington.wywapp.model.Contact[ contactid=" + contactid + " ]";
    String s = firstname + " " + surname;
    if (phone != null & phone.length() > 0) {
      s += " " + phone;
    }
    if (email != null & email.length() > 0) {
      s += " email:" + email;
    }
    return s;
  }

}
