/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import util.*;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PC
 */
@XmlRootElement
public class Recette implements Serializable {
    private String idKiosque;
    private String reference;
    private double montant;
    private String devise;
    private Date date;

    public Recette(String idKiosque, String reference, double montant, String devise, Date date) {
        this.idKiosque = idKiosque;
        this.reference = reference;
        this.montant = montant;
        this.devise = devise;
        this.date = date;
    }

    public Recette() {
    }

    public String getIdKiosque() {
        return idKiosque;
    }

    public void setIdKiosque(String idKiosque) {
        this.idKiosque = idKiosque;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    
    
}
