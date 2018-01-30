/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Auteur;
import bean.Categorie;
import bean.Oeuvre;
import bean.Reservation;
import controler.util.SearchUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author omar
 */
@Stateless
public class OeuvreFacade extends AbstractFacade<Oeuvre> {

    @EJB
    private CategorieFacade categorieFacade;

    @EJB
    private ReservationFacade reservationFacade;

    @PersistenceContext(unitName = "com.mycompany_biblio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OeuvreFacade() {
        super(Oeuvre.class);
    }
    
    
    public void incNombre(Oeuvre oeuvre,int nb) {
        if(oeuvre==null) return;
        oeuvre =find(oeuvre.getId());
        System.out.println("service.OeuvreFacade.incNombre()");
        oeuvre.setNbemprute(oeuvre.getNbemprute()+nb);
        edit(oeuvre);
    }

    
    public List <Oeuvre> getOeuvreByCategorie(Categorie categorie , int nb){     
        Query q = em.createQuery("select o from Oeuvre o where o.categorie.id = "+categorie.getId());
        if(nb!=0) q.setMaxResults(nb);
        return q.getResultList();
        
    }
    
       
    public List <Oeuvre> getOeuvresList(Categorie categorie,Auteur auteur, String nom,Date date, String type,String lang, boolean dispo ) {
        String requette="select o from Oeuvre o where  1=1 ";
        
        if(categorie!=null) requette+=SearchUtil.addConstraint("o", "categorie.id", "=", categorie.getId());
        if(auteur!=null) requette+=SearchUtil.addConstraint("o", "auteur.id", "=", auteur.getId());
        if(nom!=null && !nom.equals("")) requette+="And o.nom LIKE  CONCAT('%','"+nom+"','%') ";
        if(date!=null) requette+=SearchUtil.addConstraint("o", "dateCreation", "=", date);
        if(type!=null && !type.equals("")) requette+=SearchUtil.addConstraint("o", "type", "LIKE", type);        
        if(lang!=null && !lang.equals("")) requette+=SearchUtil.addConstraint("o", "lang", "LIKE", lang);        
        if(dispo==true) requette+=("and o.nbemprute < o.qte");
        System.out.println("service.OeuvreFacade.getOeuvresList() :"+requette);
        return em.createQuery(requette).getResultList();
      
    }
    
    
    public List<String> getOeuvreType() {
                return em.createQuery("select  DISTINCT o.type from Oeuvre o where 1=1").getResultList();
    }
    
    public void incrementQte(Oeuvre oeuvre , int nb) {
        if(oeuvre==null || find(oeuvre.getId())==null) return;
        oeuvre.setQte(oeuvre.getQte() + nb );
        edit(oeuvre);
    }
    
    public void incrementtotalEmprute(Oeuvre oeuvre , int nb) {
        if(oeuvre==null || find(oeuvre.getId())==null) return;
        oeuvre = find(oeuvre.getId());
        oeuvre.setNbtotalemprute(oeuvre.getNbtotalemprute()+ 1);  
        edit(oeuvre);
    }    
    
    public Date getOuvreRemiseDate(Oeuvre oeuvre ) {
       Reservation r = reservationFacade.getLastOeuvreReservation(oeuvre);
       if(r!=null) return r.getDateRemise();
       return null;
    }


    public String graph1() {
        
        String s="";
        List <Categorie> categories =  categorieFacade.findAll().subList(0, 6);
        
        List<String> color = Arrays.asList("#f56954","#00a65a","#f39c12","#00c0ef","#3c8dbc","#d2d6de","#d2d6de");
        int j =0;
        for(Categorie cat:categories)  {
            if(j>6) break;
            if(j>0) s+=",";
            s+="{value: "+getOeuvreByCategorie(cat,0).size()+",color: \""+color.get(j)+"\",highlight: \""+color.get(j)+"\",label: \""+cat.getNom()+"\"}";
            j++;
        }
        
        return s;
    }
    
    public List<String> graph2() {
        
        List <String> r = new ArrayList<String>();
        
        String cat = "";
        String qte ="";
        int i=0;
        for(String type:getOeuvreType()) {
            if(i>0) {
               cat+=",";
               qte+=",";
            }

            cat+="\""+type+"\"";
            qte+=""+this.getOeuvresList(null, null, null, null, type, null, true).size();
                
            i++;
        }
        
        r.add(cat);
        r.add(qte);
        
        
        
        
        return r;
        
    }
    
    
}
