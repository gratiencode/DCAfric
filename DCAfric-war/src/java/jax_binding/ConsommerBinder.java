/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Consommer;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Consommer.class)
public class ConsommerBinder extends ArrayList<Consommer> {
    public ConsommerBinder() {
        super();
    }

    public ConsommerBinder(Collection<? extends Consommer> c) {
        super(c);
    }
    
    public void addAllConsommers(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Consommer> getListConsommer(){
        return this;
    }
}
