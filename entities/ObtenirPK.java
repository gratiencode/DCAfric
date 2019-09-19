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
 * @author PC
 */
@Embeddable
public class ObtenirPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_agent")
    private String idAgent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_kiosq")
    private String idKiosq;

    public ObtenirPK() {
    }

    public ObtenirPK(int id, String idAgent, String idKiosq) {
        this.id = id;
        this.idAgent = idAgent;
        this.idKiosq = idKiosq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent;
    }

    public String getIdKiosq() {
        return idKiosq;
    }

    public void setIdKiosq(String idKiosq) {
        this.idKiosq = idKiosq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (idAgent != null ? idAgent.hashCode() : 0);
        hash += (idKiosq != null ? idKiosq.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObtenirPK)) {
            return false;
        }
        ObtenirPK other = (ObtenirPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idAgent == null && other.idAgent != null) || (this.idAgent != null && !this.idAgent.equals(other.idAgent))) {
            return false;
        }
        if ((this.idKiosq == null && other.idKiosq != null) || (this.idKiosq != null && !this.idKiosq.equals(other.idKiosq))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ObtenirPK[ id=" + id + ", idAgent=" + idAgent + ", idKiosq=" + idKiosq + " ]";
    }
    
}
