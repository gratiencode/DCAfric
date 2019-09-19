/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Objects;

/**
 *
 * @author PC
 */
public class ServiceVendu {
    private String idService;
    private String nomService;
    private String clientName;
    private String unitMesure;
    private String devise;
    private String oInfo;
    private double quantVendu;
    private double montatTotal;
    private String date;
    private String etat;

    public ServiceVendu(String idService, String nomService, String clientName, String unitMesure,String devise, String oInfo, double quantVendu, double montatTotal, String date, String etat) {
        this.idService = idService;
        this.nomService = nomService;
        this.clientName = clientName;
        this.unitMesure = unitMesure;
        this.devise=devise;
        this.oInfo = oInfo;
        this.quantVendu = quantVendu;
        this.montatTotal = montatTotal;
        this.date = date;
        this.etat = etat;
    }

    public ServiceVendu() {
    }

    
    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUnitMesure() {
        return unitMesure;
    }

    public void setUnitMesure(String unitMesure) {
        this.unitMesure = unitMesure;
    }

    public String getoInfo() {
        return oInfo;
    }

    public void setoInfo(String oInfo) {
        this.oInfo = oInfo;
    }

    public double getQuantVendu() {
        return quantVendu;
    }

    public void setQuantVendu(double quantVendu) {
        this.quantVendu = quantVendu;
    }

    public double getMontatTotal() {
        return montatTotal;
    }

    public void setMontatTotal(double montatTotal) {
        this.montatTotal = montatTotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.idService);
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
        final ServiceVendu other = (ServiceVendu) obj;
        if (!Objects.equals(this.idService, other.idService)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ServiceVendu{" + "idService=" + idService + ", nomService=" + nomService + ", clientName=" + clientName + ", unitMesure=" + unitMesure + ", oInfo=" + oInfo + ", quantVendu=" + quantVendu + ", montatTotal=" + montatTotal + ", date=" + date + ", etat=" + etat + '}';
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }
    
    
    
}
