/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERVER
 */
@Entity
@Table(name = "obtenir")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obtenir.findAll", query = "SELECT o FROM Obtenir o"),
    @NamedQuery(name = "Obtenir.findById", query = "SELECT o FROM Obtenir o WHERE o.obtenirPK.id = :id"),
    @NamedQuery(name = "Obtenir.findByIdAgent", query = "SELECT o FROM Obtenir o WHERE o.obtenirPK.idAgent = :idAgent"),
    @NamedQuery(name = "Obtenir.findByKiosqOnPeriod", query = "SELECT o FROM Obtenir o WHERE o.obtenirPK.idKiosq LIKE :idKiosque AND o.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Obtenir.findByIdKiosq", query = "SELECT o FROM Obtenir o WHERE o.obtenirPK.idKiosq = :idKiosq ORDER BY o.date DESC"),
    @NamedQuery(name = "Obtenir.findLast", query = "SELECT o FROM Obtenir o WHERE o.obtenirPK.idAgent = :idAgent ORDER BY o.date DESC"),
    @NamedQuery(name = "Obtenir.findBySearchDate", query = "SELECT o FROM Obtenir o WHERE o.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Obtenir.findByDate", query = "SELECT o FROM Obtenir o WHERE o.date = :date")})
public class Obtenir implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObtenirPK obtenirPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Obtenir() {
    }

    public Obtenir(ObtenirPK obtenirPK) {
        this.obtenirPK = obtenirPK;
    }

    public Obtenir(ObtenirPK obtenirPK, Date date) {
        this.obtenirPK = obtenirPK;
        this.date = date;
    }

    public Obtenir(long id, String idAgent, String idKiosq) {
        this.obtenirPK = new ObtenirPK(id, idAgent, idKiosq);
    }

    public ObtenirPK getObtenirPK() {
        return obtenirPK;
    }

    public void setObtenirPK(ObtenirPK obtenirPK) {
        this.obtenirPK = obtenirPK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (obtenirPK != null ? obtenirPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obtenir)) {
            return false;
        }
        Obtenir other = (Obtenir) object;
        if ((this.obtenirPK == null && other.obtenirPK != null) || (this.obtenirPK != null && !this.obtenirPK.equals(other.obtenirPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Obtenir[ obtenirPK=" + obtenirPK + " ]";
    }
    
}
