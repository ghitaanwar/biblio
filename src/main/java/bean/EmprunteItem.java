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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author omar
 */
@Entity
public class EmprunteItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Emprunte emprute;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDepotPrevue  ;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datedepoteffectif;

    @OneToOne
    private Oeuvre oeuvre;

    public EmprunteItem() {
    }

    public EmprunteItem(Emprunte emprute, Date dateDepotPrevue, Oeuvre oeuvre) {
        this.emprute = emprute;
        this.dateDepotPrevue = dateDepotPrevue;
        this.oeuvre = oeuvre;
    }

    
    public EmprunteItem(Emprunte e) {
        if(e!=null)
        dateDepotPrevue= emprute.getDateRemise();
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDepotPrevue() {
        return dateDepotPrevue;
    }

    public void setDateDepotPrevue(Date dateDepotPrevue) {
        this.dateDepotPrevue = dateDepotPrevue;
    }

    public Date getDatedepoteffectif() {
        return datedepoteffectif;
    }

    public void setDatedepoteffectif(Date datedepoteffectif) {
        this.datedepoteffectif = datedepoteffectif;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    public Emprunte getEmprute() {
        return emprute;
    }

    public void setEmprute(Emprunte emprute) {
        this.emprute = emprute;
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
        if (!(object instanceof EmprunteItem)) {
            return false;
        }
        EmprunteItem other = (EmprunteItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.EmpruteItem[ id=" + id + " ]";
    }
    
}
