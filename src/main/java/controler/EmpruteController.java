package controler;

import bean.Emprunte;
import bean.EmprunteItem;
import bean.Etudiant;
import bean.Oeuvre;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.MailUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import service.EmprunteFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.mail.MessagingException;
import service.EmprunteItemFacade;
import service.OeuvreFacade;
import service.SiteFacade;

@Named("empruteController")
@SessionScoped
public class EmpruteController implements Serializable {

    @EJB
    private OeuvreFacade oeuvreFacade;

    @EJB
    private SiteFacade siteFacade;

    @EJB
    private EmprunteItemFacade emprunteItemFacade;

    @EJB
    private service.EmprunteFacade ejbFacade;
    private List<Emprunte> items = null;
    private Emprunte selected;
    private Long cne;
    private Oeuvre oeuvre;
    private Date min;
    private Date max;
    private int etat = -1;

    private List<Oeuvre> listitems;
    
    
    
    public EmpruteController() {
    }

    public Emprunte getSelected() {
        if(selected==null) selected= new Emprunte();
        return selected;
    }

    public List<Oeuvre> getListitems() {
        if(listitems==null) listitems = new ArrayList<Oeuvre>();
        return listitems;
    }

    public void setListitems(List<Oeuvre> listitems) {
        this.listitems = listitems;
    }

    
    public void setSelected(Emprunte selected) {
        this.selected = selected;
    }


    private EmprunteFacade getFacade() {
        return ejbFacade;
    }

    public Emprunte prepareCreate() {
        selected = new Emprunte();
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

    }

    public List<Emprunte> getItems() {
            items = getFacade().search(cne, min, max, etat);
        
        return items;
    }

    public Long getCne() {
        return cne;
    }

    public void setCne(Long cne) {
        this.cne = cne;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    public Date getMin() {
        return min;
    }

    public void setMin(Date min) {
        this.min = min;
    }

    public Date getMax() {
        return max;
    }

    public void setMax(Date max) {
        this.max = max;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public List <EmprunteItem> empruteitemslist() {
        System.out.println("controler.EmpruteController.empruteitemslist()"+emprunteItemFacade.getByEmprute(selected));
        return emprunteItemFacade.getByEmprute(selected);
    }    
    
    public List <EmprunteItem> searchEmpruteitems() {
        
        return emprunteItemFacade.search(cne, oeuvre, min, max, etat);
    }
    
    
    public void rendre(EmprunteItem emp) {
        emprunteItemFacade.rendre(emp, new Date());
        JsfUtil.addSuccessMessage("Emprunte Rendu");
    }

    
//Ajouter Oeuvre a une Emprute    
    public void add(Oeuvre o) {
        if(o==null) return;
        

        if(getListitems().size()>=siteFacade.getSite().getMaxreseritem()) {
            JsfUtil.addErrorMessage("Vous aves depasser le nombre de Oeuvre autoriséé");
            return;
        }

        System.out.println("controler.EmpruteController.add()");
        if(getListitems().indexOf(o)>=0) {
            JsfUtil.addErrorMessage("Oeuvre Deja selectionner");
            return;  
        }
        getListitems().add(o);
        JsfUtil.addSuccessMessage("Oeuvre Ajouter");
    }
    
    public void del(Oeuvre o) {
        if(o==null) return;
        
        
        if(getListitems().indexOf(o)==-1) {
            JsfUtil.addErrorMessage("Oeuvre Introuvable");
            return;  
        }
        
        getListitems().remove(o);
        
    }
    
    public void addEmprunte() {
        
        if(getListitems().size()<=0) {
            JsfUtil.addErrorMessage("List des oeuvres Vide");
            return;
        }
        
        ejbFacade.create(selected);
        for(Oeuvre o : getListitems()) {
            EmprunteItem emp = new EmprunteItem(selected, getSelected().getDateRemise(), o);
            emprunteItemFacade.create(emp);
            oeuvreFacade.incNombre(o, 1);
            
            
        }
        
        selected=null;
        listitems=null;
        JsfUtil.addSuccessMessage("Emprunte Ajouter");
        
        

    }
    
    public void clear() {
     cne=null;
    oeuvre=null;
    min=null;
    max=null;
     etat = -1;       
    }
    
    
    
    public Emprunte getEmprute(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    public String replaceReg(String message , String pattern ,String word) {
        
        if(message==null ) return "";
        return message.replace(pattern, word);
        
    }
    
    public boolean checkRendu(Emprunte emprunte) {
        
        for(EmprunteItem e : emprunteItemFacade.getByEmprute(emprunte)) {
            if(e.getDatedepoteffectif()==null) return false;
        }
        return true;
    }
    
    public void voir(Emprunte e) {
        System.out.println("controler.EmpruteController.voir()"+e.getId());
        selected=e;
    }
    
    public void mailAlerte(EmprunteItem emp)   {
        
        if(emp==null) return;
        controler.util.MailUtil mailUtil = new MailUtil();       
        String objet = siteFacade.getSite().getObject();
        objet = replaceReg(objet,"%etudiant%",emp.getEmprute().getEtudiant().toString());
        objet = replaceReg(objet,"%cne%",emp.getEmprute().getEtudiant().getCne().toString());        
        String emailBody = siteFacade.getSite().getObject();
        emailBody = replaceReg(objet,"%oeuvre%",emp.getOeuvre().getNom());
        
        System.out.println("controler.EmpruteController.mailAlerte()"+objet);
        try{
        mailUtil.SendEmail(siteFacade.getSite(),objet,emailBody, "xeo.omar@gmail.com");
        }catch (MessagingException e){
           //e.printStackTrace();
           return;
        }
            JsfUtil.addSuccessMessage("Email Envoyer");
        }
    
    

    public void exporttopdf() throws IOException {
        
        if(getItems()==null || searchEmpruteitems().size()==0) {
            JsfUtil.addErrorMessage("Aucunne emprute Trouver");
            return;
        }
        
        String admin = "admin";
         Map<String, Object> params = new HashMap<String, Object>();
         Date date = new Date();
        params.put("date", date);
        params.put("cne", cne==null||cne.equals("")?"tous Les Etudiant":cne);
        params.put("etudiant", cne==null||cne.equals("")?"tous Les Etudiant":getItems().get(0).getEtudiant().toString());
        params.put("oeuvre", oeuvre==null?"Tous Les Oeuvre":oeuvre.getNom());
        params.put("admin", admin);
        params.put("etat", etat(etat));
        try {
            controler.util.PdfUtil.generatePdf(searchEmpruteitems(), params, "Emprute-"+date, "/emprunte.jrxml");
       
        } catch (Exception e) {
            //Logger.getLogger(InventaireFacade.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            return;
        }       
        FacesContext.getCurrentInstance().responseComplete();         
    }    
    
    public String etat(int n) {
        
        if(n==1) return "Rendu";
        if(n==0) return " Non Rendu";
        return "Tous";
    }
    
    
    @FacesConverter(forClass = Emprunte.class)
    public static class EmpruteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmpruteController controller = (EmpruteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "empruteController");
            return controller.getEmprute(getKey(value));
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
            if (object instanceof Emprunte) {
                Emprunte o = (Emprunte) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Emprunte.class.getName()});
                return null;
            }
        }

    }

}
