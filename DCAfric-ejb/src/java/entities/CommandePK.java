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
public class CommandePK implements Serializable {
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
    @Column(name = "id_client")
    private String idClient;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "reference")
    private String reference;

    public CommandePK() {
    }

    public CommandePK(long id, String idKiosque, String idClient, String reference) {
        this.id = id;
        this.idKiosque = idKiosque;
        this.idClient = idClient;
        this.reference = reference;
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

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
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
        hash += (idKiosque != null ? idKiosque.hashCode() : 0);
        hash += (idClient != null ? idClient.hashCode() : 0);
        hash += (reference != null ? reference.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandePK)) {
            return false;
        }
        CommandePK other = (CommandePK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idKiosque == null && other.idKiosque != null) || (this.idKiosque != null && !this.idKiosque.equals(other.idKiosque))) {
            return false;
        }
        if ((this.idClient == null && other.idClient != null) || (this.idClient != null && !this.idClient.equals(other.idClient))) {
            return false;
        }
        if ((this.reference == null && other.reference != null) || (this.reference != null && !this.reference.equals(other.reference))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CommandePK[ id=" + id + ", idKiosque=" + idKiosque + ", idClient=" + idClient + ", reference=" + reference + " ]";
    }
    
}
