package controler.util;
import bean.Admin;
import bean.Etudiant;
import bean.Oeuvre;
import bean.Site;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

public class SessionUtil {


    private static final SessionUtil instance = new SessionUtil();
    private SessionUtil() {
    }

    
    
    public Site Site() {

        return null;
    }
    

    
    
   
    public static SessionUtil getInstance() {
        return instance;
    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
    


    public static void redirect(String pagePath) throws IOException {
        if (!pagePath.endsWith(".xhtml")) {
            pagePath += ".xhtml";
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(pagePath);

    }

    private static boolean isContextOk(FacesContext fc) {
        boolean res = (fc != null
                && fc.getExternalContext() != null
                && fc.getExternalContext().getSession(false) != null);
        return res;
    }

    private static HttpSession getSession(FacesContext fc) {
        return (HttpSession) fc.getExternalContext().getSession(false);
    }

    public static Object getAttribute(String cle) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Object res = null;
        if (isContextOk(fc)) {
            res = getSession(fc).getAttribute(cle);
        }
        return res;
    }

    public static void setAttribute(String cle, Object valeur) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null && fc.getExternalContext() != null) {
            getSession(fc).setAttribute(cle, valeur);
        }
    }
    
    public static void deconnection() {
        setuser(null);
        getSession().invalidate();
    }   
    
    public static void deconnectionAdmin() {
        setadmin(null);
        getSession().invalidate();
    }   
    
    public static void setuser(Etudiant e) { 
         if(e==null) return ;
         setAttribute("etudiant",e);
        // setAttribute("droit",s.getDroit());
    }
     
    public static Etudiant getuser() {     
     return (Etudiant) getAttribute("etudiant");
    } 
     
    public static void setadmin(Admin e) { 
         if(e==null) return ;
         setAttribute("admin",e);
    }
     
    public static Admin getadmin() {     
     return (Admin) getAttribute("admin");
    }      
     
     /*
    public static void setOeuvre(Oeuvre e) {
        
        if(e==null) return;
        List <Oeuvre> list = getOeuvre() ;
        if(getOeuvre()==null) {
            list = new ArrayList <Oeuvre>();
        }
        list.add(e);
           
    }
    
    public static void deleteOuvre(Oeuvre e) {
        List <Oeuvre> list = getOeuvre() ;
        if(e==null || list ==null) return;
        int indice = list.indexOf(e);
        if(indice>0) 
            list.remove(e);
     
    }
    
    public static List <Oeuvre> getOeuvre() {
     return (List <Oeuvre>) getAttribute("oeuvre");
                   
    }
*/
    
    

    
}
