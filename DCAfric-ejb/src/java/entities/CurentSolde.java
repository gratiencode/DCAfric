/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gratien
 */
@Entity
@Table(name = "curent_solde")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CurentSolde.findAll", query = "SELECT c FROM CurentSolde c")
    , @NamedQuery(name = "CurentSolde.findById", query = "SELECT c FROM CurentSolde c WHERE c.curentSoldePK.id = :id")
    , @NamedQuery(name = "CurentSolde.findByAgentId", query = "SELECT c FROM CurentSolde c WHERE c.curentSoldePK.agentId = :agentId")
    , @NamedQuery(name = "CurentSolde.findByProductId", query = "SELECT c FROM CurentSolde c WHERE c.curentSoldePK.productId = :productId")
    , @NamedQuery(name = "CurentSolde.findBySolde", query = "SELECT c FROM CurentSolde c WHERE c.solde = :solde")})
public class CurentSolde implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CurentSoldePK curentSoldePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "solde")
    private double solde;

    public CurentSolde() {
    }

    public CurentSolde(CurentSoldePK curentSoldePK) {
        this.curentSoldePK = curentSoldePK;
    }

    public CurentSolde(CurentSoldePK curentSoldePK, double solde) {
        this.curentSoldePK = curentSoldePK;
        this.solde = solde;
    }

    public CurentSolde(int id, String agentId, String productId) {
        this.curentSoldePK = new CurentSoldePK(id, agentId, productId);
    }

    public CurentSoldePK getCurentSoldePK() {
        return curentSoldePK;
    }

    public void setCurentSoldePK(CurentSoldePK curentSoldePK) {
        this.curentSoldePK = curentSoldePK;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (curentSoldePK != null ? curentSoldePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CurentSolde)) {
            return false;
        }
        CurentSolde other = (CurentSolde) object;
        if ((this.curentSoldePK == null && other.curentSoldePK != null) || (this.curentSoldePK != null && !this.curentSoldePK.equals(other.curentSoldePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CurentSolde[ curentSoldePK=" + curentSoldePK + " ]";
    }
    
}
