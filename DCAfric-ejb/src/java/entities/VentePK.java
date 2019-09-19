/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author SERVER
 */
@Embeddable
public class VentePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_produit")
    private String idProduit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "reference")
    private String reference;

    public VentePK() {
    }

    public VentePK(int id, String idProduit, String reference) {
        this.id = id;
        this.idProduit = idProduit;
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (idProduit != null ? idProduit.hashCode() : 0);
        hash += (reference != null ? reference.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VentePK)) {
            return false;
        }
        VentePK other = (VentePK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idProduit == null && other.idProduit != null) || (this.idProduit != null && !this.idProduit.equals(other.idProduit))) {
            return false;
        }
        if ((this.reference == null && other.reference != null) || (this.reference != null && !this.reference.equals(other.reference))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.VentePK[ id=" + id + ", idProduit=" + idProduit + ", reference=" + reference + " ]";
    }
    
}
