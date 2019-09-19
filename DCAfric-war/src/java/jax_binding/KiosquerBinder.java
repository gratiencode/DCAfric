/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Kiosque;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Kiosque.class)
public class KiosquerBinder extends ArrayList<Kiosque> {
    public KiosquerBinder() {
        super();
    }

    public KiosquerBinder(Collection<? extends Kiosque> c) {
        super(c);
    }
    
    public void addAllKiosques(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Kiosque> getListKiosque(){
        return this;
    }
}
