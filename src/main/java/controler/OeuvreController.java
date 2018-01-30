package controler;

import bean.Auteur;
import bean.Categorie;
import bean.Oeuvre;
import controler.util.JsfUtil;
import controler.util.SessionUtil;
import controler.util.UploadUtil;
import java.io.IOException;
import service.OeuvreFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.UploadedFile;

@Named("oeuvreController")
@SessionScoped
public class OeuvreController implements Serializable {

    @EJB
    private service.OeuvreFacade ejbFacade;
    private List<Oeuvre> items = null;
    private Oeuvre selected;
    private Oeuvre added;
    private String addType="";

    private UploadedFile file;
    
    private Auteur auteur;
    private Categorie categorie;
    private String type;
    private boolean dispo=false;
    private String edition;
    private String lang;
    private String nom;
    private String image = UploadUtil.extPath+"../images/book.png";
   
    

    public OeuvreController() {
    }
    

    

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    

    public Oeuvre getSelected() {
        if(selected==null) prepareCreate() ;
        return selected;
    }

    public void setSelected(Oeuvre selected) {
        this.selected = selected;
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

    public boolean getDispo() {
        return dispo;
    }

    public void setDispo(boolean dispo) {
        this.dispo = dispo;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Oeuvre getAdded() {
        if(added==null) added= new Oeuvre();
        return added;
    }

    public void setAdded(Oeuvre added) {
        this.added = added;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    
    
    private OeuvreFacade getFacade() {
        return ejbFacade;
    }

    public Oeuvre prepareCreate() {
        selected = new Oeuvre();
        return selected;
    }
    
    
    public List<String> getOeuvreType() {
        return ejbFacade.getOeuvreType();

    }
    
    public List <String> typeList() {
        return ejbFacade.getOeuvreType();
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    
    

    public void upload(long id) {
        if(file==null) return;
        
        int i = file.getFileName().lastIndexOf('.');
        String extension = file.getFileName().substring(i+1);

        String name = "oeuvre-"+id+"."+extension;
        controler.util.UploadUtil.upload(file, name);
        System.out.println("File uploaded()"+name);
        Oeuvre o = ejbFacade.find(id);
        o.setImage(name);
        ejbFacade.edit(o);
        file=null;

    }
    
    public String showimg(Oeuvre o) {
        
        if(o==null) return null;
        if(o.getImage()==null || o.getImage().equals("")) return null;
        return UploadUtil.extPath.concat("/").concat(o.getImage());
        
    }
    
    
    public void deleteimg(long id) {
        Oeuvre o = ejbFacade.find(id);
        o.setImage(null);
        ejbFacade.edit(o);
    }
    
    public void create() {
        ejbFacade.create(added);
        upload(added.getId());    
        added=null;
        addType="";
        JsfUtil.addSuccessMessage("Oeuvre Ajouter");
        
    }
    
    public void drag() {
        System.out.println("controler.OeuvreController.drag()");
        getAdded().setType(getAddType());
    }

    
    public void delete(Oeuvre oeuvre) {
        
        if(oeuvre==null) return;
        if(oeuvre.getImage()==null || oeuvre.getImage().equals("") ) return;
        UploadUtil.delete(UploadUtil.rootPath.concat(oeuvre.getImage()));
        oeuvre.setImage(null);
        ejbFacade.edit(oeuvre);
        
    }
    
    public void update() {
        if(selected == null || selected.getId()==null) return;

        if(image!=null ) delete(selected);
        
        ejbFacade.edit(selected);        
        
        
        upload(selected.getId());
        selected=null;
        JsfUtil.addSuccessMessage("Oeuvre Editer");

    }

    public void destroy() {
        try {
            getFacade().remove(selected);
        }catch(Exception e){
             JsfUtil.addErrorMessage("Erreur: Des Reservation  Lies au Oeuvre ");
            return; 
        }
        JsfUtil.addSuccessMessage("Oeuvre supprimer");  
    }

    public List<Oeuvre> getItems() {
            items = getFacade().getOeuvresList(categorie, auteur, nom, null, type, lang, dispo);
            return items;
    }
    
    public void reset() {
        
        auteur=null;
        categorie =null;
        type=null;
        dispo=false;
        edition=null;
        lang="";
        nom="";        
    }
    
    
    
    public List<Oeuvre> dipoOeuvreList() {
        return getFacade().getOeuvresList(null, null, null, null, null, null, true);
    }
    public Date getOuvreRemiseDate(Oeuvre oeuvre) {
        return ejbFacade.getOuvreRemiseDate(oeuvre); 
}
    public String graph() {
        return ejbFacade.graph1();
    }
    
    public List <String> graph2() {
        return ejbFacade.graph2();
    }     
    public Oeuvre getOeuvre(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    public void exporttopdf() throws IOException {
        
        if(getItems()==null || getItems().size()==0) {
            JsfUtil.addErrorMessage("Aucun Oeuvre Trouver");
            return;
        }
        String admin = "admin";        
         Map<String, Object> params = new HashMap<String, Object>();
         Date date = new Date();
        params.put("date", date);
        params.put("type", type==null||type.equals("")?"Tous Les Type":type);
        params.put("categorie", categorie==null?"Tous Les Categorie":categorie.getNom());
        params.put("auteur", auteur==null?"Tous Les Auteurs":auteur.getNom());
        params.put("admin", admin);
        params.put("dispo", dispo==true?"Disponible":"Tous Les Oeuvre");
        try {
            controler.util.PdfUtil.generatePdf(getItems(), params, "Oeuvre-"+date, "/oeuvre.jrxml");
       
        } catch (Exception e) {
            //Logger.getLogger(InventaireFacade.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            return;
        }       
        FacesContext.getCurrentInstance().responseComplete();         
        reset();
        SessionUtil.redirect("index");
    }
    

    @FacesConverter(forClass = Oeuvre.class)
    public static class OeuvreControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OeuvreController controller = (OeuvreController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "oeuvreController");
            return controller.getOeuvre(getKey(value));
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
            if (object instanceof Oeuvre) {
                Oeuvre o = (Oeuvre) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Oeuvre.class.getName()});
                return null;
            }
        }

    }

}
