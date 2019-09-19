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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERVER
 */
@Entity
@Table(name = "recquisitionner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recquisitionner.findAll", query = "SELECT r FROM Recquisitionner r"),
    @NamedQuery(name = "Recquisitionner.findById", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.id = :id"),
    @NamedQuery(name = "Recquisitionner.findByIdKiosque", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.idKiosque = :idKiosque"),
    @NamedQuery(name = "Recquisitionner.findByIdKiosqueOnPeriod", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.idKiosque LIKE :idKiosque AND r.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Recquisitionner.findByIdProduit", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.idProduit = :idProduit"),
    @NamedQuery(name = "Recquisitionner.findSumRecq", query = "SELECT SUM(r.quant) FROM Recquisitionner r WHERE r.recquisitionnerPK.idProduit = :idProduit AND r.recquisitionnerPK.idKiosque = :idKiosque AND r.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Recquisitionner.findAllRecqForProduct", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.idProduit = :idProduit AND r.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Recquisitionner.findByIdProduitAndIdKiosque", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.idProduit = :idProduit AND r.recquisitionnerPK.idKiosque = :idKiosque AND r.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Recquisitionner.findQuantiteByIdProduitByIdKiosque", query = "SELECT SUM(r.quant) FROM Recquisitionner r WHERE r.recquisitionnerPK.idProduit = :idProduit AND r.recquisitionnerPK.idKiosque = :idKiosque AND r.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Recquisitionner.findByQuant", query = "SELECT r FROM Recquisitionner r WHERE r.quant = :quant"),
    @NamedQuery(name = "Recquisitionner.findByPrixUnit", query = "SELECT r FROM Recquisitionner r WHERE r.prixUnit = :prixUnit"),
    @NamedQuery(name = "Recquisitionner.findDernierRecquisition", query = "SELECT r FROM Recquisitionner r WHERE r.recquisitionnerPK.idProduit = :idProduit ORDER BY r.date DESC"),
    @NamedQuery(name = "Recquisitionner.findByDevise", query = "SELECT r FROM Recquisitionner r WHERE r.devise = :devise"),
    @NamedQuery(name = "Recquisitionner.findOnPeriod", query = "SELECT r FROM Recquisitionner r WHERE r.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Recquisitionner.findByDate", query = "SELECT r FROM Recquisitionner r WHERE r.date = :date"),
    @NamedQuery(name = "Recquisitionner.findByCommentaire", query = "SELECT r FROM Recquisitionner r WHERE r.commentaire = :commentaire")})
public class Recquisitionner implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecquisitionnerPK recquisitionnerPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quant")
    private double quant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prix_unit")
    private double prixUnit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "devise")
    private String devise;
    @Basic(optional = false)
    @NotNull
    @Column(name = "_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 500)
    @Column(name = "commentaire")
    private String commentaire;

    public Recquisitionner() {
    }

    public Recquisitionner(RecquisitionnerPK recquisitionnerPK) {
        this.recquisitionnerPK = recquisitionnerPK;
    }

    public Recquisitionner(RecquisitionnerPK recquisitionnerPK, double quant, double prixUnit, String devise, Date date) {
        this.recquisitionnerPK = recquisitionnerPK;
        this.quant = quant;
        this.prixUnit = prixUnit;
        this.devise = devise;
        this.date = date;
    }

    public Recquisitionner(long id, String idKiosque, String idProduit) {
        this.recquisitionnerPK = new RecquisitionnerPK(id, idKiosque, idProduit);
    }

    public RecquisitionnerPK getRecquisitionnerPK() {
        return recquisitionnerPK;
    }

    public void setRecquisitionnerPK(RecquisitionnerPK recquisitionnerPK) {
        this.recquisitionnerPK = recquisitionnerPK;
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }

    public double getPrixUnit() {
        return prixUnit;
    }

    public void setPrixUnit(double prixUnit) {
        this.prixUnit = prixUnit;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recquisitionnerPK != null ? recquisitionnerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recquisitionner)) {
            return false;
        }
        Recquisitionner other = (Recquisitionner) object;
        if ((this.recquisitionnerPK == null && other.recquisitionnerPK != null) || (this.recquisitionnerPK != null && !this.recquisitionnerPK.equals(other.recquisitionnerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Recquisitionner[ recquisitionnerPK=" + recquisitionnerPK + " ]";
    }

}
