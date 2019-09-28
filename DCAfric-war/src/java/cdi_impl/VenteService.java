/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Agents;
import entities.Agents_;
import entities.Commande;
import entities.CommandePK_;
import entities.Commande_;
import entities.Kiosque;
import entities.Kiosque_;
import entities.Obtenir;
import entities.ObtenirPK_;
import entities.Obtenir_;
import entities.Produit;
import entities.Recquisitionner;
import entities.RecquisitionnerPK_;
import entities.Recquisitionner_;
import entities.Vente;
import entities.VentePK;
import entities.VentePK_;
import entities.Vente_;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import util.Constants;
import util.ProduitVendu;
import util.TopClient;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class VenteService {

    @PersistenceContext(unitName = "dds_web", type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    public Vente createVente(Vente vente) {
        em.persist(vente);
        return vente;
    }

    public double getLastPrice(String idProd) {
        Query query = em.createNamedQuery("Recquisitionner.findByIdProduit").setParameter("idProduit", idProd);
        Recquisitionner r = (Recquisitionner) query.getResultList().get(query.getFirstResult());
        return r.getPrixUnit();
    }

    public Vente getVente(int id, String ref, String prod) {
        VentePK vpk = new VentePK();
        vpk.setId(id);
        vpk.setIdProduit(prod);
        vpk.setReference(ref);
        return em.find(Vente.class, vpk);
    }

    public double getStock(String idProd, String date1, String date2) {
        double quantRec = (double) em.createNamedQuery("Recquisitionner.findProductSum").setParameter("idPro", idProd)
                .setParameter("date1", date1).setParameter("date2", date2).getSingleResult();
        double quantVend = (double) em.createNamedQuery("Vente.findSumQVendu").setParameter("prod", idProd)
                .setParameter("date1", date1).setParameter("date2", date2).getSingleResult();
        return quantRec - quantVend;
    }

    public Commande createCommande(Commande cmd) {
        try {
            em.persist(cmd);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();
            for (ConstraintViolation dt : cvs) {
                String s = dt.getMessage();
                Logger.getLogger(this.getClass().getName()).info(" Valeur detail faute >>>>>>>>> === " + s);
            }
        }
        return cmd;
    }

    public List<Vente> getAllVentes(String kiosk) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c._date, c.id, c.id_produit, c.devise, c.mantant, c.quantite, c.reference ");
        sb.append("FROM vente c ");
        sb.append("WHERE c.reference LIKE ? ");
        Query query = em.createNativeQuery(sb.toString(), Vente.class);
        query.setParameter(1, kiosk + "%");
        return query.getResultList();
    }

    public Commande updateCommande(Commande cmd) {
        em.merge(cmd);
        return cmd;
    }

    public Commande getFromReference(String ref) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.id, c.id_kiosque, c.id_client, c.reference, c.libelle, c.nombre_article, c.valide, c._date_, c.comment ");//select
            sb.append("FROM commande c "); //from
            sb.append("WHERE c.reference = ?");
            Query query = em.createNativeQuery(sb.toString(), Commande.class);
            query.setParameter(1, ref);
            return (Commande) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Vente> getVentesByReference(String ref) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c._date, c.id, c.id_produit, c.devise, c.mantant, c.quantite, c.reference ");
        sb.append("FROM Vente c ");
        sb.append("WHERE c.reference = ? ");
        Query query = em.createNativeQuery(sb.toString(), Vente.class);
        query.setParameter(1, ref);
        return query.getResultList();
    }

    public List<Commande> getClientPoints(String idClient) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c._date_, c.id, c.id_kiosque, c.valide, c.libelle, c.id_client, c.reference, c.nombre_article, c.comment ");
        sb.append("FROM Commande c ");
        sb.append("WHERE c.id_client = ? AND c.valide = ? AND c._date_ <= ? ");
        Query query = em.createNativeQuery(sb.toString(), Commande.class);
        query.setParameter(1, idClient);
        query.setParameter(2, false);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        d1.format(df);
        Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        query.setParameter(3, d, TemporalType.TIMESTAMP);
        return query.getResultList();
    }

    public List<Commande> getCommands(String idKiosq, String date1, String date2) {
        return em.createNamedQuery("Commande.findByDateIntervalOnKiosq").setParameter("idkiosque", idKiosq)
                .setParameter("date1", date1).setParameter("date2", date2).getResultList();

    }

    public List<ProduitVendu> loadVentesForCommande(String references, String idKiosque) {
        Commande cmds = (Commande) em.createNamedQuery("Commande.findByReference").setParameter("reference", references).getSingleResult();
        List<Vente> lv = em.createNamedQuery("Vente.findByReference").setParameter("reference", cmds.getCommandePK().getReference()).getResultList();
        Calendar c = Calendar.getInstance();
        int i = -1;
        c.add(Calendar.MONTH, i);
        Logger.getLogger(this.getClass().getName()).info(" dates " + Constants.dateFormat.format(c.getTime()) + " , " + Constants.dateFormat.format(new Date()));

        List<ProduitVendu> pvs = new ArrayList<>();
        for (Vente v : lv) {
            Produit p = (Produit) em.createNamedQuery("Produit.findById").setParameter("id", v.getVentePK().getIdProduit()).getSingleResult();
            Query q = em.createNamedQuery("Recquisitionner.findByIdProduit").setParameter("idProduit", p.getId());
            Recquisitionner r = (Recquisitionner) q.getResultList().get(q.getFirstResult());
            Logger.getLogger(this.getClass().getName()).info(p.getId() + "dats ..." + Constants.dateFormat.format(c.getTime()) + " , " + Constants.dateFormat.format(new Date()) + " " + v.getVentePK().getIdProduit());

            TypedQuery<Double> tqReq = em.createNamedQuery("Recquisitionner.findQuantiteByIdProduitByIdKiosque", Double.class);
            Double sommeRecqisiton = tqReq.setParameter("idProduit", v.getVentePK().getIdProduit()).setParameter("idKiosque", idKiosque)
                    .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();

            TypedQuery<Double> tqVs = em.createNamedQuery("Vente.findSumQuantByReference", Double.class);
            Double sommeVente = tqVs.setParameter("reference", idKiosque + "%").setParameter("idProduit", p.getId())
                    .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();
            //Constants.dateFormat.format()Constants.dateFormat.format()
            while (sommeVente == null || sommeRecqisiton == null) {
                --i;
                c.add(Calendar.MONTH, i);

                sommeVente = tqVs.setParameter("reference", idKiosque + "%").setParameter("idProduit", p.getId())
                        .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();
                sommeRecqisiton = tqReq.setParameter("idProduit", v.getVentePK().getIdProduit()).setParameter("idKiosque", idKiosque)
                        .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();
                if (i == -12) {
                    sommeVente = 0d;
                    sommeRecqisiton = 0d;
                    break;
                }
            }

            Logger.getLogger(this.getClass().getName()).info(p.getId() + "Details vente Req " + sommeRecqisiton + " ve " + sommeVente + " dates " + Constants.dateFormat.format(c.getTime()) + " , " + Constants.dateFormat.format(new Date()));
            ProduitVendu pv = new ProduitVendu();
            pv.setDate(Constants.dateFormat.format(cmds.getDate()));
            pv.setFournisseur(p.getFournisseur());
            pv.setIdProduit(p.getId());
            pv.setMontantTotalVendu(v.getMantant());
            pv.setNomProduit(p.getNomProduit());
            pv.setQuaniteVendu(v.getQuantite());
            pv.setPrixUnitaire(r.getPrixUnit());
            pv.setReference(v.getVentePK().getReference());
            pv.setDevise(v.getDevise());
            pv.setQuantiteRestant((getDouble(sommeRecqisiton) - getDouble(sommeVente)));
            pvs.add(pv);
        }
        return pvs;

    }

    private double getDouble(Double d) {
        if (d == null) {
            return 0;
        }
        return d;
    }

    public Double getSommeQuantVenduPerKiosq(String idProduit, String kiosq) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Vente> r = cq.from(Vente.class);
        Path<String> prodP = r.get(Vente_.ventePK).get(VentePK_.idProduit);
        Path<String> kiosqP = r.get(Vente_.ventePK).get(VentePK_.reference);
        cq.select(cb.sum(r.get("quantite")));
        cq.where(cb.and(cb.like(kiosqP, kiosq), cb.equal(prodP, idProduit)));
        Double t = em.createQuery(cq).getSingleResult();
        return t;
    }

    public List<TopClient> gettop10() {
        List<TopClient> top10 = new ArrayList<>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
        Root<Vente> rvente = cq.from(Vente.class);
        Root<Commande> rcommande = cq.from(Commande.class);
        Root<Kiosque> rkiosq = cq.from(Kiosque.class);
        Root<Agents> rAgent = cq.from(Agents.class);
        Root<Obtenir> rObtenir = cq.from(Obtenir.class);

        Path<String> kidKiosP = rkiosq.get(Kiosque_.id);
        Path<String> oidAgP = rObtenir.get(Obtenir_.obtenirPK).get(ObtenirPK_.idAgent);
        Path<String> oidKiosP = rObtenir.get(Obtenir_.obtenirPK).get(ObtenirPK_.idKiosq);

        Path<String> agNomP = rAgent.get(Agents_.nom);
        Path<String> agPrenomP = rAgent.get(Agents_.prenom);
        Path<String> agID = rAgent.get(Agents_.id);
        Path<String> cidKiosP = rcommande.get(Commande_.commandePK).get(CommandePK_.idClient);

        Path<String> vrefer = rvente.get(Vente_.ventePK).get(VentePK_.reference);
        Path<String> crefer = rcommande.get(Commande_.commandePK).get(CommandePK_.reference);
        Expression exp = cb.count(rcommande.get("id_client"));

        cq.multiselect(exp.alias("freq"), cb.max(rvente.get("quantite")).alias("somme"), cidKiosP, agID, agPrenomP, agNomP);

        cq.where(cb.and(cb.equal(vrefer, crefer), cb.equal(cidKiosP, kidKiosP), cb.equal(agID, oidAgP), cb.equal(oidKiosP, cidKiosP)));
        cq.groupBy(rcommande.get(Commande_.commandePK).get(CommandePK_.idClient), rAgent.get(Agents_.id));
        cq.orderBy(cb.desc(exp));

        TypedQuery<Tuple> typedQuery = em.createQuery(cq);
        typedQuery.setMaxResults(10);
        for (Tuple t : typedQuery.getResultList()) {
            TopClient topClt = new TopClient();
            topClt.setFrequence(t.get("freq", Integer.class));
            topClt.setId(t.get(agID));
            topClt.setKiosque(t.get(cidKiosP));
            topClt.setNom(t.get(agNomP));
            topClt.setPrenom(t.get(agPrenomP));
            topClt.setQuantite(t.get("somme", Double.class));
            top10.add(topClt);
        }
        return top10;

    }

    public Double getSommeQuantVenteVendu(String idProduit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Vente> r = cq.from(Vente.class);
        Path<String> prodP = r.get(Vente_.ventePK).get(VentePK_.idProduit);
        cq.select(cb.sum(r.get("quantite")));
        cq.where(cb.equal(prodP, idProduit));
        Double t = em.createQuery(cq).getSingleResult();
        return t;
    }

    public Double getSommeQuantByProduitRecquit(String idProduit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Recquisitionner> r = cq.from(Recquisitionner.class);
        Path<String> prodP = r.get(Recquisitionner_.recquisitionnerPK).get(RecquisitionnerPK_.idProduit);
        cq.select(cb.sum(r.get("quant")));
        cq.where(cb.equal(prodP, idProduit));
        Double t = em.createQuery(cq).getSingleResult();
        return t;
    }

    public Double getSommeQuantAchatPerKiosq(String idProduit, String kiosq) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Recquisitionner> r = cq.from(Recquisitionner.class);
        Path<String> prodP = r.get(Recquisitionner_.recquisitionnerPK).get(RecquisitionnerPK_.idProduit);
        Path<String> kiosqP = r.get(Recquisitionner_.recquisitionnerPK).get(RecquisitionnerPK_.idKiosque);
        cq.select(cb.sum(r.get("quant")));
        cq.where(cb.and(cb.like(kiosqP, kiosq), cb.equal(prodP, idProduit)));
        Double t = em.createQuery(cq).getSingleResult();
        return t;
    }

    public double getSumVendu(String idKiosque, String idProduit) {
        Calendar c = Calendar.getInstance();
        int i = -12;
        c.add(Calendar.MONTH, i);
        TypedQuery<Double> tqReq = em.createNamedQuery("Recquisitionner.findQuantiteByIdProduitByIdKiosque", Double.class);
        Double sommeRecqisiton = tqReq.setParameter("idProduit", idProduit).setParameter("idKiosque", idKiosque)
                .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();

        TypedQuery<Double> tqVs = em.createNamedQuery("Vente.findSumQuantByReference", Double.class);
        Double sommeVente = tqVs.setParameter("reference", idKiosque + "%").setParameter("idProduit", idProduit)
                .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();
        //Constants.dateFormat.format()Constants.dateFormat.format()
        while (sommeVente == null || sommeRecqisiton == null) {
            --i;
            c.add(Calendar.MONTH, i);

            sommeVente = tqVs.setParameter("reference", idKiosque + "%").setParameter("idProduit", idProduit)
                    .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();
            sommeRecqisiton = tqReq.setParameter("idProduit", idProduit).setParameter("idKiosque", idKiosque)
                    .setParameter("date1", c.getTime()).setParameter("date2", new Date()).getSingleResult();
            if (i == -12) {
                sommeVente = 0d;
                sommeRecqisiton = 0d;
                break;
            }
        }

        return (getDouble(sommeRecqisiton) - getDouble(sommeVente));
    }

    public List<ProduitVendu> getVentes(String kiosk, String date1, String date2) {
        List<Recquisitionner> reqs = em.createNamedQuery("Recquisitionner.findByIdKiosque").setParameter("idKiosque", kiosk).getResultList();
        List<ProduitVendu> prvd = new ArrayList<>();
        for (Recquisitionner r : reqs) {
            Produit p = (Produit) em.createNamedQuery("Produit.findById").setParameter("id", r.getRecquisitionnerPK().getIdProduit()).getSingleResult();
            double quantRec = (double) em.createNamedQuery("Recquisitionner.findProductSum").setParameter("idPro", r.getRecquisitionnerPK().getIdProduit())
                    .setParameter("date1", date1).setParameter("date2", date2).getSingleResult();
            double quantVend = (double) em.createNamedQuery("Vente.findSumQVendu").setParameter("prod", r.getRecquisitionnerPK().getIdProduit())
                    .setParameter("date1", date1).setParameter("date2", date2).getSingleResult();
            double restant = quantRec - quantVend;
            double montantVend = quantVend * r.getPrixUnit();
            ProduitVendu pv = new ProduitVendu();
            pv.setDate(date2);
            pv.setFournisseur(p.getFournisseur());
            pv.setIdProduit(r.getRecquisitionnerPK().getIdProduit());
            pv.setMontantTotalVendu(montantVend);
            pv.setNomProduit(p.getNomProduit());
            pv.setPrixUnitaire(r.getPrixUnit());
            pv.setQuaniteVendu(quantVend);
            pv.setQuantiteRestant(restant);

            prvd.add(pv);
        }
        return prvd;

    }

}
