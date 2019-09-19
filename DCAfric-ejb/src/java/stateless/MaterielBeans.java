/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.MaterielRemote;
import entities.Materiel;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/MaterielSB")
@LocalBean
public class MaterielBeans implements MaterielRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method") 

    @Override
    public Materiel insertMateriel(Materiel obj) {
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Materiel updateMateriel(Materiel obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteMateriel(Materiel obj) {
        em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Materiel> AffichageMaterielByName(String name) {
        return em.createNamedQuery("Materiel.findByNomMateriel").setParameter("nomMateriel", name).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Materiel> AffichageMaterielByModel(String model) {
        return em.createNamedQuery("Materiel.findByModel").setParameter("model", model).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Materiel> AffichageMaterielByFabricant(String fabricant) {
        return em.createNamedQuery("Materiel.findByFabriquant").setParameter("fabriquant", fabricant).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Materiel> AffichageMateriel() {
        return em.createNamedQuery("Materiel.findAll").getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Materiel getMateriel(String id) {
        return (Materiel) em.createNamedQuery("Materiel.findById").setParameter("id", id).getSingleResult(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Materiel affichageOneMaterielByName(String name) {
       return (Materiel)em.createNamedQuery("Materiel.findByNomMateriel").setParameter("nomMateriel", name).getSingleResult();
    }

    @Override
    public List<Materiel> searchMateriel(String name) {
        return em.createNamedQuery("Materiel.findBySearching").setParameter("model", name+"%").getResultList();
    }

   
    
    
}
