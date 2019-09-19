/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.ParametreRemote;
import entities.Parametre;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName="ejb/stateless/parametreSB")
@LocalBean
public class ParametreBeans implements ParametreRemote {
   @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Parametre insertParamtre(Parametre obj) {
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Parametre updateParametre(Parametre obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Parametre obj) {
        em.remove(em.merge(obj));
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Parametre> AffichageParametre() {
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT p.clef, p.valeur ");
        sb.append("FROM Parametre p ");
        Query q=em.createNativeQuery(sb.toString(),Parametre.class);
        return q.getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Parametre getParametre(String key) {
        return (Parametre)em.createNamedQuery("Parametre.findByClef").setParameter("clef", key).getSingleResult();
    }
}
