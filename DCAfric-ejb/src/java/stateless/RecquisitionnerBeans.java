/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import com.sun.istack.logging.Logger;
import entities.Produit;
import interfaces.RecquisitionnerRemote;
import entities.Recquisitionner;
import entities.RecquisitionnerPK;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/RecquisitionnerSB")
@LocalBean
public class RecquisitionnerBeans implements RecquisitionnerRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Recquisitionner insertRecquisitionner(Recquisitionner obj) {
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Recquisitionner updateRecquisitionner(Recquisitionner obj) {

        em.merge(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteRecquisitionner(Recquisitionner obj) {
        em.remove(em.merge(obj));
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionnerByKiosque(String kiosque) {
        return em.createNamedQuery("Recquisitionner.findByIdKiosque").setParameter("idKiosque", kiosque).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionnerByProduit(String Produit) {
        return em.createNamedQuery("Recquisitionner.findByIdProduit").setParameter("idProduit", Produit).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionnerByDate(String date) {
        return em.createNamedQuery("Recquisitionner.findByDate").setParameter("date", date).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionnerByQuantite(String quantite) {
        return em.createNamedQuery("Recquisitionner.findByQuant").setParameter("quant", quantite).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionnerByPrix(String prix) {
        return em.createNamedQuery("Recquisitionner.findByPrixUnit").setParameter("prixUnit", prix).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recquisitionner> affichageRecquisitionner() {
        try {

            return em.createNamedQuery("Recquisitionner.findAll").getResultList();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();
            for (ConstraintViolation dt : cvs) {
                String s = dt.getMessage();
                Logger.getLogger(this.getClass()).info("Valeur detail faute recqisition >>>>>>>>> " + s);
            }
            return null;
        }//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Recquisitionner getRecquisition(int uid, String kiosq, String produit) {
        RecquisitionnerPK rpk = new RecquisitionnerPK(uid, kiosq, produit);
        return em.find(Recquisitionner.class, rpk);
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionneeByProduit_ByKiosque(String Produit, String Kiosque, String date1, String date2) {
        return em.createNamedQuery("Recquisitionner.findByIdProduitAndIdKiosque").setParameter("idProduit", Produit).setParameter("idKiosque", Kiosque)
                .setParameter("date1", date1).setParameter("date2", date2).getResultList();
    }

    @Override
    public Double AffichageQuantRecquisitionneeByProduit_ByKiosque(String produit, String kiosque, String date1, String date2) {
        TypedQuery tq = em.createNamedQuery("Recquisitionner.findQuantiteByIdProduitByIdKiosque", Double.class);
        return (Double) tq.setParameter("idProduit", produit).setParameter("idKiosque", kiosque).setParameter("date1", date1).setParameter("date2", date2).getSingleResult();
    }

    @Override
    public Recquisitionner getLastRecquisitionProduit(String produitName) {
        Produit p = (Produit) em.createNamedQuery("Produit.findByNomProduit").setParameter("nomProduit", produitName).getSingleResult();
        Query q = em.createNamedQuery("Recquisitionner.findDernierRecquisition").setParameter("idProduit", p.getId());
        return (Recquisitionner) q.getResultList().get(q.getFirstResult());
    }

    @Override
    public List<Recquisitionner> AffichageRecquisitionnerByProd(String prod) {
        Produit p = (Produit) em.createNamedQuery("Produit.findByNomProd").setParameter("nomProduit", prod).getSingleResult();
        Query q = em.createNamedQuery("Recquisitionner.findDernierRecquisition").setParameter("idProduit", p.getId());
        return q.getResultList();
    }

    @Override
    public List<Recquisitionner> searchRecquisition(String produit, Date date1, Date date2) {
        List<Produit> lps = em.createNamedQuery("Produit.findBySearching").setParameter("nomProduit", produit + "%").getResultList();
        List<Recquisitionner> result = new ArrayList<>();
        for (Produit p : lps) {
            List<Recquisitionner> reqs = em.createNamedQuery("Recquisitionner.findAllRecqForProduct")
                    .setParameter("idProduit", p.getId())
                    .setParameter("date1", date1, TemporalType.TIMESTAMP)
                    .setParameter("date2", date2, TemporalType.TIMESTAMP).getResultList();
            for (Recquisitionner r : reqs) {
                if (!result.contains(r)) {
                    result.add(r);
                }
            }

        }
        return result;
    }

    @Override
    public List<Recquisitionner> searchRecquisition(Date d1, Date d2) {
        return em.createNamedQuery("Recquisitionner.findOnPeriod")
                .setParameter("date1", d1, TemporalType.TIMESTAMP)
                .setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
        
    }

    @Override
    public List<Recquisitionner> searchByKiosqueOnPeriod(String kiosque, Date d1, Date d2) {
        List<Recquisitionner> reqs = em.createNamedQuery("Recquisitionner.findByIdKiosqueOnPeriod")
                .setParameter("idKiosque", kiosque + "%") 
                .setParameter("date1", d1, TemporalType.TIMESTAMP)
                .setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList(); 
        return reqs;
    }

    @Override
    public List<Recquisitionner> paginate(Date d1, Date d2) {
        return em.createNamedQuery("Recquisitionner.findOnPeriod")
                .setParameter("date1", d1, TemporalType.TIMESTAMP)
                .setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList(); 
    }
}
