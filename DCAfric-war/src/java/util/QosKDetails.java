/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PC
 */
@XmlRootElement
public class QosKDetails {
    
    private String dateObtenir;
    private String idKiosque;
    private String nomAgent;
    private String info;
    private double latitude;
    private double longitude;

    public QosKDetails(String idKiosque, String nomAgent, String dateObtenir, String info, double latitude, double longitude) {
        this.idKiosque = idKiosque;
        this.nomAgent = nomAgent;
        this.dateObtenir = dateObtenir;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public QosKDetails() {
    }

    public String getIdKiosque() {
        return idKiosque;
    }

    public void setIdKiosque(String idKiosque) {
        this.idKiosque = idKiosque;
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public String getDateObtenir() {
        return dateObtenir;
    }

    public void setDateObtenir(String dateObtenir) {
        this.dateObtenir = dateObtenir;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.idKiosque);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QosKDetails other = (QosKDetails) obj;
        if (!Objects.equals(this.idKiosque, other.idKiosque)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QosKDetails{" + "dateObtenir=" + dateObtenir + ", idKiosque=" + idKiosque + ", nomAgent=" + nomAgent + ", info=" + info + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
    
     
    
}
