/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Auteur;
import bean.Oeuvre;
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
public class AuteurFacade extends AbstractFacade<Auteur> {

    @EJB
    private OeuvreFacade oeuvreFacade;

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuteurFacade() {
        super(Auteur.class);
    }
    
    public int nbBookAuteur(Auteur auteur) {
        if(auteur==null) return 0;
        List <Oeuvre> list = oeuvreFacade.getOeuvresList(null, auteur, null, null, null, null, false);
        if(list!=null) return list.size();
        return 0  ;
        
    }
    
}
