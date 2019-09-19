/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Client;
import entities.Consommer;
import entities.ConsommerPK;
import entities.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.Constants;
import util.ServiceVendu;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class ConsommerService {

    @PersistenceContext(unitName = "dds_web")
    EntityManager em;

    public Consommer createVenteService(Consommer service) {
        em.persist(service);
        return service;
    }

    public Consommer updateConsommer(Consommer serv) {
        em.merge(serv);
        return serv;
    }

    public Consommer getConsommer(int id, String idService, String idClt) {
        return em.find(Consommer.class, new ConsommerPK(id, idService, idClt));
    }

    public ServiceVendu getServiceVendu(Consommer cons) {
        ConsommerPK pk = cons.getConsommerPK();
        Service s = em.find(Service.class, pk.getIdService());
        Client c = em.find(Client.class, pk.getIdClient());
        ServiceVendu sv = new ServiceVendu(pk.getId() + "-" + pk.getIdClient() + "-" + pk.getIdService(), s.getNomService(),
                c.getPrenom(), s.getUnitMesure(), cons.getDevise(), cons.getAutreInfo(), cons.getQuantite(),
                cons.getMontant(), Constants.dateFormat.format(cons.getDate()), cons.getEtat());
        Logger.getLogger(this.getClass().getName()).info("Identifiant " + sv.getIdService());
        return sv;
    }

    public List<Consommer> getClientPoints(String idClient) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c._date, c.id, c.id_kiosq,c.valide, c.montant, c.libelle, c.etat,c.devise, c.id_client, c.id_service, c.quantite ");
        sb.append("FROM Consommer c ");
        sb.append("WHERE c.id_client = ? AND c.valide = ? AND c._date <= ? ");
        Query query = em.createNativeQuery(sb.toString(), Consommer.class);
        query.setParameter(1, idClient);
        query.setParameter(2, false);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        d1.format(df);
        Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        query.setParameter(3, d, TemporalType.TIMESTAMP);
        return query.getResultList();
    }

    public List<ServiceVendu> getServiceVendu(String kiosk, String date1, String daste2) {
        List<Consommer> ccs = em.createNamedQuery("Consommer.findDaillyServices").setParameter("idQsk", kiosk)
                .setParameter("date1", date1).setParameter("date2", daste2).getResultList();
        List<ServiceVendu> svs = new ArrayList<>();
        for (Consommer c : ccs) {
            Service s = (Service) em.createNamedQuery("Service.findById").setParameter("id", c.getConsommerPK().getIdService()).getSingleResult();
            Client clt = (Client) em.createNamedQuery("Client.findById").setParameter("id", c.getConsommerPK().getIdClient()).getSingleResult();
            double sumTot = (double) em.createNamedQuery("Consommer.findAmounts").setParameter("idService", s.getId()).setParameter("date1", date1).setParameter("date2", daste2).getSingleResult();
            double quantV = (double) em.createNamedQuery("Consommer.findQuantities").setParameter("idService", s.getId()).setParameter("date1", date1).setParameter("date2", daste2).getSingleResult();
            ServiceVendu sv = new ServiceVendu();
            sv.setDate(Constants.dateFormat.format(c.getDate()));
            sv.setClientName(clt.getPrenom());
            sv.setEtat(c.getEtat());
            sv.setIdService(s.getId());
            sv.setMontatTotal(sumTot);
            sv.setNomService(s.getNomService());
            sv.setQuantVendu(quantV);
            sv.setUnitMesure(s.getUnitMesure());
            sv.setoInfo(c.getAutreInfo());
            svs.add(sv);
        }
        return svs;
    }

    public List<ServiceVendu> getServiceVenduPerClient(String id_clt, String date1, String daste2) {
        List<Consommer> ccs = em.createNamedQuery("Consommer.findByIdClient").setParameter("idClient", id_clt)
                .setParameter("date1", date1).setParameter("date2", daste2).getResultList();
        List<ServiceVendu> svs = new ArrayList<>();
        for (Consommer c : ccs) {
            Service s = (Service) em.createNamedQuery("Service.findById").setParameter("id", c.getConsommerPK().getIdService()).getSingleResult();
            Client clt = (Client) em.createNamedQuery("Client.findById").setParameter("id", id_clt).getSingleResult();
            double sumTot = (double) em.createNamedQuery("Consommer.findAmounts").setParameter("idService", s.getId()).setParameter("date1", date1).setParameter("date2", daste2).getSingleResult();
            double quantV = (double) em.createNamedQuery("Consommer.findQuantities").setParameter("idService", s.getId()).setParameter("date1", date1).setParameter("date2", daste2).getSingleResult();
            ServiceVendu sv = new ServiceVendu();
            sv.setDate(Constants.dateFormat.format(c.getDate()));
            sv.setClientName(clt.getPrenom());
            sv.setEtat(c.getEtat());
            sv.setIdService(s.getId());
            sv.setMontatTotal(sumTot);
            sv.setNomService(s.getNomService());
            sv.setQuantVendu(quantV);
            sv.setUnitMesure(s.getUnitMesure());
            sv.setoInfo(c.getAutreInfo());
            svs.add(sv);
        }
        return svs;
    }

}
