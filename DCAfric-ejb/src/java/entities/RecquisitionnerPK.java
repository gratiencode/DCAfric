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
public class RecquisitionnerPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_kiosque")
    private String idKiosque;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_produit")
    private String idProduit;

    public RecquisitionnerPK() {
    }

    public RecquisitionnerPK(long id, String idKiosque, String idProduit) {
        this.id = id;
        this.idKiosque = idKiosque;
        this.idProduit = idProduit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdKiosque() {
        return idKiosque;
    }

    public void setIdKiosque(String idKiosque) {
        this.idKiosque = idKiosque;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (idKiosque != null ? idKiosque.hashCode() : 0);
        hash += (idProduit != null ? idProduit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecquisitionnerPK)) {
            return false;
        }
        RecquisitionnerPK other = (RecquisitionnerPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idKiosque == null && other.idKiosque != null) || (this.idKiosque != null && !this.idKiosque.equals(other.idKiosque))) {
            return false;
        }
        if ((this.idProduit == null && other.idProduit != null) || (this.idProduit != null && !this.idProduit.equals(other.idProduit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.RecquisitionnerPK[ id=" + id + ", idKiosque=" + idKiosque + ", idProduit=" + idProduit + " ]";
    }
    
}
