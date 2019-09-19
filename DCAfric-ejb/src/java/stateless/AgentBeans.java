/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import com.sun.istack.logging.Logger;
import interfaces.AgentRemote;
import entities.Agents;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/agentSB")
public class AgentBeans implements AgentRemote { 

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Agents insertAgents(Agents obj) {
        try {
            em.persist(obj);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();
            for (ConstraintViolation dt : cvs) {
                String s = dt.getMessage();
                Logger.getLogger(this.getClass()).info("Valeur detail faute >>>>>>>>> " + s);
            }
        }
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Agents updateAgents(Agents obj) {
        em.merge(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAgents(Agents obj) {
        em.remove(em.merge(obj));
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agents> AffichageAgentsByNom(String nom) {
        return em.createNamedQuery("Agents.findByNom").setParameter("nom", nom).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agents> AffichageAgentsByPostNom(String postnom) {
        return em.createNamedQuery("Agents.findByPostnom").setParameter("postnom", postnom).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agents> AffichageAgentsByPrenom(String prenom) {
        return em.createNamedQuery("Agents.findByPrenom").setParameter("prenom", prenom).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agents> AffichageAgentsBySexe(String sexe) {
        return em.createNamedQuery("Agents.findBySexe").setParameter("sexe", sexe).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agents> AffichageAgentsByRole(String role) {
        return em.createNamedQuery("Agents.findByRoleAgent").setParameter("role", role).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agents> AffichageAgents() {
        return em.createNamedQuery("Agents.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Agents getAgent(String role, String password) {
        return (Agents) em.createNamedQuery("Agents.findByRoleWithPassword").setParameter("roleAgent", role).setParameter("agentPassword", password).getSingleResult(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Agents getAgent(String id) {
        try{
        return (Agents) em.createNamedQuery("Agents.findById").setParameter("id", id).getSingleResult();  //To change body of generated methods, choose Tools | Templates.
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public Agents findByCorespondance(String nom, String postnom, String prenom) {
        return (Agents) em.createNamedQuery("Agents.findByCorrespondance").setParameter("nom", nom).setParameter("postnom", postnom).setParameter("prenom", prenom).getSingleResult();
    }

    @Override
    public List<Agents> searchAgent(String nom, String postnom, String prenom) {
        return em.createNamedQuery("Agents.searchAgentByCompleteNamePostnameAndPrenom").setParameter("nom", nom+"%").setParameter("postnom", postnom+"%")
                .setParameter("prenom",prenom+"%").getResultList();                                                     
    }

    @Override
    public List<Agents> searchAgentSexe(String sexe) {
       return em.createNamedQuery("Agents.findBySexe").setParameter("sexe", sexe+"%").getResultList();
    }

    @Override
    public List<Agents> searchBlockedAgent(boolean blocked) {
      return  em.createNamedQuery("Agents.findByBloquee").setParameter("bloquee", blocked).getResultList();
    }

    @Override
    public List<Agents> searchAgent(String nom, String postnom) {
        return em.createNamedQuery("Agents.searchAgentByCompleteNamePostnom").setParameter("nom", nom+"%").setParameter("postnom", postnom+"%").getResultList();
    }

    @Override
    public List<Agents> searchAgent(String nom) {
        return em.createNamedQuery("Agents.findByNom").setParameter("nom", nom+"%").getResultList();
    }
}
