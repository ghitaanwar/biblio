package controler;

import bean.Emprunte;
import bean.EmprunteItem;
import bean.Etudiant;
import bean.Oeuvre;
import bean.Reservation;
import bean.ReservationItem;
import bean.Site;
import controler.util.DateUtil;
import controler.util.JsfUtil;
import controler.util.SessionUtil;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import service.EmprunteFacade;
import service.EmprunteItemFacade;
import service.SiteFacade;

@Named("homeController")
@SessionScoped
public class HomeController implements Serializable {

    @EJB
    private EmprunteItemFacade empruteItemFacade;

    @EJB
    private SiteFacade siteFacade;

    @EJB
    private EmprunteFacade empruteFacade;

    @EJB
    private service.CategorieFacade catEjbFacade;
    @EJB 
    private service.ReservationFacade reservationFacade ; 
    @EJB 
    private service.ReservationItemFacade reservationItemFacade  ;
    
    private Etudiant user;
    private List <Oeuvre> books;
    private Date datedemande;
    private Date dateremise;
    private Reservation reservation;
    private List <Reservation> reservations;
    private String page="home";
    
    
    public Etudiant getUser() {
          user = controler.util.SessionUtil.getuser();
          return user;
    }

    public void setUser(Etudiant user) {
        this.user = user;
    }

    public List<Oeuvre> getBooks() {
        return books;
    }

    public void setBooks(List<Oeuvre> books) {
        this.books = books;
    
    }

    public Date getDatedemande() {
       if(datedemande==null)    datedemande=new Date();
        return datedemande;
    }

    public void setDatedemande(Date datedemande) {
        this.datedemande = datedemande;
    }

    public Date mindateRemise() {
                return DateUtil.incrimentDate(getDatedemande(), 3);

        
    }
    public Date getDateremise() {
        return dateremise;
    }

    public void setDateremise(Date dateremise) {
        this.dateremise = dateremise;
    }

    /*public Configuration config() {
        return  new C
        //ConfigurationFacade().config();
    }*/
    
    public Site site(){
        return siteFacade.findAll().get(0);
    }
    
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
    
    
    public String pageActive(String s) throws IOException {
        if(s.equals(getPage())) return "active";
        return "";
    }
    
    public void addReservedbook(Oeuvre book) {
        if(book==null) return;
        if(books==null) books = new ArrayList<Oeuvre>();
        
        System.out.println("1-controler.HomeController.addReservedbook()"+books.size());
        if(getBooks().size()>=siteFacade.getSite().getMaxreseritem()) {
            JsfUtil.addErrorMessage("Vous aves deppaser le nombre de Oeuvre autoriséé");
            return;
        }
        if(books.indexOf(book)!=-1) {
             JsfUtil.addErrorMessage("Oeuvre Existant");
             return;
        }
        
        books.add(book);
        JsfUtil.addSuccessMessage("Oeuvre Ajouter au List ");
        System.out.println("2-controler.HomeController.addReservedbook()"+books);  
    }
    
    
    public void delReservedbook(Oeuvre book) {
        if(book==null) return;        
        if(books==null) return;
        System.out.println("1-controler.HomeController.delReservedbook()"+books);
       /* if(books.indexOf(book)!=-1) {
             JsfUtil.addErrorMessage("Oeuvre Inexistant");
             return;
        }*/ 
        try {
        books.remove(book);
        }catch(Exception e){ return;}
        JsfUtil.addSuccessMessage("Oeuvre Supprimer de la List ");
        System.out.println("2-controler.HomeController.delReservedbook()"+books);
    }        
        
    public void makeReservation() throws IOException {
        
        if(getUser()==null) { SessionUtil.redirect("login"); return;}
        if(dateremise==null) {JsfUtil.addErrorMessage("Date Remise Introuvable"); return;}
        if(books==null || books.size()<1) {JsfUtil.addErrorMessage("Liste des Oeuvre Vide"); return;}
        
        if(reservationFacade.search(user.getCne(), null, null, null, 0).isEmpty()==false ){
                JsfUtil.addErrorMessage("Operation Impossible , Vous avez des Reservation en attente");
                return;            
        }
        
        if(empruteItemFacade.search(user.getCne(), null, null, null, 0).isEmpty()==false ){
                JsfUtil.addErrorMessage("Operation Impossible , Vous Devez Rendre Les Oeuvre Empruntés ");
                return;            
        }        
        Reservation reserv = new Reservation(getDatedemande(), getDateremise(), user);
        reservationFacade.create(reserv);
        for(Oeuvre bk:books) {
            ReservationItem ri = new ReservationItem(reserv, bk);
            reservationItemFacade.create(ri);
        }
        
        books=null;
        dateremise=null;
        datedemande=null;
        JsfUtil.addSuccessMessage("Reservasion Ajouter avec Sucess");
        SessionUtil.redirect("recherche");
    }
 
    public HomeController() {
    }
    
    public void checklogin() throws IOException {
                if(getUser()==null) SessionUtil.redirect("login.xhtml");

    }

    public List<Emprunte> empruteByEtudiant()  {

        if(getUser()==null)return null;
        return empruteFacade.getByEtudiant(getUser());
        
    }
    
    public List <Reservation> reservationbyEtudient() {
        if(getUser()==null)return null;
        return reservationFacade.reservationByEtudiant(getUser());
    }
    
    public List <ReservationItem> reservationItemsbyReservation() {
                if(reservation==null)return null;
        return reservationItemFacade.getByReservation(reservation);
        
   
    }
    
    public List <EmprunteItem> empruteItembyetudian() {
            
        return empruteItemFacade.getByEtudiant(getUser());
    }
    
    public void deconnection() throws IOException {
        user=null;
        SessionUtil.deconnection();
        SessionUtil.redirect("home");
    }
    
    public void selectReservation(Reservation reservation) {

        setReservation(reservation);
        System.out.println("controler.HomeController.selectReservation()" + reservation);
    }
    
    public String couleurAndFont(int num){
        List <String> couleur = Arrays.asList("bg-yellow","bg-light-green","bg-blue","bg-red","bg-violet","bg-green");
        return couleur.get(num%6);
         
    }
    
    

    

}
