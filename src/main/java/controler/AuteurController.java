package controler;

import bean.Auteur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.AuteurFacade;

import java.io.Serializable;
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

@Named("auteurController")
@SessionScoped
public class AuteurController implements Serializable {

    @EJB
    private service.AuteurFacade ejbFacade;
    private List<Auteur> items = null;
    private Auteur selected;
    private Auteur added; 

    public AuteurController() {
    }

    public Auteur getSelected() {
        return selected;
    }

    public void setSelected(Auteur selected) {
        this.selected = selected;
    }

    public Auteur getAdded() {
        if(added==null) added = new Auteur();        
        return added;
    }

    public void setAdded(Auteur added) {
        this.added = added;
    }



    private AuteurFacade getFacade() {
        return ejbFacade;
    }

    public Auteur prepareCreate() {
        selected = new Auteur();
        return selected;
    }

    public void create() {
       ejbFacade.create(added);
        JsfUtil.addSuccessMessage("Auteur Ajouter");
        added=null;        
    }

    public void update() {

        ejbFacade.edit(selected);

    }

    public void destroy() {
 try {
            getFacade().remove(selected);
        }catch(Exception e){
             JsfUtil.addErrorMessage("Erreur: Des Oeuvre Lies au Auteur");
            return; 
        }
        JsfUtil.addSuccessMessage("Auteur Supprimer");        
    }

    public List<Auteur> getItems() {
            return getFacade().findAll();
    }

    public int nbOeuvre(Auteur a) {
        return ejbFacade.nbBookAuteur(a);
        
    }

    public Auteur getAuteur(java.lang.Long id) {
        return getFacade().find(id);
    }

    

    @FacesConverter(forClass = Auteur.class)
    public static class AuteurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AuteurController controller = (AuteurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "auteurController");
            return controller.getAuteur(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Auteur) {
                Auteur o = (Auteur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Auteur.class.getName()});
                return null;
            }
        }

    }

}
