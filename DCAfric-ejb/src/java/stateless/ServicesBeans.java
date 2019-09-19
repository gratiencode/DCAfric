/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.ServiceRemote;
import entities.Service;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.swing.JOptionPane;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName="ejb/Stateless/ServicesSB")
@LocalBean
public class ServicesBeans implements ServiceRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Service insertService(Service obj) {
        em.persist(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Service updateService(Service obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteService(Service obj) {
       em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Service> AffichageServiceByName(String name) {
        return em.createNamedQuery("Service.findByNomService").setParameter("nomService", name).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Service> AffichageServiceByUnitMesure(String unit) {
       return em.createNamedQuery("Service.findByUnitMesure").setParameter("unitMesure", unit).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Service> AffichageService() {
        return em.createNamedQuery("Service.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Service getService(String id) {
        
        return read(id);
    }
    
    private Service read(String id){
        try{
        return (Service)em.createNamedQuery("Service.findById").setParameter("id", id).getSingleResult(); 
        }catch(NoResultException e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public Service affichageOneByName(String name) {
        return (Service) em.createNamedQuery("Service.findByNomService").setParameter("nomService", name).getSingleResult();
    }
    
    
}
