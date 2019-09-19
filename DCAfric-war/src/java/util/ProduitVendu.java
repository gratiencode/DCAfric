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
public class ProduitVendu {
    private String idProduit;
    private String nomProduit;
    private String fournisseur;
    private String reference;
    private double quaniteVendu;
    private double montantTotalVendu;
    private double prixUnitaire;
    private double quantiteRestant;
    private String devise;
    private String date;

    public ProduitVendu(String idProduit, String nomProduit, String fournisseur, String reference, double quaniteVendu, double montantTotalVendu, double prixUnitaire, double quantiteRestant, String date) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.fournisseur = fournisseur;
        this.reference = reference;
        this.quaniteVendu = quaniteVendu;
        this.montantTotalVendu = montantTotalVendu;
        this.prixUnitaire = prixUnitaire;
        this.quantiteRestant = quantiteRestant;
        this.date = date;
    }

    

    public ProduitVendu() {
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public double getQuaniteVendu() {
        return quaniteVendu;
    }

    public void setQuaniteVendu(double quaniteVendu) {
        this.quaniteVendu = quaniteVendu;
    }

    public double getMontantTotalVendu() {
        return montantTotalVendu;
    }

    public void setMontantTotalVendu(double montantTotalVendu) {
        this.montantTotalVendu = montantTotalVendu;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getQuantiteRestant() {
        return quantiteRestant;
    }

    public void setQuantiteRestant(double quantiteRestant) {
        this.quantiteRestant = quantiteRestant;
    }

    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idProduit);
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
        final ProduitVendu other = (ProduitVendu) obj;
        if (!Objects.equals(this.idProduit, other.idProduit)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProduitVendu{" + "idProduit=" + idProduit + ", nomProduit=" + nomProduit + ", fournisseur=" + fournisseur + ", quaniteVendu=" + quaniteVendu + ", montantTotalVendu=" + montantTotalVendu + ", prixUnitaire=" + prixUnitaire + ", quantiteRestant=" + quantiteRestant + ", date=" + date + '}';
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }
    
    
    
    
    
    
}
