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
@Table(name = "EXTRAITEMENTRY")
@NamedQueries({
  @NamedQuery(name = "ExtraItemEntry.findAll", query = "SELECT e FROM ExtraItemEntry e"),
  @NamedQuery(name = "ExtraItemEntry.findByChild", query = "SELECT e FROM ExtraItemEntry e WHERE e.child.childid = :childid"),
  @NamedQuery(name = "ExtraItemEntry.findByItemid", query = "SELECT e FROM ExtraItemEntry e WHERE e.item.itemid = :itemid"),
  @NamedQuery(name = "ExtraItemEntry.deleteAll", query = "DELETE FROM ExtraItemEntry")})
public class ExtraItemEntry implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ITEMID")
  private ExtraItem item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CHILDID")
  private Child child;

  // Count of them
  @Column(name = "COUNT")
  private Integer itemCount;
  // Free
  @Column(name = "FREEITEM")
  private Boolean freeItem;

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
    if (!(object instanceof ExtraItemEntry)) {
      return false;
    }
    ExtraItemEntry other = (ExtraItemEntry) object;
    System.out.println("Compare " + this.toString() + " to " + other.toString());
    if (this.id != null && other.id != null) {
      return this.id.equals(other.id);
    }
    return (this.freeItem != null && this.freeItem.equals(other.freeItem)
            && this.item != null && this.item.equals(other.item)
            && this.itemCount != null && this.itemCount.equals(other.itemCount));
//      return true;
 //   }
//    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
  //  return false;
//    }
//    return true;
  }

  @Override
  public String toString() {
    return "uk.org.wrington.youthweek.model.ExtraItemEntry[ id=" + id + " ]: Item: " + (item != null ? item.toString() : "null") +
            ", count = " + itemCount;
  }

  public ExtraItem getItem() {
    return item;
  }

  public void setItem(ExtraItem item) {
    this.item = item;
  }

  public Child getChild() {
    return child;
  }

  public void setChild(Child child) {
    this.child = child;
  }

  public void setItemCount(Integer count) {
    System.out.println("Item count = " + count);
    this.itemCount = count;
  }

  public Integer getItemCount() {
    return itemCount;
  }

  public void setFreeItem(Boolean free) {
    this.freeItem = free;
  }

  public Boolean getFreeItem() {
    return freeItem;
  }

  public Double getCost() {
    double c = 0;
    if (this.item != null && itemCount != null && freeItem != null && freeItem == false) {
      c = item.getCost() * itemCount;
    }
    return c;
  }

  public Double getUnitCost() {
    double c = 0;
    if (this.item != null && freeItem != null && freeItem == false) {
      c = item.getCost();
    }
    return c;
  }
}
