package controler;

import bean.Emprunte;
import bean.EmprunteItem;
import bean.Etudiant;
import bean.Oeuvre;
import bean.Reservation;
import bean.ReservationItem;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.ReservationFacade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
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
import service.EmprunteFacade;
import service.EmprunteItemFacade;
import service.OeuvreFacade;
import service.ReservationItemFacade;

@Named("reservationController")
@SessionScoped
public class ReservationController implements Serializable {

    @EJB
    private OeuvreFacade oeuvreFacade;

    @EJB
    private EmprunteItemFacade emprunteItemFacade;

    @EJB
    private EmprunteFacade emprunteFacade;

    @EJB
    private ReservationItemFacade reservationItemFacade;

    @EJB
    private service.ReservationFacade ejbFacade;
    private List<Reservation> items = null;
    private Reservation selected;
    private Long cne;
    private Oeuvre oeuvre;
    private Date min;
    private Date max;
    private int etat=-2;

    private List <ReservationItem> reservtoEmprunt;
    
    
    
    public ReservationController() {
    }

    public Reservation getSelected() {
        return selected;
    }

    public void setSelected(Reservation selected) {
        this.selected = selected;
    }


    private ReservationFacade getFacade() {
        return ejbFacade;
    }

    public Reservation prepareCreate() {
        selected = new Reservation();
        return selected;
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

    public List<ReservationItem> getReservtoEmprunt() {
        return reservtoEmprunt;
    }

    public void setReservtoEmprunt(List<ReservationItem> reservtoEmprunt) {
        this.reservtoEmprunt = reservtoEmprunt;
    }



    public void create() {
        
    }

    public void update() {
    }

    public void destroy() {

    }

    public List<Reservation> getItems() {
            items = getFacade().search(cne, null, min, max, etat);
        return items;
    }
    
    public void reset() {
    cne = null;
    oeuvre = null;
    min =null;
    max = null;
    etat=-2;       
    }
    
    public List <String> reservationstate(Reservation r ) {
        if(r==null) return null;
        if(ejbFacade.state(r)==1) return Arrays.asList("Confirmer","green-bg");
        if(ejbFacade.state(r)==0) return Arrays.asList("En Attente","yellow-bg");
         return Arrays.asList("Annuler","red-bg");
        
    }
    
    public int state(Reservation r ) {
        return ejbFacade.state(r);
    }

    public void Annuler(Reservation r) {
        ejbFacade.anuller(r);
        JsfUtil.addSuccessMessage("Reservation Anuller");
    }

    public Reservation getReservation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List <ReservationItem> reservationItemsbyReservation() {
                if(selected==null)return null;
        return reservationItemFacade.getByReservation(selected);
   
    }
    
    public void view(Reservation res) {
        selected=res;
    }
    
    public void toEmprunte() throws IOException {
        if(reservtoEmprunt==null || reservtoEmprunt.size()==0) {
            JsfUtil.addErrorMessage("Selectinner les oeuvres");
            return ;
        }
        List <ReservationItem> list = reservtoEmprunt;
        List <ReservationItem> list2 = reservationItemsbyReservation();
        
        Emprunte emp = new Emprunte(selected.getDateDemande(), selected.getDateRemise(), selected.getEtudiant());
        emprunteFacade.create(emp);
        
       for(ReservationItem rsi: list) {
        EmprunteItem emprunteItem = new EmprunteItem(emp, selected.getDateRemise(), rsi.getOeuvre());
            emprunteItemFacade.create(emprunteItem);
          list2.remove(list2.indexOf(rsi));
        }
               
        for(ReservationItem rs:list2){
            oeuvreFacade.incNombre(rs.getOeuvre(), -1);
            reservationItemFacade.remove(rs);
        }
        ejbFacade.confirmer(selected, true);
        reservtoEmprunt =null;
        selected=null;
        SessionUtil.redirect("index");
        JsfUtil.addSuccessMessage("Emprunte Ajouter");
        
        
    }

    @FacesConverter(forClass = Reservation.class)
    public static class ReservationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReservationController controller = (ReservationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reservationController");
            return controller.getReservation(getKey(value));
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
            if (object instanceof Reservation) {
                Reservation o = (Reservation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Reservation.class.getName()});
                return null;
            }
        }

    }

}
