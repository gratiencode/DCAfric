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
public class ConsommerPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_service")
    private String idService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_client")
    private String idClient;

    public ConsommerPK() {
    }

    public ConsommerPK(int id, String idService, String idClient) {
        this.id = id;
        this.idService = idService;
        this.idClient = idClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (idService != null ? idService.hashCode() : 0);
        hash += (idClient != null ? idClient.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsommerPK)) {
            return false;
        }
        ConsommerPK other = (ConsommerPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idService == null && other.idService != null) || (this.idService != null && !this.idService.equals(other.idService))) {
            return false;
        }
        if ((this.idClient == null && other.idClient != null) || (this.idClient != null && !this.idClient.equals(other.idClient))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ConsommerPK[ id=" + id + ", idService=" + idService + ", idClient=" + idClient + " ]";
    }
    
}
