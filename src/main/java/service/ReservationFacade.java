/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Etudiant;
import bean.Oeuvre;
import bean.Reservation;
import bean.ReservationItem;
import controler.util.DateUtil;
import controler.util.SearchUtil;
import java.util.Date;
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
public class ReservationFacade extends AbstractFacade<Reservation> {

    @EJB
    private SiteFacade siteFacade;

    @EJB
    private OeuvreFacade oeuvreFacade;

    @EJB
    private ReservationItemFacade reservationItemFacade;

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservationFacade() {
        super(Reservation.class);
    }
    
    public void anuller(Reservation r) {
        
        if(r==null) return;
        
        List <ReservationItem> list = reservationItemFacade.getByReservation(r);
        for(ReservationItem ri : list){
            oeuvreFacade.incNombre(ri.getOeuvre(), -1);
        }
        r.setTraiter(true);
        r.setConfirmer(false);
        edit(r);
        
    }
    
    
    public List <Reservation> reservationByEtudiant(Etudiant etudiant) {
        
        if(etudiant==null) return null;
        return em.createQuery("select r from Reservation r where r.etudiant.cne="+etudiant.getCne()+" ORDER BY r.id DESC").getResultList();
            
    }
    
    public  Reservation getLastOeuvreReservation(Oeuvre oeuvre) {
        if(oeuvre==null) return null;
   
        List <Reservation> list = em.createQuery("select r from Reservation r , ReservationItem ri  where r.id=ri.reservation.id and ri.oeuvre.id='"+oeuvre.getId()+"' ORDER BY r.dateRemise DESC").getResultList();
        if(list !=null && list.size()>0) return list.get(0);
        return null;
    }
    
    public void confirmer(Reservation re , boolean stat) {
        if(re==null) return;
        re.setTraiter(true);
        re.setConfirmer(stat);
        edit(re);
    }
    
  /*  public String etat(int i ) {
        
        String query="";
        if(i==-1) {
            query +=  SearchUtil.addConstraint("r", "r.traiter", "=", false);
            return query;
        }
        
        query +=  SearchUtil.addConstraint("r", "r.traiter", "=", true);
            
        if(i==0) query +=SearchUtil.addConstraint("r", "r.confirmer", "=", false); 
        else query +=SearchUtil.addConstraint("r", "r.confirmer", "=", true); 

        return query;

        
    } */
    
    
    public void refresh() {
        
        List <Reservation> list = em.createQuery("select r from Reservation r where r.traiter=false ").getResultList();
        
        for(Reservation r : list) {
            state(r);
        }
    }
    
    public int state(Reservation r) {
        if(r==null) return -2;
        int day = siteFacade.getSite().getMaxdayafterreserve();
        Date dateMax = DateUtil.incrimentDate(r.getDateDemande(), day);
        System.out.println("service.ReservationFacade.state()"+r.getDateDemande()+":"+dateMax);
        
        if(r.isTraiter()) {
            
            if(r.isConfirmer()) return 1;
            return -1;
        }
        
        if(r.isConfirmer()==false && new Date().after(dateMax)==true ) {
            anuller(r);
            return -1;
        }
        return 0;
    }
    
    public List <Reservation> search(Long etudiant , Oeuvre oeuvre,Date min,Date max,int etat) {
        refresh();
        
        String query = "select r from Reservation r  ";

        if(oeuvre!=null) {
            query +=", ReservationItem ri  where r.id=ri.reservation.id";
            query += SearchUtil.addConstraint("ri", "oeuvre.id", "=", oeuvre.getId());
            
        }else query +=" where 1=1";

            
        
        
        if(etudiant!=null )
            query +=  SearchUtil.addConstraint("r", "etudiant.cne", "=", etudiant);
        
        if(min!=null || max!=null)
        query += SearchUtil.addConstraintMinMaxDate("r", "dateDemande", min, max);
                    

        
        
        if(etat==1) query += SearchUtil.addConstraint("r", "traiter", "=", true) + SearchUtil.addConstraint("r", "confirmer", "=", true);
        if(etat==-1) query += SearchUtil.addConstraint("r", "traiter", "=", true) + SearchUtil.addConstraint("r", "confirmer", "=", false);
        if(etat==0) query += SearchUtil.addConstraint("r", "traiter", "=", false) ;
        
        System.out.println("service.ReservationFacade.search()"+query);
         return em.createQuery(query+" order by r.id DESC").getResultList();
    }
    
    
    
    
    
    
}
