package controler;

import bean.Admin;
import bean.Auteur;
import bean.Categorie;
import bean.Etudiant;
import bean.Oeuvre;
import bean.Reservation;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.CategorieFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.AdminFacade;

@Named("loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private AdminFacade adminFacade;

    @EJB
    private service.EtudiantFacade ejbFacade;
    private Etudiant etudiant;
    private Admin admin;
    
    private Long cne ;
    private String pass; 
    
    private String userAdmin;
    private String passAdmin;
 
    public LoginController() {
    }

    public Etudiant getEtudiant() {
        if(etudiant==null) etudiant = new Etudiant();
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Long getCne() {
        return cne;
    }

    public void setCne(Long cne) {
        this.cne = cne;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public Admin admin() {
        return SessionUtil.getadmin();
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(String userAdmin) {
        this.userAdmin = userAdmin;
    }

    public String getPassAdmin() {
        return passAdmin;
    }

    public void setPassAdmin(String passAdmin) {
        this.passAdmin = passAdmin;
    }
    
    
    
    
    
    public void checkAdminlogin() throws IOException {
        if(admin()==null)          SessionUtil.redirect("../../login");

    }
    
    public void deconnectionAdmin() throws IOException {
         SessionUtil.deconnectionAdmin();
         SessionUtil.redirect("../../login");
         
    }

    public void loginEtud(String page)  {
    
        if(page==null || page.equals("")) page="home";
            
        if(cne==null || pass.equals("")) {
            JsfUtil.addErrorMessage("CNE ou PASS vide");
            return;
        }
        Etudiant user = ejbFacade.login(cne, pass);
        
        if(user==null) {
            JsfUtil.addErrorMessage("CNE ou Pass incorrect");
            return;
        }
        
        JsfUtil.addSuccessMessage("Connexion success");
        SessionUtil.setuser(user);
        try {
            SessionUtil.redirect(page);
        } catch (IOException ex) {
           return;
        }
        

    }
    
    
    public void loginAdmin() { 
            
        if(userAdmin.equals("") || passAdmin.equals("")) {
            JsfUtil.addErrorMessage("Username ou Pass vide");
            return;
        } 
        
        Admin admin = adminFacade.login(userAdmin, passAdmin);
        if(admin==null) {
            JsfUtil.addErrorMessage("User ou Pass incorrect");
            return;
        }
        
        JsfUtil.addSuccessMessage("Connexion success");
        SessionUtil.setadmin(admin);
        try {
            SessionUtil.redirect("admin/home/index");
        } catch (IOException ex) {
           return;
        }
        
    }
    
    
    
    public void makeEtud() {
        if(getEtudiant().getCne()==null|| getEtudiant().getPassword().equals("") || getEtudiant().getPassword().equals("") ) {
             JsfUtil.addErrorMessage("CNE ou Pass ou Email vide");
             return;
         }
        
        if(ejbFacade.CheckCNE(getEtudiant().getCne())) {
             JsfUtil.addErrorMessage("CNE Existant");
            return;   
        }
        
        ejbFacade.create(etudiant);
        JsfUtil.addErrorMessage("Compte Cree avec success ");
        etudiant = new Etudiant();
    }
    
    
    

}
