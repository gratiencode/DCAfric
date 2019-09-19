/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Commande;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Commande.class)
public class CommandBinder extends ArrayList<Commande>{
    
     public CommandBinder() {
        super(); 
    }

    public CommandBinder(Collection<? extends Commande> c) {
        super(c);
    }
    
    public void addAllCommande(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Commande> getListCommande(){
        return this;
    }
}
