/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.CommandeRemote;
import entities.Commande;
import entities.CommandePK;
import entities.Produit;
import entities.Vente;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import stateless.Recette;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/commandeSB")
@LocalBean
public class CommandeBeans implements CommandeRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Commande insertCommande(Commande obj) {
        em.persist(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Commande updateCommande(Commande obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteCommande(Commande obj) {
        em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Commande> AffichageCommandeByKiosque(String Kiosque) {
        return em.createNamedQuery("Commande.findByIdKiosque").setParameter("idKiosque", Kiosque).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Commande> AffichageCommandeByClient(String client) {
        return em.createNamedQuery("Commande.findByIdClient").setParameter("idClient", client).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Commande AffichageCommandeByReference(String reference) {
        return (Commande) em.createNamedQuery("Commande.findByReference").setParameter("reference", reference).getSingleResult();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Commande> AffichageCommandeByDate(String dateCommande) {
        return em.createNamedQuery("Commande.findByDate").setParameter("date", dateCommande).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Commande> AffichageCommande(Date d1, Date d2) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.comment, c._date_, c.libelle, c.nombre_article, c.valide, c.methode, c.reference, c.id_client, c.ID, c.id_kiosque ")
                .append("FROM Commande c ")
                .append("WHERE c._date_ BETWEEN ? AND ? ");
        Query q = em.createNativeQuery(sb.toString(),Commande.class);
        q.setParameter(1, d1, TemporalType.TIMESTAMP);
        q.setParameter(2, d2, TemporalType.TIMESTAMP);
        return q.getResultList();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Commande getCommande(int uid, String kiosque, String client, String reference) {
        return em.find(Commande.class, new CommandePK(uid, kiosque, client, reference));
    }

    @Override
    public List<Commande> getRecettesForPeriod(Date d1, Date d2) {
        List<Commande> lc = em.createNamedQuery("Commande.findOnPeriod").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
        return lc;
    }

    @Override
    public List<Commande> searchByProduit(String nomProduit) {
        List<Commande> rst = new ArrayList<>();
        List<Produit> lp = em.createNamedQuery("Produit.findBySearching").setParameter("nomProduit", nomProduit + "%").getResultList();
        for (Produit p : lp) {
            List<Vente> lv = em.createNamedQuery("Vente.findByIdProduit").setParameter("idProduit", p.getId()).getResultList();
            for (Vente v : lv) {
                Commande cmd = (Commande) em.createNamedQuery("Commande.findByReference").setParameter("reference", v.getVentePK().getReference()).getSingleResult();
                rst.add(cmd);
            }
        }
        return rst;
    }

    @Override
    public List<Commande> searchByProduit(String nomProduit, Date d1, Date d2) {

        List<Commande> rst = new ArrayList<>();
        List<Produit> lp = em.createNamedQuery("Produit.findBySearching").setParameter("nomProduit", nomProduit + "%").getResultList();
        for (Produit p : lp) {
            List<Vente> lv = em.createNamedQuery("Vente.findPerProduct").setParameter("date1", d1).setParameter("date2", d2).setParameter("idProduit", p.getId()).getResultList();
            for (Vente v : lv) {
                Commande cmd = (Commande) em.createNamedQuery("Commande.findByReference").setParameter("reference", v.getVentePK().getReference()).getSingleResult();
                rst.add(cmd);
            }
        }
        return rst;
    }

    @Override
    public List<Commande> searchByReference(String ref) {
        return em.createNamedQuery("Commande.searchByReference").setParameter("reference", ref + "%").getResultList();
    }

    @Override
    public List<Commande> searchByKiosque(String qosk, Date d1, Date d2) {
        return em.createNamedQuery("Commande.findByKiosqOnMobile").setParameter("idKiosque", qosk + "%").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public List<Commande> paginate(Date d1, Date d2) {
        return em.createNamedQuery("Commande.findOnPeriod").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public List<Commande> getRecettesForPeriodByProduit(String nomProd, Date d1, Date d2) {
        List<Produit> ps = em.createNamedQuery("Produit.findBySearching").setParameter("nomProduit", nomProd + "%").getResultList();
        List<Commande> cmds = new ArrayList<>();
        for (Produit p : ps) {
            List<Vente> ves = em.createNamedQuery("Vente.findPerProductForRecette").setParameter("idProduit", p.getId() + "%")
                    .setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
            for (Vente v : ves) {
                Commande cmd = (Commande) em.createNamedQuery("Commande.findByReference").setParameter("reference", v.getVentePK().getReference()).getSingleResult();
                cmds.add(cmd);
            }
        }
        return cmds;
    }

    @Override
    public List<Commande> getRecettesForPeriodByKiosque(String kiosque, Date d1, Date d2) {
        return em.createNamedQuery("Commande.findByKiosqOnMobile").setParameter("idKiosque", kiosque + "%").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public List<Commande> AffichageCommandeByMethode(String method, Date d1, Date d2) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.comment, c._date_, c.libelle, c.nombre_article, c.valide, c.methode, c.reference, c.id_client, c.ID, c.id_kiosque ")//select
                .append("FROM Commande c ")//from
                .append("WHERE c.methode LIKE ? AND c._date_ BETWEEN ? AND ?");//where
        Query query = em.createNativeQuery(sb.toString(), Commande.class);
        query.setParameter(1, method + "%");
        query.setParameter(2, d1, TemporalType.TIMESTAMP);
        query.setParameter(3, d2, TemporalType.TIMESTAMP);
        return query.getResultList();
    }

}
