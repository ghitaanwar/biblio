package controler;

import bean.Admin;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.AdminFacade;

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

@Named("adminController")
@SessionScoped
public class AdminController implements Serializable {

    @EJB
    private service.AdminFacade ejbFacade;
    private List<Admin> items = null;
    private Admin selected;

    public AdminController() {
    }

    public Admin getSelected() {
        return selected;
    }

    public void setSelected(Admin selected) {
        this.selected = selected;
    }

    private AdminFacade getFacade() {
        return ejbFacade;
    }

    public Admin prepareCreate() {
        selected = new Admin();
        return selected;
    }

    public void create() {
        if(ejbFacade.checkExist(selected.getUsername())) {
        JsfUtil.addErrorMessage("Username Deja existant ");            
            return;
        }
        ejbFacade.create(selected);
        JsfUtil.addSuccessMessage("Admin Ajouter");
    }

    public void update() {
        if(selected==null) return;
        ejbFacade.edit(selected);
        JsfUtil.addSuccessMessage("Admin Editer");
        
    }

    public void destroy() {
        if(selected==null) return;        
            ejbFacade.remove(selected);
            selected=null;
            JsfUtil.addSuccessMessage("Admin Supprimer");
            
        
    }

    public List<Admin> getItems() {
            items = getFacade().findAll();
        return items;
    }



    public Admin getAdmin(java.lang.Long id) {
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Admin.class)
    public static class AdminControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AdminController controller = (AdminController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "adminController");
            return controller.getAdmin(getKey(value));
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
            if (object instanceof Admin) {
                Admin o = (Admin) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Admin.class.getName()});
                return null;
            }
        }

    }

}
