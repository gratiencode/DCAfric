/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Parametre;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class ParametreService {
@PersistenceContext(unitName = "dds_web")
    EntityManager em;
public Parametre getParametre(String key){
    return (Parametre)em.createNamedQuery("Parametre.findByClef").setParameter("clef", key).getSingleResult();
}

public List<Parametre> getParametreByValue(String value){
    return em.createNamedQuery("Parametre.findByValeur").setParameter("valeur", value).getResultList();
}

}
