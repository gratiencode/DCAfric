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
import javax.persistence.Lob;
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
@Table(name = "commande")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commande.findAll", query = "SELECT c FROM Commande c"),
    @NamedQuery(name = "Commande.findById", query = "SELECT c FROM Commande c WHERE c.commandePK.id = :id"),
    @NamedQuery(name = "Commande.findByIdKiosque", query = "SELECT c FROM Commande c WHERE c.commandePK.idKiosque = :idKiosque"),
    @NamedQuery(name = "Commande.findByIdClient", query = "SELECT c FROM Commande c WHERE c.commandePK.idClient = :idClient"),
    @NamedQuery(name = "Commande.findByReference", query = "SELECT c FROM Commande c WHERE c.commandePK.reference = :reference"),
    @NamedQuery(name = "Commande.searchByReference", query = "SELECT c FROM Commande c WHERE c.commandePK.reference LIKE :reference"),
    @NamedQuery(name = "Commande.findByKiosqOnMobile", query = "SELECT c FROM Commande c WHERE c.commandePK.idKiosque LIKE :idKiosque AND c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Commande.findOnPeriod", query = "SELECT c FROM Commande c WHERE c.date BETWEEN :date1 AND :date2"),
    @NamedQuery(name = "Commande.findOnPeriodByKiosque", query = "SELECT c FROM Commande c WHERE c.commandePK.idKiosque = :kiosque AND c.date BETWEEN :date1 AND :date2 GROUP BY c.commandePK.reference"),
    @NamedQuery(name = "Commande.findByNombreArticle", query = "SELECT c FROM Commande c WHERE c.nombreArticle = :nombreArticle"),
    @NamedQuery(name = "Commande.findByDate", query = "SELECT c FROM Commande c WHERE c.date = :date")})
public class Commande implements Serializable {
    @Column(name = "nombre_article")
    private Integer nombreArticle;
    @Column(name = "valide")
    private Boolean valide;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "methode")
    private String methode;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommandePK commandePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "libelle")
    private String libelle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "_date_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Lob
    @Size(max = 65535)
    @Column(name = "comment")
    private String comment;

    public Commande() {
    }

    public Commande(CommandePK commandePK) {
        this.commandePK = commandePK;
    }

    public Commande(CommandePK commandePK, String libelle, int nombreArticle, Date date, boolean valide) {
        this.commandePK = commandePK;
        this.libelle = libelle;
        this.nombreArticle = nombreArticle;
        this.date = date;
        this.valide = valide;
    }

    public Commande(long id, String idKiosque, String idClient, String reference) {
        this.commandePK = new CommandePK(id, idKiosque, idClient, reference);
    }

    public CommandePK getCommandePK() {
        return commandePK;
    }

    public void setCommandePK(CommandePK commandePK) {
        this.commandePK = commandePK;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getNombreArticle() {
        return nombreArticle;
    }

    public void setNombreArticle(int nombreArticle) {
        this.nombreArticle = nombreArticle;
    }

    public Date getDate()  {               
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commandePK != null ? commandePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commande)) {
            return false;
        }
        Commande other = (Commande) object;
        if ((this.commandePK == null && other.commandePK != null) || (this.commandePK != null && !this.commandePK.equals(other.commandePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Commande[ commandePK=" + commandePK + " ]";
    }

  public void setNombreArticle(Integer nombreArticle) { 
        this.nombreArticle = nombreArticle;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }
    
}
