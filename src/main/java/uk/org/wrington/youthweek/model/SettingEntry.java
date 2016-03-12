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
import javax.persistence.Id;
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
@Table(name = "SETTINGS")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "SettingEntry.findAll", query = "SELECT e FROM SettingEntry e")})
public class SettingEntry implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "SKEY")
  private String key;
  @Size(max = 255)
  @Column(name = "SVALUE")
  private String value;

  public SettingEntry() {
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (key != null ? key.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof SettingEntry)) {
      return false;
    }
    SettingEntry other = (SettingEntry) object;
    if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "uk.org.wrington.wywapp.model.SettingEntry[ key=" + key + " ]";
  }

}
