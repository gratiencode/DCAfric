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
public class ProduitRecquis {
    private String idProduit;
    private String nomProduit;
    private String forunisseur;
    private String devise;
    private double quantiteRecu;
    private double prixUnitaire;
    private String date;

    public ProduitRecquis(String nomProduit, String forunisseur, double quantiteRecu, double prixUnitaire, String date,String idPro,String devise) {
        this.nomProduit = nomProduit;
        this.forunisseur = forunisseur;
        this.devise=devise;
        this.quantiteRecu = quantiteRecu;
        this.prixUnitaire = prixUnitaire;
        this.date = date;
        this.idProduit=idPro;
        
    }

    public ProduitRecquis() {
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getForunisseur() {
        return forunisseur;
    }

    public void setForunisseur(String forunisseur) {
        this.forunisseur = forunisseur;
    }

    public double getQuantiteRecu() {
        return quantiteRecu;
    }

    public void setQuantiteRecu(double quantiteRecu) {
        this.quantiteRecu = quantiteRecu;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.idProduit);
        return hash;
    }

    @Override
    public String toString() {
        return "ProduitRecquis{" + "idProduit=" + idProduit + ", nomProduit=" + nomProduit + ", forunisseur=" + forunisseur + ", quantiteRecu=" + quantiteRecu + ", prixUnitaire=" + prixUnitaire + ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProduitRecquis other = (ProduitRecquis) obj;
        if (!Objects.equals(this.idProduit, other.idProduit)) {
            return false;
        }
        return true;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }
    
    
}
