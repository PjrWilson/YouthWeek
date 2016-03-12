/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import uk.org.wrington.youthweek.controller.ExtraItemController;
import uk.org.wrington.youthweek.model.ExtraItem;
import uk.org.wrington.youthweek.model.util.JsfUtil;
import uk.org.wrington.youthweek.settings.Settings;

/**
 *
 * @author wilson_pjr
 */
@ManagedBean
@SessionScoped
public class SettingsView implements Serializable {

  @ManagedProperty(value = "#{settings}")
  private Settings settings;
  @ManagedProperty(value = "#{extraItemController}")
  private ExtraItemController extraItemController;

  private ExtraItem created = null;
  private ExtraItem selected = null;

//  LocalDate startDate;
//  Float adminFee;
  @PostConstruct
  public void init() {
  }

  public void setExtraItemController(ExtraItemController controller) {
    this.extraItemController = controller;
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  public Date getStartDate() {
    return settings.getStartDate();
  }

  public void setStartDate(Date startDate) {
    if (!startDate.equals(settings.getStartDate())) {
      settings.setStartDate(startDate);
      System.out.println("START DATE = " + startDate.toString());
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      JsfUtil.addSuccessMessage("Selected start date " + format.format(startDate));
    }
  }

  public Boolean getHideDelete() {
    return settings.getDisableDelete();
  }

  public void setHideDelete(Boolean hide) {
    if (!hide.equals(settings.getDisableDelete())) {
      settings.setDisableDelete(hide);
      JsfUtil.addSuccessMessage("Set Delete Button hidden to " + hide);
    }
  }

  public Float getAdminFee() {
    return settings.getAdminFee();
  }

  public void setAdminFee(Float adminFee) {
    // Only put out message if its different.
    if (settings.getAdminFee() == null || !adminFee.equals(settings.getAdminFee())) {
      settings.setAdminFee(adminFee);
      System.out.println("ADMIN FEE = " + adminFee);
      JsfUtil.addSuccessMessage("Admin fee set to " + String.format("£%.2f", settings.getAdminFee()));
    }
  }

//  public void onDateSelect(SelectEvent event) {
//    // setStartDate((Date)event.getObject());
//    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//    JsfUtil.addSuccessMessage("Selected start date " + format.format(event.getObject()));
//  }
//
//  public void onFeeSelect() {
//    // setStartDate((Date)event.getObject());
//    // SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//
//  }
  public void prepareCreate() {
    System.out.println("SettingsView:prepareCreate");
    created = new ExtraItem();
  }

  public ExtraItem getCreated() {
    return created;
  }

  public void finaliseCreate() {
    System.out.println("SettingsView:finaliseCreate");
    if (created != null) {
      extraItemController.create(created);
      JsfUtil.addSuccessMessage("Created BBQ Item " + created.getName());
      created = null;
    }
  }

  public void update() {
    if (selected != null) {
      extraItemController.update(selected);
    }
  }

  public ExtraItem getSelected() {
    return selected;
  }

  public void setSelected(ExtraItem selected) {
    this.selected = selected;
  }

  public List<ExtraItem> getItems() {
    return extraItemController.getItems();
  }

  public String getReportMessageHeader() {
    return settings.getReportMessageHeader();
  }

  public void setReportMessageHeader(String header) {
    if (!header.equals(settings.getReportMessageHeader())) {
      settings.setReportMessageHeader(header);
      JsfUtil.addSuccessMessage("Saved report message header");
    }
  }

  public String getReportMessage() {
    return settings.getReportMessage();
  }

  public void setReportMessage(String msg) {
    if (!msg.equals(settings.getReportMessage())) {
      settings.setReportMessage(msg);
      JsfUtil.addSuccessMessage("Saved report message");
    }
  }
  
  public boolean getShowOrganiserEmail() {
    return settings.getShowOrganiserEmail();
  }
  
  public void setShowOrganiserEmail(boolean show) {
    if (show != settings.getShowOrganiserEmail()) {
      settings.setShowOrganiserEmail(show);
      JsfUtil.addSuccessMessage("Set Organiser Email in Child Report to " + show);
      
    }
  }
  public String getTicketHeader() {
    return settings.getTicketHeader();
  }
  
  public void setTicketHeader(String header) {
    if (!header.equals(settings.getTicketHeader())) {
      settings.setTicketHeader(header);
      JsfUtil.addSuccessMessage("Saved ticket title");
    }
  }
  
  public String getTicketMessage() {
    return settings.getTicketMessage();
  }
  
  public void setTicketMessage(String msg) {
    if (!msg.equals(settings.getTicketMessage())) {
      settings.setTicketMessage(msg);
      JsfUtil.addSuccessMessage("Saved ticket message");
    }
  }
  
  public String getMondayColour() {
    return settings.getColour("Monday");
  }
  
  public void setMondayColour(String c) {
    if (!c.equals(settings.getColour("Monday"))) {
      settings.setColour("Monday", c);
      JsfUtil.addSuccessMessage("Saved Monday Title Colour");
    }
  }
  
  public String getTuesdayColour() {
    return settings.getColour("Tuesday");
  }
  
  public void setTuesdayColour(String c) {
    if (!c.equals(settings.getColour("Tuesday"))) {
      settings.setColour("Tuesday", c);
      JsfUtil.addSuccessMessage("Saved Tuesday Title Colour");
    }
  }
  
  public String getWednesdayColour() {
    return settings.getColour("Wednesday");
  }
  
  public void setWednesdayColour(String c) {
    if (!c.equals(settings.getColour("Wednesday"))) {
      settings.setColour("Wednesday", c);
      JsfUtil.addSuccessMessage("Saved Wednesday Title Colour");
    }
  }
  
  public String getThursdayColour() {
    return settings.getColour("Thursday");
  }
  
  public void setThursdayColour(String c) {
    if (!c.equals(settings.getColour("Thursday"))) {
      settings.setColour("Thursday", c);
      JsfUtil.addSuccessMessage("Saved Thursday Title Colour");
    }
  }
  
  public String getFridayColour() {
    return settings.getColour("Friday");
  }
  
  public void setFridayColour(String c) {
    if (!c.equals(settings.getColour("Friday"))) {
      settings.setColour("Friday", c);
      JsfUtil.addSuccessMessage("Saved Friday Title Colour");
    }
  }
}
