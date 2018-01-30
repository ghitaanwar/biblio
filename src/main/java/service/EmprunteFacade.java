/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Emprunte;
import bean.Etudiant;
import bean.Oeuvre;
import controler.util.SearchUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class EmprunteFacade extends AbstractFacade<Emprunte> {

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmprunteFacade() {
        super(Emprunte.class);
    }
    
    public List <Emprunte> getByEtudiant(Etudiant etudiant) {
        
        if(etudiant==null) return null;
        return em.createQuery("select e from Emprunte e where e.etudiant.cne="+etudiant.getCne()+" ORDER BY e.dateDemande DESC").getResultList();

    }
    
    
    
    public List <Emprunte> search(Long cne , Date min , Date max ,int state ) {
        String query="select e from Emprunte e , EmprunteItem ei where 1=1 and ei.emprute.id=e.id";
        
        if(cne!=null )  query += SearchUtil.addConstraint("e", "etudiant.cne", "=", cne);

        if(min!=null || max!=null)        query += SearchUtil.addConstraintMinMaxDate("e", "dateDemande", min, max);
        
        if(state==0) query += " And ei.datedepoteffectif IS NULL ";
        if(state==1) query += " And ei.datedepoteffectif IS NOT NULL ";
        return em.createQuery(query+" ORDER BY e.id DESC").getResultList();
        
        

    }
    
    
}
