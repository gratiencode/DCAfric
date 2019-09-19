/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import util.ProduitVendu;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(ProduitVendu.class)
public class ProduitVenduBinder extends ArrayList<ProduitVendu> {
    public ProduitVenduBinder() {
        super();
    }

    public ProduitVenduBinder(Collection<? extends ProduitVendu> c) {
        super(c);
    }
    
    public void addAllProduitVendus(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<ProduitVendu> getListProduitVendu(){
        return this;
    }
}
