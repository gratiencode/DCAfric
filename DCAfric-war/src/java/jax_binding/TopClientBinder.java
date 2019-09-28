/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;


import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import util.TopClient;

/**
 *
 * @author gratien
 */
@XmlRootElement
@XmlSeeAlso(TopClient.class)
public class TopClientBinder extends ArrayList<TopClient>{
     public TopClientBinder() {
        super();
    }

    public TopClientBinder(Collection<? extends TopClient> c) {
        super(c);
    }
    
    public void addAllClient(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<TopClient> getListClient(){
        return this;
    }
}
