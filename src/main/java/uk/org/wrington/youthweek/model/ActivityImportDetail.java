/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import uk.org.wrington.youthweek.model.Activity;

/**
 *
 * @author phil
 */
public class ActivityImportDetail {
  
  private final boolean eventConsent;
  private final boolean eventHelp;
  private final int eventId;
  private Activity activity;
  
  public ActivityImportDetail(boolean consent, boolean help, int eventId) {
    eventConsent = consent;
    eventHelp = help;
    this.eventId = eventId;
  }

  public boolean getConsentProvided() {
    return eventConsent;
  }

  public boolean getEventHelpProvided() {
    return eventHelp;
  }

  public int getEventId() {
    return eventId;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }
  
  public Activity getActivity() {
    return activity;
  } 
}
