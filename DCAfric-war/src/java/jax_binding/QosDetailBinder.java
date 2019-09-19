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
import util.QosKDetails;

/**
 *
 * @author PC
 */
@XmlRootElement
@XmlSeeAlso(QosKDetails.class)
public class QosDetailBinder extends ArrayList<QosKDetails>{

    public QosDetailBinder(Collection<? extends QosKDetails> c) {
        super(c);
    }

    public QosDetailBinder() {
        super();
    }
    
    public ArrayList<QosKDetails> getAllQosDetails(){
        return this;
    }
    
    public void setAllQosDetails(ArrayList<QosKDetails> details){
        addAll(details);
    }
    
    
}
