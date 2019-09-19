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
@Table(name = "materiel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materiel.findAll", query = "SELECT m FROM Materiel m"),
    @NamedQuery(name = "Materiel.findById", query = "SELECT m FROM Materiel m WHERE m.id = :id"),
    @NamedQuery(name = "Materiel.findByNomMateriel", query = "SELECT m FROM Materiel m WHERE m.nomMateriel = :nomMateriel"),
    @NamedQuery(name = "Materiel.findByModel", query = "SELECT m FROM Materiel m WHERE m.model = :model"),
    @NamedQuery(name = "Materiel.findBySearching", query = "SELECT m FROM Materiel m WHERE m.nomMateriel LIKE :model"),
    @NamedQuery(name = "Materiel.findByQuantiteMateriel", query = "SELECT m FROM Materiel m WHERE m.quantiteMateriel = :quantiteMateriel"),
    @NamedQuery(name = "Materiel.findByFabriquant", query = "SELECT m FROM Materiel m WHERE m.fabriquant = :fabriquant")})
public class Materiel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "nom_materiel")
    private String nomMateriel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "model")
    private String model;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_materiel")
    private int quantiteMateriel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "fabriquant")
    private String fabriquant;

    public Materiel() {
    }

    public Materiel(String id) {
        this.id = id;
    }

    public Materiel(String id, String nomMateriel, String model, int quantiteMateriel, String fabriquant) {
        this.id = id;
        this.nomMateriel = nomMateriel;
        this.model = model;
        this.quantiteMateriel = quantiteMateriel;
        this.fabriquant = fabriquant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomMateriel() {
        return nomMateriel;
    }

    public void setNomMateriel(String nomMateriel) {
        this.nomMateriel = nomMateriel;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getQuantiteMateriel() {
        return quantiteMateriel;
    }

    public void setQuantiteMateriel(int quantiteMateriel) {
        this.quantiteMateriel = quantiteMateriel;
    }

    public String getFabriquant() {
        return fabriquant;
    }

    public void setFabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
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
        if (!(object instanceof Materiel)) {
            return false;
        }
        Materiel other = (Materiel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Materiel[ id=" + id + " ]";
    }
    
}
