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
@Table(name = "consommer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consommer.findAll", query = "SELECT c FROM Consommer c"),
    @NamedQuery(name = "Consommer.findById", query = "SELECT c FROM Consommer c WHERE c.consommerPK.id = :id"),
    @NamedQuery(name = "Consommer.findByIdService", query = "SELECT c FROM Consommer c WHERE c.consommerPK.idService = :idService"),
    @NamedQuery(name = "Consommer.findByIdClient", query = "SELECT c FROM Consommer c WHERE c.consommerPK.idClient = :idClient"),
    @NamedQuery(name = "Consommer.findByIdKiosq", query = "SELECT c FROM Consommer c WHERE c.idKiosq = :idKiosq"),
    @NamedQuery(name = "Consommer.findByLikeIdKiosq", query = "SELECT c FROM Consommer c WHERE c.idKiosq LIKE :idKiosq"),
    @NamedQuery(name = "Consommer.findByQuantite", query = "SELECT c FROM Consommer c WHERE c.quantite = :quantite"),
    @NamedQuery(name = "Consommer.findByMontant", query = "SELECT c FROM Consommer c WHERE c.montant = :montant"),
    @NamedQuery(name = "Consommer.findByDevise", query = "SELECT c FROM Consommer c WHERE c.devise = :devise"),
    @NamedQuery(name = "Consommer.findByDate", query = "SELECT c FROM Consommer c WHERE c.date = :date"),
    @NamedQuery(name = "Consommer.findOnPeriod", query = "SELECT c FROM Consommer c WHERE c.date BETWEEN :date1 AND :date2 "),
    @NamedQuery(name = "Consommer.findRecetteForService", query = "SELECT c FROM Consommer c WHERE c.date BETWEEN :date1 AND :date2 GROUP BY c.consommerPK.idService , c.devise"),
    @NamedQuery(name = "Consommer.findByServiceOnPeriod", query = "SELECT c FROM Consommer c WHERE c.consommerPK.idService = :servc AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Consommer.findByKiosqOnPeriod", query = "SELECT c FROM Consommer c WHERE c.idKiosq LIKE :kiosq AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Consommer.findSumDeviseOnPeriod", query = "SELECT SUM(c.montant) FROM Consommer c WHERE c.devise = :devise AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Consommer.findSumServiceOnPeriod", query = "SELECT SUM(c.montant) FROM Consommer c WHERE c.consommerPK.idService = :idService AND c.devise = :devise AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Consommer.findQuantityForServiceOnPeriod", query = "SELECT SUM(c.quantite) FROM Consommer c WHERE c.consommerPK.idService = :idService AND c.devise = :devise AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Consommer.findSumBySrvcWithDeviseOnPeriod", query = "SELECT SUM(c.montant) FROM Consommer c WHERE c.devise = :devise AND c.consommerPK.idService = :idServ AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Consommer.findByEtat", query = "SELECT c FROM Consommer c WHERE c.etat = :etat"),
    @NamedQuery(name = "Consommer.findByAutreInfo", query = "SELECT c FROM Consommer c WHERE c.autreInfo = :autreInfo")})
public class Consommer implements Serializable {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "quantite")
    private Double quantite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "methode")
    private String methode;
    @Column(name = "valide")
    private Boolean valide;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConsommerPK consommerPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_kiosq")
    private String idKiosq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "libelle")
    private String libelle;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "etat")
    private String etat;
    @Size(max = 64)
    @Column(name = "autre_info")
    private String autreInfo;

    public Consommer() {
    }

    public Consommer(ConsommerPK consommerPK) {
        this.consommerPK = consommerPK;
    }

    public Consommer(ConsommerPK consommerPK, String idKiosq, String libelle, double quantite, double montant, String devise, Date date, String etat, boolean valide) {
        this.consommerPK = consommerPK;
        this.idKiosq = idKiosq;
        this.libelle = libelle;
        this.quantite = quantite;
        this.montant = montant;
        this.devise = devise;
        this.date = date;
        this.etat = etat;
        this.valide = valide;
    }

    public Consommer(long id, String idService, String idClient) {
        this.consommerPK = new ConsommerPK(id, idService, idClient);
    }

    public ConsommerPK getConsommerPK() {
        return consommerPK;
    }

    public void setConsommerPK(ConsommerPK consommerPK) {
        this.consommerPK = consommerPK;
    }

    public String getIdKiosq() {
        return idKiosq;
    }

    public void setIdKiosq(String idKiosq) {
        this.idKiosq = idKiosq;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    public String getAutreInfo() {
        return autreInfo;
    }

    public void setAutreInfo(String autreInfo) {
        this.autreInfo = autreInfo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consommerPK != null ? consommerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consommer)) {
            return false;
        }
        Consommer other = (Consommer) object;
        if ((this.consommerPK == null && other.consommerPK != null) || (this.consommerPK != null && !this.consommerPK.equals(other.consommerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Consommer[ consommerPK=" + consommerPK + " ]";
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }
    
}
