/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author omar
 */
@Entity
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String adress;
    private String tel;
    private String email;
    private String smtphost;
    private String smtppass;
    private String smtpuser;
    private int maxdayafterreserve;
    private int maxreseritem;
    private String object;
    private String message;

    public Site() {
        maxdayafterreserve=3;
        maxreseritem=3;
    }
    

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSmtphost() {
        return smtphost;
    }

    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    public String getSmtppass() {
        return smtppass;
    }

    public void setSmtppass(String smtppass) {
        this.smtppass = smtppass;
    }

    public String getSmtpuser() {
        return smtpuser;
    }

    public void setSmtpuser(String smtpuser) {
        this.smtpuser = smtpuser;
    }

    public int getMaxdayafterreserve() {
        return maxdayafterreserve;
    }

    public void setMaxdayafterreserve(int maxdayafterreserve) {
        this.maxdayafterreserve = maxdayafterreserve;
    }

    public int getMaxreseritem() {
        return maxreseritem;
    }

    public void setMaxreseritem(int maxreseritem) {
        this.maxreseritem = maxreseritem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    

    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        return "bean.Site[ id=" + id + " ]";
    }
    
}
