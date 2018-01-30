/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Emprunte;
import bean.EmprunteItem;
import bean.Etudiant;
import bean.Oeuvre;
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
public class EmprunteItemFacade extends AbstractFacade<EmprunteItem> {

    @EJB
    private OeuvreFacade oeuvreFacade;

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmprunteItemFacade() {
        super(EmprunteItem.class);
    }
    
    public List <EmprunteItem> getByEmprute(Emprunte e) {
        if(e==null) return null;
        return em.createQuery("select e from EmprunteItem e where e.emprute.id="+e.getId()).getResultList();
    }  
    
    public List <EmprunteItem> getByEtudiant(Etudiant e) {
        if(e==null) return null;
        return em.createQuery("select e from EmprunteItem e where e.emprute.etudiant.cne="+e.getCne() +" order by e.emprute.id DESC").getResultList();
    }
    
    
    public List <EmprunteItem> search(Long cne ,Oeuvre oeuvre, Date min , Date max ,int state ) {
        String query="select e from EmprunteItem e where 1=1";
        
        if(cne!=null )  query += SearchUtil.addConstraint("e", "emprute.etudiant.cne", "=", cne);
        if(oeuvre!=null)  query += SearchUtil.addConstraint("e", "oeuvre.id", "=", oeuvre.getId());
        
        if(state==0) query += " And e.datedepoteffectif IS NULL ";
        if(state==1) query += " And e.datedepoteffectif IS NOT NULL ";
        
        //if(state==1) query += SearchUtil.addConstraint("e", "datedepoteffectif", "<>", null);
        
        if(min!=null || max!=null)
        query += SearchUtil.addConstraintMinMaxDate("e", "dateDepotPrevue", min, max);
        
        return em.createQuery(query+" ORDER BY E.emprute.etudiant.cne,E.id DESC").getResultList();
        
    }
    
    @Override
    public void create(EmprunteItem emprunteItem) {
        if(emprunteItem==null) return;
        super.create(emprunteItem);
       oeuvreFacade.incrementtotalEmprute(emprunteItem.getOeuvre(), 1);
    }
    
    
    public void rendre(EmprunteItem emprunteItem , Date date){
        if(emprunteItem==null ||date==null ) return;
         emprunteItem = find(emprunteItem.getId());
        emprunteItem.setDatedepoteffectif(date);
        oeuvreFacade.incNombre(emprunteItem.getOeuvre(), -1);
        edit(emprunteItem);
    }
}
