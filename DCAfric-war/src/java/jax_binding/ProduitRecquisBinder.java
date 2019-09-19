/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import util.ProduitRecquis;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(ProduitRecquis.class)
public class ProduitRecquisBinder extends ArrayList<ProduitRecquis> {
    public ProduitRecquisBinder() {
        super();
    }

    public ProduitRecquisBinder(Collection<? extends ProduitRecquis> c) {
        super(c);
    }
    
    public void addAllProduitRecquiss(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<ProduitRecquis> getListProduitRecquis(){
        return this;
    }
}
