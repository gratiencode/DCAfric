/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Vente;
import entities.Vente;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Vente.class)
public class VenteBinder extends ArrayList<Vente> {
    public VenteBinder() {
        super();
    }

    public VenteBinder(Collection<? extends Vente> c) {
        super(c);
    }
    
    public void addAllVente(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Vente> getListVente(){
        return this;
    }
}
