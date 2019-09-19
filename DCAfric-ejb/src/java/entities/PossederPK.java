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
public class PossederPK implements Serializable {
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
    @Column(name = "id_materiel")
    private String idMateriel;

    public PossederPK() {
    }

    public PossederPK(long id, String idKiosque, String idMateriel) {
        this.id = id;
        this.idKiosque = idKiosque;
        this.idMateriel = idMateriel;
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

    public String getIdMateriel() {
        return idMateriel;
    }

    public void setIdMateriel(String idMateriel) {
        this.idMateriel = idMateriel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (idKiosque != null ? idKiosque.hashCode() : 0);
        hash += (idMateriel != null ? idMateriel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PossederPK)) {
            return false;
        }
        PossederPK other = (PossederPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idKiosque == null && other.idKiosque != null) || (this.idKiosque != null && !this.idKiosque.equals(other.idKiosque))) {
            return false;
        }
        if ((this.idMateriel == null && other.idMateriel != null) || (this.idMateriel != null && !this.idMateriel.equals(other.idMateriel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PossederPK[ id=" + id + ", idKiosque=" + idKiosque + ", idMateriel=" + idMateriel + " ]";
    }
    
}
