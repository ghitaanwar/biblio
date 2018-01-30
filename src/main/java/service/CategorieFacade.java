/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class CategorieFacade extends AbstractFacade<Categorie> {

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategorieFacade() {
        super(Categorie.class);
    }
    
    
    public List <Categorie> getListMaxCat(int num) {
        System.out.println("service.CategorieFacade.getListMaxCat() +" + em.createQuery("select c from Categorie c").setMaxResults(num).getResultList());
        return em.createQuery("select c from Categorie c").setMaxResults(num).getResultList();
    }
    
}
