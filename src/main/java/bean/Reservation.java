/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author omar
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)    
    private Date dateDemande;
    @Temporal(javax.persistence.TemporalType.DATE)    
    private Date dateRemise;
    private boolean confirmer=false;
    private boolean traiter=false;
    
    @ManyToOne
    private Etudiant etudiant;

    public Reservation() {
        
    }  

    public Reservation(Date dateDemande, Date dateRemise, Etudiant etudiant) {
        this.dateDemande = dateDemande;
        this.dateRemise = dateRemise;
        this.etudiant = etudiant;
    }

    public boolean isTraiter() {
        return traiter;
    }

    public void setTraiter(boolean traiter) {
        this.traiter = traiter;
    }
    
    

   
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Date getDateRemise() {
        return dateRemise;
    }

    public void setDateRemise(Date dateRemise) {
        this.dateRemise = dateRemise;
    }

    public boolean isConfirmer() {
        return confirmer;
    }

    public void setConfirmer(boolean confirmer) {
        this.confirmer = confirmer;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.demande[ id=" + id + " ]";
    }
    
}
