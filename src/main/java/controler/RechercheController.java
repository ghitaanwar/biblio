package controler;

import bean.Auteur;
import bean.Categorie;
import bean.Oeuvre;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import java.io.IOException;
import service.AuteurFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.OeuvreFacade;

@Named("rechecheController")
@SessionScoped
public class RechercheController implements Serializable {

    @EJB
    private service.AuteurFacade auteurFacade ;
    @EJB
    private service.OeuvreFacade oeuvreFacade;
    
    private List<Oeuvre> books = null;
    private Auteur auteur;
    private Categorie categorie;
    private String type;
    private String nom;    
    private Oeuvre book;
    private String message;
    private boolean dispo=false;

    public RechercheController() {
    }
    
    
    @PostConstruct
    public void init() {
        books = oeuvreFacade.findAll();
    }

    public AuteurFacade getAuteurFacade() {
        return auteurFacade;
    }    
    
    public void setAuteurFacade(AuteurFacade auteurFacade) {
        this.auteurFacade = auteurFacade;
    }

    public OeuvreFacade getOeuvreFacade() {
        return oeuvreFacade;
    }

    public void setOeuvreFacade(OeuvreFacade oeuvreFacade) {
        this.oeuvreFacade = oeuvreFacade;
    }

    public List<Oeuvre> getBooks() {
        return books;
    }

    public void setBooks(List<Oeuvre> books) {
        this.books = books;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Oeuvre getBook() {
        return book;
    }

    public void setBook(Oeuvre book) {
        this.book = book;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDispo() {
        return dispo;
    }

    public void setDispo(boolean dispo) {
        this.dispo = dispo;
    }
    


    public void recherche() {
        books = oeuvreFacade.getOeuvresList(categorie,auteur,nom , null,type,null, dispo);
        if(books==null) message ="Aucun euvre trouver";
    }
    
    public void rechercheRD() throws IOException {
        books = oeuvreFacade.getOeuvresList(categorie,auteur,nom , null,type,null, false);
        controler.util.SessionUtil.redirect("recherche");
    }

    public void clear() {
        categorie=null;
        auteur = null;
        nom="";
        type="";
    }
    public void CarousrelCategorie(Categorie cat) throws IOException{
        clear();
        categorie=cat;
        books = oeuvreFacade.getOeuvresList(cat,auteur,nom , null,type,null, false);
        controler.util.SessionUtil.redirect("recherche");
    }
    
    public void viewbook(Oeuvre oeuvre) {
        book=oeuvre;
    }
    
    public void addtoCart(Oeuvre oeuvre){
        
    }
    
 
}
