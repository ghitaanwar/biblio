/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Reservation;
import bean.ReservationItem;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class ReservationItemFacade extends AbstractFacade<ReservationItem> {

    @EJB
    private OeuvreFacade oeuvreFacade;

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservationItemFacade() {
        super(ReservationItem.class);
    }
    @Override
    public void create(ReservationItem r) {
        
        if(r==null) return ;
        oeuvreFacade.incNombre(r.getOeuvre(), 1);
        super.create(r);
    }
    
    
    
    public List <ReservationItem> getByReservation(Reservation r) {
        if(r==null) return null;
        return em.createQuery("select r from ReservationItem r where r.reservation.id="+r.getId()).getResultList();
    }
    
    
    
}
