package com.districom.endeleya.districomservices.tools;

/**
 * Created by SERVER on 27/04/2019.
 */

public class Promotion {
    String phone;
    String code;
    String date;
    String produit;
    double quantite;

    public Promotion() {
    }

    public Promotion(String phone, String code, String date, String produit, double quantite) {
        this.phone = phone;
        this.code = code;
        this.date = date;
        this.produit = produit;
        this.quantite = quantite;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Promotion promotion = (Promotion) o;

        if (Double.compare(promotion.quantite, quantite) != 0) return false;
        if (phone != null ? !phone.equals(promotion.phone) : promotion.phone != null) return false;
        if (code != null ? !code.equals(promotion.code) : promotion.code != null) return false;
        if (date != null ? !date.equals(promotion.date) : promotion.date != null) return false;
        return produit != null ? produit.equals(promotion.produit) : promotion.produit == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = phone != null ? phone.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (produit != null ? produit.hashCode() : 0);
        temp = Double.doubleToLongBits(quantite);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
