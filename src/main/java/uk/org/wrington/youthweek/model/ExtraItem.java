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
@Table(name = "EXTRA")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ExtraItem.findAll", query = "SELECT e FROM ExtraItem e"),
  @NamedQuery(name = "ExtraItem.findByDay", query = "SELECT e FROM Activity e WHERE e.activityday = :activityday"),
  @NamedQuery(name = "ExtraItem.findByActivityid", query = "SELECT e FROM Activity e WHERE e.activityid = :activityid")})
public class ExtraItem implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Basic(optional = false)
  @Column(name = "ITEMID")
  private Integer itemid;
  @Size(max = 45)
  @Column(name = "NAME")
  private String name;
  @Lob
  @Size(max = 256)
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "COST")
  private Double cost = 0.0;
  @Size(max = 45)
  @Column(name = "REPORTKEY")
  private String reportKey;

  public ExtraItem() {
  }

  public ExtraItem(Integer itemid) {
    this.itemid = itemid;
  }

  public Integer getItemid() {
    return itemid;
  }

  public void setItemid(Integer itemid) {
    this.itemid = itemid;
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

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public void setReportKey(String key) {
    this.reportKey = key;
  }
  
  public String getReportKey() { 
    return reportKey;
  }
  
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (itemid != null ? itemid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ExtraItem)) {
      return false;
    }
    ExtraItem other = (ExtraItem) object;
    if ((this.itemid == null && other.itemid != null) || (this.itemid != null && !this.itemid.equals(other.itemid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "uk.org.wrington.wywapp.model.ExtraItem[ itemid=" + itemid + " ]";
  }

}
