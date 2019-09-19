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
import util.USSDCommand;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(USSDCommand.class)
public class USSDBinder extends ArrayList<USSDCommand>{
    
     public USSDBinder() {
        super(); 
    }

    public USSDBinder(Collection<? extends USSDCommand> c) {
        super(c);
    }
    
    public void addAllCommande(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<USSDCommand> getListCommande(){
        return this;
    }
}
