/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.ProduitRemote;
import entities.Produit; 
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
@Stateless(mappedName="ejb/Stateless/ProduitSB")
@LocalBean
public class ProduitBeans implements ProduitRemote{

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Produit insertProduit(Produit obj) {
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Produit updateProduit(Produit obj) {
        em.merge(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteProduit(Produit obj) {
        em.remove(em.merge(obj));
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Produit> AffichageProduitByName(String name) {
        return em.createNamedQuery("Produit.findByNomProduit").setParameter("nomProduit", name).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Produit> AffichageProduitByFournisseur(String fournisseur) {
       return em.createNamedQuery("Produit.findByFournisseur").setParameter("fournisseur", fournisseur).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Produit> AffichageProduit() {
        return em.createNamedQuery("Produit.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Produit getProduit(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT v.id,v.fournisseur,nom_produit ")
                .append("FROM Produit v ")
                .append("WHERE v.id LIKE ? ");
        Query q=em.createNativeQuery(sb.toString(),Produit.class);
        q.setParameter(1, id+"%"); 
        return (Produit)q.getSingleResult(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Produit affichageByOneByName(String name) {
        return (Produit) em.createNamedQuery("Produit.findByNomProduit").setParameter("nomProduit", name).getSingleResult();
    }

    @Override
    public List<Produit> searchProduit(String name) {
       return em.createNamedQuery("Produit.findBySearching").setParameter("nomProduit", name+"%").getResultList();
    }
}
