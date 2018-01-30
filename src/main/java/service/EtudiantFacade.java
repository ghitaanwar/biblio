/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Etudiant;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class EtudiantFacade extends AbstractFacade<Etudiant> {

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtudiantFacade() {
        super(Etudiant.class);
    }
    
    
    public Etudiant login(Long cne , String pass) {
        
        List <Etudiant> list = em.createQuery("select e from Etudiant e where e.cne = '"+cne+"' and e.password='"+pass+"' ").getResultList();
        if(list!=null && list.size()>0) return list.get(0);
        return null;
    }
    
    public boolean CheckCNE(Long cne) {
         List <Etudiant> list = em.createQuery("select e from Etudiant e where e.cne = '"+cne+"'").getResultList();
        if(list!=null && list.size()>0) return true;
        return false;
    }
    
}
