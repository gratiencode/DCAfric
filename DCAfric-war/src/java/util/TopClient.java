/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Objects;

/**
 *
 * @author gratien
 */
public class TopClient {
    private String id;
    private String nom;
    private String prenom;
    private String kiosque;
    private double quantite;
    private int frequence;

    public TopClient(String id, String nom, String prenom, String kiosque, double quantite, int frequence) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.kiosque = kiosque;
        this.quantite = quantite;
        this.frequence = frequence;
    }

    public TopClient() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getKiosque() {
        return kiosque;
    }

    public void setKiosque(String kiosque) {
        this.kiosque = kiosque;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TopClient other = (TopClient) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.kiosque, other.kiosque)) {
            return false;
        }
        return true;
    }

    
    
    
}
