/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model.util;

import java.util.ArrayList;
import java.util.Date;
import uk.org.wrington.youthweek.model.Activity;

/**
 *
 * @author wilson_pjr
 */
public class CopyUtil {

  public static Activity clone(Activity a) {
    Activity na = new Activity();
    na.setActivityDay(a.getActivityDay());
    if (a.getStartTime() != null) {
      na.setStartTime((Date) a.getStartTime().clone());
    }
    if (a.getEndTime() != null) {
      na.setEndTime((Date) a.getEndTime().clone());
    }
    na.setTravelTimeRequired(a.getTravelTimeRequired());
    if (a.getActivityStartTime() != null) {
      na.setActivityStartTime((Date)a.getActivityStartTime().clone());
    }
    if (a.getActivityEndTime() != null) {
      na.setActivityEndTime((Date)a.getActivityEndTime().clone());
    }
    na.setName(a.getName());
    na.setDescription(a.getDescription());
    na.setNotes(a.getNotes());
    na.setLeaderNotes(a.getLeaderNotes());
    na.setCost(a.getCost());
    na.setminyear(a.getMinyear());
    na.setMaxyear(a.getMaxyear());
    na.setCommitteeMember(a.getCommitteeMember());
    na.setOrganisers(new ArrayList<>(a.getOrganisers()));
    na.setVenue(a.getVenue());
    na.setConsentRequired(a.getConsentRequired());

    return na;
  }
}
