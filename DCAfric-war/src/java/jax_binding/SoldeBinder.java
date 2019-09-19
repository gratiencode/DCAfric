/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.CurentSolde;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author gratien
 */
@XmlRootElement
@XmlSeeAlso(CurentSolde.class)
public class SoldeBinder extends ArrayList<CurentSolde>{
   public SoldeBinder() {
        super();
    }

    public SoldeBinder(Collection<? extends CurentSolde> c) {
        super(c);
    }
    
    public void addAllSoldes(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<CurentSolde> getListSolde(){
        return this;
    } 
}
