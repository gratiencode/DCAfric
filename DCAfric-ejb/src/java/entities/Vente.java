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
@Table(name = "vente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vente.findAll", query = "SELECT v FROM Vente v"),
    @NamedQuery(name = "Vente.findById", query = "SELECT v FROM Vente v WHERE v.ventePK.id = :id"),
    @NamedQuery(name = "Vente.findByIdProduit", query = "SELECT v FROM Vente v WHERE v.ventePK.idProduit = :idProduit"),
    @NamedQuery(name = "Vente.findByReference", query = "SELECT v FROM Vente v WHERE v.ventePK.reference = :reference"),
    @NamedQuery(name = "Vente.findSumByReference", query = "SELECT SUM(v.quantite) FROM Vente v WHERE v.ventePK.reference = :reference AND v.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Vente.findSumQuantByReference", query = "SELECT SUM(v.quantite) FROM Vente v WHERE v.ventePK.reference LIKE :reference AND v.ventePK.idProduit = :idProduit AND v.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Vente.findSumQuantVendu", query = "SELECT SUM(v.quantite) FROM Vente v WHERE v.ventePK.reference LIKE :reference AND v.ventePK.idProduit = :idProduit"),
    @NamedQuery(name = "Vente.findByQuantite", query = "SELECT v FROM Vente v WHERE v.quantite = :quantite"),
    @NamedQuery(name = "Vente.findSumQVendu", query = "SELECT SUM(v.quantite) FROM Vente v WHERE v.date BETWEEN :date1 AND :date2 AND v.ventePK.idProduit = :prod"),
    @NamedQuery(name = "Vente.findRecetteTotalByDevise", query = "SELECT SUM(v.mantant) FROM Vente v WHERE v.devise = :devise AND v.date BETWEEN :date1 AND :date2 GROUP BY v.devise"),
    @NamedQuery(name = "Vente.findSumByDeviseAndProduct", query = "SELECT SUM(v.mantant) FROM Vente v WHERE v.devise = :devise AND v.ventePK.idProduit = :idProduit AND v.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Vente.sumRecetteForKiosque", query = "SELECT SUM(v.mantant) FROM Vente v WHERE v.ventePK.reference LIKE :ref AND v.devise = :devise AND v.date BETWEEN :date11 AND :date22"),
    @NamedQuery(name = "Vente.findSumProduct", query = "SELECT SUM(v.mantant) FROM Vente v WHERE v.ventePK.idProduit = :idProduit AND v.date BETWEEN :date1 AND :date2 GROUP BY v.ventePK.idProduit"),
    @NamedQuery(name = "Vente.findPerProduct", query = "SELECT v FROM Vente v WHERE v.ventePK.idProduit = :idProduit AND v.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Vente.findPerProductForRecette", query = "SELECT v FROM Vente v WHERE v.ventePK.idProduit LIKE :idProduit AND v.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Vente.findRecetteTotalByReference", query = "SELECT DISTINCT SUM(v.mantant) FROM Vente v WHERE v.ventePK.reference = :ref AND v.date BETWEEN :date1 AND :date2 GROUP BY v.ventePK.reference"),
    @NamedQuery(name = "Vente.findRecetteTotalByReferenceDevise", query = "SELECT DISTINCT SUM(v.mantant) FROM Vente v WHERE v.ventePK.reference = :ref AND v.devise = :devise AND v.date BETWEEN :date1 AND :date2 GROUP BY v.ventePK.reference"),
    @NamedQuery(name = "Vente.findByMantant", query = "SELECT v FROM Vente v WHERE v.mantant = :mantant"),
    @NamedQuery(name = "Vente.paginate", query = "SELECT v FROM Vente v WHERE v.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Vente.findByDevise", query = "SELECT v FROM Vente v WHERE v.devise = :devise"),
    @NamedQuery(name = "Vente.findByDate", query = "SELECT v FROM Vente v WHERE v.date = :date")})
public class Vente implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VentePK ventePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite")
    private double quantite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mantant")
    private double mantant;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "devise")
    private String devise;
    @Basic(optional = false)
    @NotNull
    @Column(name = "_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Vente() {
    }

    public Vente(VentePK ventePK) {
        this.ventePK = ventePK;
    }

    public Vente(VentePK ventePK, double quantite, double mantant, String devise, Date date) {
        this.ventePK = ventePK;
        this.quantite = quantite;
        this.mantant = mantant;
        this.devise = devise;
        this.date = date;
    }

    public Vente(int id, String idProduit, String reference) {
        this.ventePK = new VentePK(id, idProduit, reference);
    }

    public VentePK getVentePK() {
        return ventePK;
    }

    public void setVentePK(VentePK ventePK) {
        this.ventePK = ventePK;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getMantant() {
        return mantant;
    }

    public void setMantant(double mantant) {
        this.mantant = mantant;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ventePK != null ? ventePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vente)) {
            return false;
        }
        Vente other = (Vente) object;
        if ((this.ventePK == null && other.ventePK != null) || (this.ventePK != null && !this.ventePK.equals(other.ventePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Vente[ ventePK=" + ventePK + " ]";
    }

}
