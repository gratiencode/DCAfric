/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Client;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Client.class)
public class ClientBinder extends ArrayList<Client>{
    public ClientBinder() {
        super();
    }

    public ClientBinder(Collection<? extends Client> c) {
        super(c);
    }
    
    public void addAllClient(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Client> getListClient(){
        return this;
    }
}
