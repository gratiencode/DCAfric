/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import util.ServiceVendu;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(ServiceVendu.class)
public class ServiceVenduBinder extends ArrayList<ServiceVendu> {
    public ServiceVenduBinder() {
        super();
    }

    public ServiceVenduBinder(Collection<? extends ServiceVendu> c) {
        super(c);
    }
    
    public void addAllServiceVendus(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<ServiceVendu> getListServiceVendu(){
        return this;
    }
}
