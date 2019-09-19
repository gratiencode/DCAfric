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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "posseder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Posseder.findAll", query = "SELECT p FROM Posseder p"),
    @NamedQuery(name = "Posseder.findById", query = "SELECT p FROM Posseder p WHERE p.possederPK.id = :id"),
    @NamedQuery(name = "Posseder.findByIdKiosque", query = "SELECT p FROM Posseder p WHERE p.possederPK.idKiosque = :idKiosque"),
    @NamedQuery(name = "Posseder.findBySearchingPerIdKiosque", query = "SELECT p FROM Posseder p WHERE p.possederPK.idKiosque LIKE :idKiosque"),
    @NamedQuery(name = "Posseder.findByIdMateriel", query = "SELECT p FROM Posseder p WHERE p.possederPK.idMateriel = :idMateriel"),
    @NamedQuery(name = "Posseder.findBySearch", query = "SELECT p FROM Posseder p WHERE p.possederPK.idMateriel = :idMateriel AND p.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Posseder.findByAutreInfo", query = "SELECT p FROM Posseder p WHERE p.autreInfo = :autreInfo"),
    @NamedQuery(name = "Posseder.paginate", query = "SELECT p FROM Posseder p WHERE p.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Posseder.findByDate", query = "SELECT p FROM Posseder p WHERE p.date = :date")})
public class Posseder implements Serializable {
    @JoinColumn(name = "id_kiosque", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Kiosque kiosque;
    @JoinColumn(name = "id_materiel", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materiel materiel;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PossederPK possederPK;
    @Size(max = 64)
    @Column(name = "autre_info")
    private String autreInfo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Posseder() {
    }

    public Posseder(PossederPK possederPK) {
        this.possederPK = possederPK;
    }

    public Posseder(PossederPK possederPK, Date date) {
        this.possederPK = possederPK;
        this.date = date;
    }

    public Posseder(int id, String idKiosque, String idMateriel) {
        this.possederPK = new PossederPK(id, idKiosque, idMateriel);
    }

    public PossederPK getPossederPK() {
        return possederPK;
    }

    public void setPossederPK(PossederPK possederPK) {
        this.possederPK = possederPK;
    }

    public String getAutreInfo() {
        return autreInfo;
    }

    public void setAutreInfo(String autreInfo) {
        this.autreInfo = autreInfo;
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
        hash += (possederPK != null ? possederPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posseder)) {
            return false;
        }
        Posseder other = (Posseder) object;
        if ((this.possederPK == null && other.possederPK != null) || (this.possederPK != null && !this.possederPK.equals(other.possederPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Posseder[ possederPK=" + possederPK + " ]";
    }

    public Kiosque getKiosque() {
        return kiosque;
    }

    public void setKiosque(Kiosque kiosque) {
        this.kiosque = kiosque;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }
    
}
