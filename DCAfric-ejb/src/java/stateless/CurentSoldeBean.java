/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entities.CurentSolde;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gratien
 */
@Stateless
@LocalBean
public class CurentSoldeBean {

    // Add business logic below. (Right-click in editor and choose
    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public CurentSolde insertSolde(CurentSolde obj) { 
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    
    public CurentSolde updateSolde(CurentSolde obj) {
        em.merge(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }
    
    public CurentSolde getSolde(String id){ 
        return em.find(CurentSolde.class, id);
    }
    
    public void deleteSolde(String id){
        em.remove(em.merge(getSolde(id)));
    }
    
    
    public CurentSolde findByAgentID(String id_agent){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT c.id, c.agent_id, c.product_id, c.solde ")
                .append("FROM curent_solde c ")
                .append("WHERE c.agent_id = ?");
        Query query=em.createNativeQuery(sb.toString(),CurentSolde.class);
        query.setParameter(1, id_agent);
        return (CurentSolde)query.getSingleResult();
    }
    
    public CurentSolde findExact(String id_agent,String id_produit){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT c.id, c.agent_id, c.product_id, c.solde ")
                .append("FROM curent_solde c ")
                .append("WHERE c.agent_id = ? AND c.product_id = ? ");
        Query query=em.createNativeQuery(sb.toString(),CurentSolde.class);
        query.setParameter(1, id_agent);
        query.setParameter(2, id_produit);
        return (CurentSolde)query.getSingleResult();
    }
    
    public List<CurentSolde> findAllSolde(){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT c.id, c.agent_id, c.product_id, c.solde ")
                .append("FROM curent_solde c ");
         Query query=em.createNativeQuery(sb.toString(),CurentSolde.class);
        return query.getResultList();
    }
    
    public List<CurentSolde> findAllSolde(String prodId){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT c.id, c.agent_id, c.product_id, c.solde ")
                .append("FROM curent_solde c ")
                .append("WHERE c.product_id = ? ");
         Query query=em.createNativeQuery(sb.toString(),CurentSolde.class);
         query.setParameter(1, prodId);
        return query.getResultList();
    }
    
    public List<CurentSolde> findAllSoldeByAgent(String IdAg){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT c.id, c.agent_id, c.product_id, c.solde ")
                .append("FROM curent_solde c ")
                .append("WHERE c.agent_id = ? ");
         Query query=em.createNativeQuery(sb.toString(),CurentSolde.class);
         query.setParameter(1, IdAg);
        return query.getResultList();
    }
            

    
}
