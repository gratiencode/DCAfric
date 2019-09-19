/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Produit;
import entities.Recquisitionner;
import entities.RecquisitionnerPK_;
import entities.Recquisitionner_;
import entities.Vente;
import entities.VentePK_;
import entities.Vente_;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import util.Constants;
import util.ProduitRecquis;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class ProduitService {

    @PersistenceContext(unitName = "dds_web")
    EntityManager em;

    public List<Produit> getAllMyProducts(String idQosk) {
        List<Produit> result = new ArrayList<>();
        List<Recquisitionner> recqs = em.createNamedQuery("Recquisitionner.findByIdKiosque").setParameter("idKiosque", idQosk).getResultList();
        for (Recquisitionner recq : recqs) {
            Produit p = (Produit) em.createNamedQuery("Produit.findById").setParameter("id", recq.getRecquisitionnerPK().getIdProduit()).getSingleResult();
            result.add(p);
        }
        return result;
    }

    public List<Produit> getAllProducts() {
        return em.createNamedQuery("Produit.findAll").getResultList();
    }
    
   

    public Produit getProduct(String id) {
        return (Produit) em.createNamedQuery("Produit.findById").setParameter("id", id).getSingleResult();
    }

    public List<ProduitRecquis> getAllProducts(String idQosk) {
        List<ProduitRecquis> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT r.commentaire, r._date, r.devise, r.prix_unit, r.quant, r.id_produit, r.ID, r.id_kiosque ")
                .append("FROM Recquisitionner r ")
                .append("WHERE r.id_kiosque = ? ");
        Query q = em.createNativeQuery(sb.toString(), Recquisitionner.class);
        q.setParameter(1, idQosk);
        List<Recquisitionner> recqs = q.getResultList();
        for (Recquisitionner recq : recqs) {
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT p.ID, p.fournisseur, p.nom_produit ")
                    .append("FROM Produit p ")
                    .append("WHERE p.ID = ? ");
            Query q1 = em.createNativeQuery(sbd.toString(), Produit.class);
            q1.setParameter(1, recq.getRecquisitionnerPK().getIdProduit());
            Produit p = (Produit) q1.getSingleResult();
            ProduitRecquis pr = new ProduitRecquis();
            pr.setDate(Constants.dateFormat.format(recq.getDate()));
            pr.setForunisseur(p.getFournisseur());
            pr.setIdProduit(p.getId());
            pr.setDevise(recq.getDevise());
            pr.setPrixUnitaire(recq.getPrixUnit());
            pr.setNomProduit(p.getNomProduit());
            pr.setQuantiteRecu(recq.getQuant());
            result.add(pr);
        }
        return result;
    }

}
