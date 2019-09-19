/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entities.Materiel;
import interfaces.PossederRemote;
import entities.Posseder;
import entities.PossederPK;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/possederSB")
@LocalBean
public class PossederBeans implements PossederRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Posseder insertPosseder(Posseder obj) {
        em.persist(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Posseder updatePosseder(Posseder obj) {
        em.merge(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteposseder(Posseder obj) {
        em.remove(em.merge(obj));
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posseder> AffichagePossederByKiosque(String kiosque) {
        return em.createNamedQuery("Posseder.findByIdKiosque").setParameter("idKiosque", kiosque).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posseder> AffichagePossederByMateriel(String materiel) {
        return em.createNamedQuery("Posseder.findByIdMateriel").setParameter("idMaterield", materiel).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posseder> AffichagePossederByDate(String date) {
        return em.createNamedQuery("Posseder.findByDate").setParameter("date", date).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posseder> AffichagePosseder() {
        return em.createNamedQuery("Posseder.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Posseder getPossession(int id,String kiosque,String materiel) {
        return em.find(Posseder.class, new PossederPK(id,kiosque,materiel)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override 
    public List<Posseder> searchMateriel(String materiel, Date d1, Date d2) {
      List<Posseder> result=new ArrayList<>();
      List<Materiel> lm=em.createNamedQuery("Materiel.findBySearching").setParameter("model", materiel+"%").getResultList();
      for( Materiel m:lm){
         List<Posseder> lp=em.createNamedQuery("Posseder.findBySearch").setParameter("idMateriel", m.getId()).setParameter("date1", d1,TemporalType.DATE)
                  .setParameter("date2", d2,TemporalType.DATE).getResultList();
         for(Posseder p:lp){
             if(!result.contains(p)){
                 result.add(p);
             }
         }
      }
      return result;
    }

    @Override
    public List<Posseder> searchMaterielOnKiosque(String idKiosque) { 
      return em.createNamedQuery("Posseder.findBySearchingPerIdKiosque").setParameter("idKiosque", idKiosque+"%").getResultList();   
    }

    @Override
    public List<Posseder> searchMateriel(String materiel) {
         List<Posseder> result=new ArrayList<>();
      List<Materiel> lm=em.createNamedQuery("Materiel.findBySearching").setParameter("model", materiel+"%").getResultList();
      for( Materiel m:lm){
         List<Posseder> lp=em.createNamedQuery("Posseder.findBySearch").setParameter("idMateriel", m.getId()).getResultList();
         for(Posseder p:lp){
             if(!result.contains(p)){
                 result.add(p);
             }
         }
      }
      return result;
    }

    @Override
    public List<Posseder> paginate(Date d1, Date d2) {
       return em.createNamedQuery("Posseder.paginate").setParameter("date1", d1,TemporalType.DATE).setParameter("date2", d2,TemporalType.DATE).getResultList();
    }
}
