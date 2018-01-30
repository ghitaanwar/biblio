/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Admin;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class AdminFacade extends AbstractFacade<Admin> {

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminFacade() {
        super(Admin.class);
    }
    
    public Admin login(String user , String pass) {
       if(user==null || pass==null ) return null;
       
       List <Admin> u = em.createQuery("select a from Admin a where  a.username='"+user+"'  and a.pass='"+pass+"'").getResultList();
       if(u.size()>0) return u.get(0);
       return null;    
   }
   
   public boolean checkExist(String username ) {
        List <Admin> u = em.createQuery("select a from Admin a where  a.username='"+username+"'").getResultList();
       
        if(u.size()>0) return true;
        
       return false;    
   }
    
}
