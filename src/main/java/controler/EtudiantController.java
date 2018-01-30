package controler;

import bean.Etudiant;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.EtudiantFacade;

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

@Named("etudiantController")
@SessionScoped
public class EtudiantController implements Serializable {

    @EJB
    private service.EtudiantFacade ejbFacade;
    private List<Etudiant> items = null;
    private Etudiant selected;

    public EtudiantController() {
    }

    public Etudiant getSelected() {
        if(selected==null) prepareCreate();
        return selected;
    }

    public void setSelected(Etudiant selected) {
        this.selected = selected;
    }


    private EtudiantFacade getFacade() {
        return ejbFacade;
    }

    public Etudiant prepareCreate() {
        selected = new Etudiant();
        return selected;
    }

    
    
    public void create() {
        ejbFacade.create(selected);
    }

    public void update() {
        ejbFacade.edit(selected);
    }

    public void destroy() {
         ejbFacade.remove(selected);

        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Etudiant> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }


    public Etudiant getEtudiant(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    public void editUser(Etudiant etudiant) {
        if(etudiant==null) return;
        ejbFacade.edit(etudiant);
    }

    @FacesConverter(forClass = Etudiant.class)
    public static class EtudiantControllerConverter implements Converter {

        
        
@Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EtudiantController controller = (EtudiantController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "etudiantController");
            return controller.getEtudiant(getKey(value));
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
            if (object instanceof Etudiant) {
                Etudiant o = (Etudiant) object;
                return getStringKey(o.getCne());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Etudiant.class.getName()});
                return null;
            }
        }        
        


    }

}
