/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Service;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Service.class)
public class ServiceBinder extends ArrayList<Service> {
    public ServiceBinder() {
        super();
    }

    public ServiceBinder(Collection<? extends Service> c) {
        super(c);
    }
    
    public void addAllService(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Service> getListService(){
        return this;
    }
}
