/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.VenteRemote;
import entities.Vente;
import entities.VentePK;
import entities.VentePK_;
import entities.Vente_;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/VenteSB")
@LocalBean
public class VenteBeans implements VenteRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose 
    // "Insert Code > Add Business Method")

    @Override
    public Vente insertVente(Vente obj) {
        em.persist(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vente updateVente(Vente obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVente(Vente obj) {
        em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageVenteByProduit(String produit) {
        return em.createNamedQuery("Vente.findByIdProduit").setParameter("idProduit", produit).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageVenteByKiosque(String kiosque) {
        return em.createNamedQuery("Vente.findByIdKiosq").setParameter("idKiosq", kiosque).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageVenteByClient(String client) {
        return em.createNamedQuery("Vente.findByIdClient").setParameter("idClient", client).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageVenteByMontant(double montant) {
        return em.createNamedQuery("Vente.findByMantant").setParameter("mantant", montant).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageVenteByDate(String date) {
        return em.createNamedQuery("Vente.findByDate").setParameter("date", date).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageMontantVenteByDate_ByKiosque(String date, String kiosque) {
        return em.createNamedQuery("Vente.findMontantVenteByDate_ByKiosque").setParameter("date", date).setParameter("idKiosq", kiosque).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageVente() {
        return em.createNamedQuery("Vente.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vente> AffichageParRefernce(String reference) {
        return em.createNamedQuery("Vente.findByReference").setParameter("reference", reference).getResultList();
    }

    @Override
    public Vente getVente(int id, String produit, String reference) {
        return em.find(Vente.class, new VentePK(id, produit, reference));
    }

    @Override
    public double getSommeParDevise(String devise, Date d1, Date d2) {
        return getRecette(devise, d1, d2);

    }

    private double getRecette(String devise, Date d1, Date d2) {
        try {
            TypedQuery tqr = em.createNamedQuery("Vente.findRecetteTotalByDevise", Double.class);
            tqr.setParameter("devise", devise);
            tqr.setParameter("date1", d1);
            tqr.setParameter("date2", d2);
            // Logger.getLogger(this.getClass().getName()).info("Data incoming dev "+devise+" > "+d1.toString()+" et "+d2.toString());
            return (double) tqr.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public double getSommeParDevise(String devise, String product, Date d1, Date d2) {
        return getRecetteProduitDevise(devise, product, d1, d2);
    }

    private double getRecetteProduitDevise(String devise, String produit, Date d1, Date d2) {
        try {
            TypedQuery tq = em.createNamedQuery("Vente.findSumByDeviseAndProduct", Double.class);
            return (double) tq.setParameter("devise", devise).setParameter("idProduit", produit).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<Vente> getVenteParProduit(String product, Date d1, Date d2) {
        List<Vente> lv = em.createNamedQuery("Vente.findPerProduct").setParameter("idProduit", product).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
        return lv;
    }

    @Override
    public List<Vente> paginate(Date d1, Date d2) {
        return em.createNamedQuery("Vente.paginate").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public double getSommeParReference(String ref, Date d1, Date d2) {
        TypedQuery tquery = em.createNamedQuery("Vente.findRecetteTotalByReference", Double.class);
        return (double) tquery.setParameter("ref", ref).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
    }

    @Override
    public List<Vente> getRecetteParProduit(String product, Date d1, Date d2) {
        return em.createNamedQuery("Vente.findPerProductForRecette").setParameter("idProduit", product + "%").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public double getSommeParProduit(String product, Date d1, Date d2) {
        try {
            TypedQuery query = em.createNamedQuery("Vente.findSumProduct", Double.class);
            return (double) query.setParameter("idProduit", product).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double getSommeParDeviseParKiosque(String devise, String kiosque, Date d1, Date d2) {
        try {
            TypedQuery query = em.createNamedQuery("Vente.sumRecetteForKiosque", Double.class);

            query.setParameter("devise", devise);
            query.setParameter("ref", kiosque + "%");
            query.setParameter("date11", d1, TemporalType.TIMESTAMP);
            query.setParameter("date22", d2, TemporalType.TIMESTAMP);
            if (query == null) {
                return 0;
            }
            return (double) query.getSingleResult();

        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<Vente> getVenteForKiosqueGroupByProductOnPeriod(String kiosque, Date date1, Date date2) {

        List<Vente> ventes=new ArrayList<>();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq=cb.createQuery(Tuple.class);
        Root<Vente> r=cq.from(Vente.class);
        Path<Date> dateP=r.get(Vente_.date);
        Path<String> deviseP=r.get(Vente_.devise);
        Path<String> refP=r.get(Vente_.ventePK).get(VentePK_.reference);
        Path<String> prodP=r.get(Vente_.ventePK).get(VentePK_.idProduit);
        Path<Integer> idP=r.get(Vente_.ventePK).get(VentePK_.id);
        cq.multiselect(dateP,deviseP,cb.sum(r.get("mantant")),cb.sum(r.get("quantite")),refP,prodP,idP);
        cq.where(cb.and(cb.like(r.get(Vente_.ventePK).get(VentePK_.reference), kiosque+"%"),cb.between(dateP, date1, date2)));
        cq.groupBy(r.get(Vente_.ventePK).get(VentePK_.idProduit));

        List<Tuple> vq=em.createQuery(cq).getResultList();
        for(Tuple t:vq){
            Vente v=new Vente();
            VentePK vpk=new VentePK();
            v.setDate(t.get(dateP));
            v.setDevise(t.get(deviseP));
            v.setMantant((Double)t.get(2));
            v.setQuantite((Double)t.get(3));
            vpk.setReference(t.get(refP));
            vpk.setIdProduit(t.get(prodP));
            vpk.setId(t.get(idP));
            v.setVentePK(vpk);
            ventes.add(v);
        }
        return ventes;
    }

    @Override 
    public Double getSumVenteForKiosqueOnPeriod(String kiosque,String devise, Date date1, Date date2) {

       CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq=cb.createQuery(Tuple.class);
        Root<Vente> r=cq.from(Vente.class);
        Path<Date> dateP=r.get(Vente_.date);
        Path<String> deviseP=r.get(Vente_.devise);
        Path<String> refP=r.get(Vente_.ventePK).get(VentePK_.reference);
        Path<String> prodP=r.get(Vente_.ventePK).get(VentePK_.idProduit);
        Path<Integer> idP=r.get(Vente_.ventePK).get(VentePK_.id);
        cq.multiselect(dateP,deviseP,cb.sum(r.get("mantant")),cb.sum(r.get("quantite")),refP,prodP,idP);
        cq.where(cb.and(cb.like(r.get(Vente_.ventePK).get(VentePK_.reference), kiosque+"%"),cb.between(dateP, date1, date2),cb.equal(deviseP, devise)));
        cq.groupBy(r.get(Vente_.devise));
        Tuple t=em.createQuery(cq).getSingleResult();
        return ((Double)t.get(2));
    }

    @Override
    public List<Vente> getVenteForKiosqueAllDetails(String kiosque, Date date1, Date date2) {
       StringBuilder sb = new StringBuilder();
        sb.append("SELECT v._date,v.devise,v.mantant,v.quantite,v.reference,v.id_produit,v.id ")
                .append("FROM Vente v ")
                .append("WHERE v.reference LIKE ? AND v._date BETWEEN ? AND ? ");
        Query q=em.createNativeQuery(sb.toString(),Vente.class);
        q.setParameter(1, kiosque+"%").setParameter(2, date1,TemporalType.TIMESTAMP)
                .setParameter(3, date2,TemporalType.TIMESTAMP);
        return q.getResultList();
    }

}
