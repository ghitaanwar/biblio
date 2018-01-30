package controler;

import bean.Site;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.SiteFacade;

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

@Named("siteController")
@SessionScoped
public class SiteController implements Serializable {

    @EJB
    private service.SiteFacade ejbFacade;
    private List<Site> items = null;
    private Site selected;

    public SiteController() {
    }

    public Site getSelected() {
        if(selected==null) selected = ejbFacade.getSite();
        return selected;
    }

    public void setSelected(Site selected) {
        this.selected = selected;
    }


    private SiteFacade getFacade() {
        return ejbFacade;
    }

   


    public void update() {
        JsfUtil.addSuccessMessage("Configuration Editer");
            
        ejbFacade.edit(selected);

    }


    public Site getSite(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Site> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Site> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Site.class)
    public static class SiteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SiteController controller = (SiteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "siteController");
            return controller.getSite(getKey(value));
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
            if (object instanceof Site) {
                Site o = (Site) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Site.class.getName()});
                return null;
            }
        }

    }

}
