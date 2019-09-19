/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "kiosque")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kiosque.findAll", query = "SELECT k FROM Kiosque k"),
    @NamedQuery(name = "Kiosque.findById", query = "SELECT k FROM Kiosque k WHERE k.id = :id"),
    @NamedQuery(name = "Kiosque.findByLatitude", query = "SELECT k FROM Kiosque k WHERE k.latitude = :latitude"),
    @NamedQuery(name = "Kiosque.findByLongitude", query = "SELECT k FROM Kiosque k WHERE k.longitude = :longitude"),
    @NamedQuery(name = "Kiosque.findByInfo", query = "SELECT k FROM Kiosque k WHERE k.info = :info")})
public class Kiosque implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private double latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private double longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4096)
    @Column(name = "info")
    private String info;

    public Kiosque() {
    }

    public Kiosque(String id) {
        this.id = id;
    }

    public Kiosque(String id, double latitude, double longitude, String info) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
        if (!(object instanceof Kiosque)) {
            return false;
        }
        Kiosque other = (Kiosque) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Kiosque[ id=" + id + " ]";
    }

    
}
