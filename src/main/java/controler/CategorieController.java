package controler;

import bean.Categorie;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.CategorieFacade;

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
import service.OeuvreFacade;

@Named("categorieController")
@SessionScoped
public class CategorieController implements Serializable {

    @EJB
    private OeuvreFacade oeuvreFacade;

    @EJB
    private service.CategorieFacade ejbFacade;
    private List<Categorie> items = null;
    private Categorie selected;
    private Categorie added; 
    

    public CategorieController() {
    }

    public Categorie getSelected() {
        return selected;
    }

    public void setSelected(Categorie selected) {
        this.selected = selected;
    }


    private CategorieFacade getFacade() {
        return ejbFacade;
    }

    public Categorie prepareCreate() {
        selected = new Categorie();
        return selected;
    }

    public Categorie getAdded() {
        if(added==null) added=new Categorie();
        return added;
    }

    public void setAdded(Categorie added) {
        this.added = added;
    }

    
    public void create() {
       ejbFacade.create(added);
        JsfUtil.addSuccessMessage("Categorie Ajouter");
        added=null;        
    }

    public void update() {
        JsfUtil.addSuccessMessage("Categorie Editer");

        ejbFacade.edit(selected);
        

    }

    public void destroy() {
    try {
            getFacade().remove(selected);
        }catch(Exception e){
             JsfUtil.addErrorMessage("Erreur: Des Oeuvre Lies cette Categorie");
            return; 
        }
        JsfUtil.addSuccessMessage("Categorie Supprimer");   
        
    }

    public List<Categorie> getItems() {        
        return ejbFacade.findAll();
    }
    
    public List <Categorie> getListMaxCat(int num){
        
        return ejbFacade.getListMaxCat(num);
    }    
    
    public int numberOeuvre(Categorie cat) {
        if(cat==null) return -1;
        return oeuvreFacade.getOeuvreByCategorie(cat, 0).size();
    }
    
    
    



    public Categorie getCategorie(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Categorie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Categorie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Categorie.class)
    public static class CategorieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategorieController controller = (CategorieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categorieController");
            return controller.getCategorie(getKey(value));
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
            if (object instanceof Categorie) {
                Categorie o = (Categorie) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Categorie.class.getName()});
                return null;
            }
        }

    }

}
