/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wilson_pjr
 */
@Stateless
public class ExtraItemFacade extends AbstractFacade<ExtraItem> {
    @PersistenceContext(unitName = "uk.org.wrington_YouthWeek")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ExtraItemFacade() {
        super(ExtraItem.class);
    }
    
}
