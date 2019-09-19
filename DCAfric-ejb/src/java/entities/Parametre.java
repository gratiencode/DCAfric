/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERVER
 */
@Entity
@Table(name = "parametre")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametre.findAll", query = "SELECT p FROM Parametre p"),
    @NamedQuery(name = "Parametre.findByClef", query = "SELECT p FROM Parametre p WHERE p.clef = :clef"),
    @NamedQuery(name = "Parametre.findByValeur", query = "SELECT p FROM Parametre p WHERE p.valeur = :valeur")})
public class Parametre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "clef")
    private String clef;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "valeur")
    private String valeur;

    public Parametre() {
    }

    public Parametre(String clef) {
        this.clef = clef;
    }

    public Parametre(String clef, String valeur) {
        this.clef = clef;
        this.valeur = valeur;
    }

    public String getClef() {
        return clef;
    }

    public void setClef(String clef) {
        this.clef = clef;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clef != null ? clef.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametre)) {
            return false;
        }
        Parametre other = (Parametre) object;
        if ((this.clef == null && other.clef != null) || (this.clef != null && !this.clef.equals(other.clef))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Parametre[ clef=" + clef + " ]";
    }
    
}
