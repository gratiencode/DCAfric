/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Client;
import entities.Commande;
import entities.Consommer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author PC
 */
@Stateless
public class ClientService {

    @PersistenceContext(unitName = "dds_web")
    EntityManager em;

    public Client createClient(Client cl) {
        em.persist(cl);
        return cl;
    }

    public List<Client> getAllClientFromVenteService(String date, String kiosk) {
        List<Consommer> vente = em.createNamedQuery("Consommer.findByDate").setParameter("date", date).setParameter("idKsk", kiosk).getResultList();
        List<Client> cs = new ArrayList<>();
        for (Consommer v : vente) {
            Client c = (Client) em.createNamedQuery("Client.findById").setParameter("id", v.getConsommerPK().getIdClient()).getSingleResult();
            cs.add(c);
        }
        return cs;
    }

    public List<Client> getAllClientForKioskFromService(String kiosk) {
        List<Consommer> vente = em.createNamedQuery("Consommer.findByKiosq").setParameter("kiosk", kiosk).getResultList();
        List<Client> cs = new ArrayList<>();
        for (Consommer v : vente) {
            Client c = (Client) em.createNamedQuery("Client.findById").setParameter("id", v.getConsommerPK().getIdClient()).getSingleResult();
            cs.add(c);
        }
        return cs;
    }

    public Client getClient(String id) {
        return em.find(Client.class, id);
    }

    public List<Client> getAllClientFromVenteProduit(String date, String idksk) {
        List<Commande> vente = em.createNamedQuery("Commande.findByDateOnKiosq").setParameter("date", date).setParameter("idkiosque", idksk).getResultList();
        List<Client> cs = new ArrayList<>();
        for (Commande v : vente) {
            Client c = (Client) em.createNamedQuery("Client.findById").setParameter("id", v.getCommandePK().getIdClient()).getSingleResult();
            cs.add(c);
        }
        return cs;
    }

    public List<Client> getAllClientForKioskFromVente(String idksk) {
        List<Commande> vente = em.createNamedQuery("Commande.findByIdKiosque").setParameter("idKiosque", idksk).getResultList();
        List<Client> cs = new ArrayList<>();
        for (Commande v : vente) {
            Client c = (Client) em.createNamedQuery("Client.findById").setParameter("id", v.getCommandePK().getIdClient()).getSingleResult();
            cs.add(c);
        }
        return cs;
    }

    public List<Client> getClientByName(String name) {
        List<Client> lst = em.createNamedQuery("Client.findByPrenom").setParameter("prenom", name + "%").getResultList();
        return lst;
    }

    public List<Client> getClients() {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.id, c.phone, c.prenom ")
                    .append("FROM client c ");
        List<Client> lst = em.createNativeQuery(sb.toString(),Client.class).getResultList();
        return lst;
    }

    public Client getClientByPhone(String phone) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.id, c.phone, c.prenom ")
                    .append("FROM client c ").append("WHERE c.phone = ?");
            Query query = em.createNativeQuery(sb.toString(), Client.class);
            query.setParameter(1, phone);
            return (Client) query.getSingleResult();
        } catch (EJBException | NoResultException e) {
            return null;
        }
    }

}
