/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.KiosqueRemote;
import entities.Kiosque;
import java.awt.Desktop;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName="ejb/Stateless/kiosqueSB")
@LocalBean
 public class KiosqueBeans implements KiosqueRemote {
    @PersistenceContext(unitName="Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Kiosque insertKiosque(Kiosque obj) {
       em.persist(obj);
       return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Kiosque updateKiosque(Kiosque obj) {
       em.merge(obj);
       return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteKiosque(Kiosque obj) {
        em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kiosque> AffichageKiosqueByLongitude(String obj) {
       return em.createNamedQuery("Kiosque.findByLongitude").setParameter("logitude", obj).getResultList(); 
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kiosque> AffichageKiosqueByLatitude(String Latitude) {
       return em.createNamedQuery("Kiosque.findByLatitude").setParameter("latitude", Latitude).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kiosque> AffichageKiosque() {
       return em.createNamedQuery("Kiosque.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kiosque> localizeKiosques() {
       List<Kiosque> getThem=em.createNamedQuery("Kiosque.findAll").getResultList(); 
       return getThem;
    }

    @Override
    public Kiosque localizeKiosque(String kiosqueId) {
       return (Kiosque)em.createNamedQuery("Kiosque.findById").setParameter("id", kiosqueId).getSingleResult();
    }

    @Override
    public Kiosque getKiosque(String uid) {
        return (Kiosque)em.createNamedQuery("Kiosque.findById").setParameter("id", uid).getSingleResult();
    }

   

}
