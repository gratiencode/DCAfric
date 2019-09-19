/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Produit;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Produit.class)
public class ProduitBinder extends ArrayList<Produit> {
    public ProduitBinder() {
        super();
    }

    public ProduitBinder(Collection<? extends Produit> c) {
        super(c);
    }
    
    public void addAllProduits(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Produit> getListProduit(){
        return this;
    }
}
