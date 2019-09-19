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
@Table(name = "service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Service.findAll", query = "SELECT s FROM Service s"),
    @NamedQuery(name = "Service.findById", query = "SELECT s FROM Service s WHERE s.id = :id"),
    @NamedQuery(name = "Service.findByNomService", query = "SELECT s FROM Service s WHERE s.nomService = :nomService"),
    @NamedQuery(name = "Service.findByUnitMesure", query = "SELECT s FROM Service s WHERE s.unitMesure = :unitMesure"),
    @NamedQuery(name = "Service.findBySearching", query = "SELECT s FROM Service s WHERE s.nomService LIKE :nomService"),
    @NamedQuery(name = "Service.findByInfo", query = "SELECT s FROM Service s WHERE s.info = :info")})
public class Service implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "tarif_cash")
    private double tarifCash;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "devise_cash")
    private String deviseCash;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tarif_mobmoney")
    private double tarifMobmoney;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "devise_mobmoney")
    private String deviseMobmoney;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "nom_service")
    private String nomService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "unit_mesure")
    private String unitMesure;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "info")
    private String info;

    public Service() {
    }

    public Service(String id) {
        this.id = id;
    }

    public Service(String id, String nomService, String unitMesure, String info) {
        this.id = id;
        this.nomService = nomService;
        this.unitMesure = unitMesure;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getUnitMesure() {
        return unitMesure;
    }

    public void setUnitMesure(String unitMesure) {
        this.unitMesure = unitMesure;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Service[ id=" + id + " ]";
    }

    public double getTarifCash() {
        return tarifCash;
    }

    public void setTarifCash(double tarifCash) {
        this.tarifCash = tarifCash;
    }

    public String getDeviseCash() {
        return deviseCash;
    }

    public void setDeviseCash(String deviseCash) {
        this.deviseCash = deviseCash;
    }

    public double getTarifMobmoney() {
        return tarifMobmoney;
    }

    public void setTarifMobmoney(double tarifMobmoney) {
        this.tarifMobmoney = tarifMobmoney;
    }

    public String getDeviseMobmoney() {
        return deviseMobmoney;
    }

    public void setDeviseMobmoney(String deviseMobmoney) {
        this.deviseMobmoney = deviseMobmoney;
    }
    
}
