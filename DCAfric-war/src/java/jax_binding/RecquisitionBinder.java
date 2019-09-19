/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Recquisitionner;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author gratien
 */
@XmlRootElement
@XmlSeeAlso(Recquisitionner.class)
public class RecquisitionBinder extends ArrayList<Recquisitionner> {
     public RecquisitionBinder() {
        super();
    }

    public RecquisitionBinder(Collection<? extends Recquisitionner> c) {
        super(c);
    }
    
    public void addAllRecquisitionner(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Recquisitionner> getListRecquisitionner(){
        return this;
    }
}
