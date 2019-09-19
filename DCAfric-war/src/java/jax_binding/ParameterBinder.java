/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Parametre;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Parametre.class)
public class ParameterBinder extends ArrayList<Parametre> {

    public ParameterBinder() {
        super();
    }

    public ParameterBinder(Collection<? extends Parametre> c) {
        super(c);
    }
    
    public void addAllParams(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Parametre> getListParams(){
        return this;
    }
    
}
