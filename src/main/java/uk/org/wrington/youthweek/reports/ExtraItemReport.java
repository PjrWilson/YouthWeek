/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import uk.org.wrington.youthweek.model.ExtraItem;
import uk.org.wrington.youthweek.model.ExtraItemEntry;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean(name = "extraItemReport")
@ViewScoped
public class ExtraItemReport implements Serializable {

  public class ExtraItemInfo {

    private final ExtraItem item;
    private long numberOrdered = 0;
    private long freeItems = 0;
    private double income = 0;

    public ExtraItemInfo(ExtraItem ei) {
      this.item = ei;
    }

    public void add(ExtraItemEntry eie) {
      numberOrdered += eie.getItemCount();
      if (eie.getFreeItem()) {
        freeItems += eie.getItemCount();
      } else {
        // Not free.
        income += (eie.getItemCount() * eie.getUnitCost());
      }
    }

    public ExtraItem getItem() {
      return item;
    }

    public long getNumberOrdered() {
      return numberOrdered;
    }

    public long getFreeItems() {
      return freeItems;
    }

    public double getIncome() {
      return income;
    }
  }

  List<ExtraItemInfo> infoList;
  ExtraItemInfo summaryInfo;

  @EJB
  private uk.org.wrington.youthweek.model.ExtraItemFacade ejbFacade;
  @EJB
  private uk.org.wrington.youthweek.model.ExtraItemEntryFacade ejbFacadeEntry;

  /**
   * Creates a new instance of ActivityIncomeReport
   */
  public ExtraItemReport() {
  }

  public void init() {
    infoList = new ArrayList<>();
    summaryInfo = new ExtraItemInfo(null);

    // Get the item definitions.
    List<ExtraItem> extraItemDefs = getItems();
    if (extraItemDefs != null) {
      for (ExtraItem ei : extraItemDefs) {

        ExtraItemInfo eii = new ExtraItemInfo(ei);

        // For each item, count the number required. Count the number of free ones.
        List<ExtraItemEntry> eieList = getOrdersFor(ei);

        if (eieList != null) {
          for (ExtraItemEntry eie : eieList) {
            eii.add(eie);
            summaryInfo.add(eie);
          }
        }

        infoList.add(eii);
      }
    }
  }

  public List<ExtraItemInfo> getInfoList() {
    return infoList;
  }

  public ExtraItemInfo getSummaryInfo() {
    return summaryInfo;
  }

  private List<ExtraItem> getItems() {
    return ejbFacade.getEntityManager().createNamedQuery("ExtraItem.findAll").getResultList();
  }

  private List<ExtraItemEntry> getOrdersFor(ExtraItem ei) {
    return ejbFacadeEntry.getEntityManager().createNamedQuery("ExtraItemEntry.findByItemid").
            setParameter("itemid", ei.getItemid()).getResultList();
  }
}
