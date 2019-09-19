/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_binding;

import com.districom.endeleya.districomservices.tools.Promotion;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author SERVER
 */
@XmlRootElement
@XmlSeeAlso(Promotion.class)
public class PromotionBinder extends ArrayList<Promotion> {

    public PromotionBinder() {
        super();
    }

    public PromotionBinder(Collection<? extends Promotion> c) {
        super(c);
    }
    
    public void addAllParams(ArrayList al){
        this.addAll(al); 
    }
    
    public ArrayList<Promotion> getListParams(){
        return this;
    }
    
}

