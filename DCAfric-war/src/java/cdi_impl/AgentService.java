/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Agents;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class AgentService {

    @PersistenceContext(unitName = "dds_web", type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    public Agents createAgent(Agents agent) {
        em.persist(agent);
        return agent;
    }

    public Agents updateAgent(Agents agent) {
        em.merge(agent);
        return agent;
    }

    public Agents getAgent(String id) {
        return (Agents) em.createNamedQuery("Agents.findById").setParameter("id", id).getSingleResult();
    }

    public Agents auth(String id, String pass) {
        List<Agents> agents = em.createNamedQuery("Agents.findAll").getResultList();
        for (Agents ag : agents) {
            String inid = ag.getId();
            String rawpswd = ag.getAgentPassword();
            if (inid.equals(id) || rawpswd.equals(pass)) {
                return ag;
            }

        }
        return null;
    }

    public List<Agents> getAgents() {
        List<Agents> agents = em.createNamedQuery("Agents.findAll").getResultList();
        return agents;
    }

    public Agents authenicate(String id, String pass) {
        return methAuth(id, pass);
    }

    public Agents methAuth(String id, String pass) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT a.ID, a.adresse, a.agent_password, a._bloquee, a.date_naisse, a.nom, a.postnom, a.prenom, a.role_agent, a.sexe ")
                    .append("FROM Agents a ")
                    .append("WHERE a.ID = ? AND  a.agent_password = ?");
            Query q = em.createNativeQuery(sb.toString(), Agents.class);
            q.setParameter(1, id)
                    .setParameter(2, pass);
            return (Agents) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
