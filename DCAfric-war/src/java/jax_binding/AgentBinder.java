/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import entities.Agents;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(Agents.class)
public class AgentBinder extends ArrayList<Agents> {

    public AgentBinder() {
        super();
    }

    public AgentBinder(Collection<? extends Agents> c) {
        super(c);
    }
    
    public void addAllAgents(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Agents> getListAgent(){
        return this;
    }
    
    
}
