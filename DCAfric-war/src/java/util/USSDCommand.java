/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entities.Commande;
import entities.Vente;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gratien
 */
@XmlRootElement
public class USSDCommand {
    private String ussdCode;
    private String operator;
    private String produit;
    private String result;
    private String telephone;
    private String ebill;
    private Commande commande;
    private List<Vente> ventes;

    public USSDCommand(String ussdCode, String operator, String produit, String result, String telephone, String ebill) {
        this.ussdCode = ussdCode;
        this.operator = operator;
        this.produit = produit;
        this.result = result;
        this.telephone = telephone;
        this.ebill = ebill;
    }
    

    

    public USSDCommand() {
    }

    public String getUssdCode() {
        return ussdCode;
    }

    public void setUssdCode(String ussdCode) {
        this.ussdCode = ussdCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }
    
    
    

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

   public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getEbill() {
        return ebill;
    }

    public void setEbill(String ebill) {
        this.ebill = ebill;
    } 
    
    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public List<Vente> getVentes() {
        return ventes;
    }

    public void setVentes(List<Vente> ventes) {
        this.ventes = ventes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.ussdCode);
        hash = 31 * hash + Objects.hashCode(this.operator);
        hash = 31 * hash + Objects.hashCode(this.produit);
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
        final USSDCommand other = (USSDCommand) obj;
        if (!Objects.equals(this.ussdCode, other.ussdCode)) {
            return false;
        }
        if (!Objects.equals(this.operator, other.operator)) {
            return false;
        }
        if (!Objects.equals(this.produit, other.produit)) {
            return false;
        }
        return true;
    }

 

    
    
    
}
