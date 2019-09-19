/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Agents;
import entities.Kiosque;
import entities.Obtenir;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.Constants;
import util.QosKDetails;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class KiosqueService {

   
    @PersistenceContext(unitName = "dds_web")
    EntityManager em;

    public Kiosque getKiosqueForAgent(String idagent) {
        Logger.getLogger(this.getClass().getName()).info("ID agent >>>>>>>>>>>>>>>>>>>>>>>> "+idagent);
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT o._date, o.id_kiosq, o.ID, o.id_agent ")
                .append("FROM Obtenir o ").append("WHERE o.id_agent = ? ORDER BY o._date DESC ");
        
        Query query = em.createNativeQuery(sb.toString(), Obtenir.class);
        query.setParameter(1, idagent.trim());
       // List<Obtenir> obs=query.getResultList();
        
        Obtenir obtenir =(Obtenir) query.getSingleResult();
               //  obs.get(query.getFirstResult());
        
        StringBuilder sbk=new StringBuilder();
        sbk.append("SELECT k.ID, k.info, k.latitude, k.longitude ")
                .append("FROM Kiosque k ").append("WHERE k.ID = ? ");
        Query queryk = em.createNativeQuery(sbk.toString(), Kiosque.class);
        String kid=obtenir.getObtenirPK().getIdKiosq();
        Logger.getLogger(this.getClass().getName()).info("KID >>>>>>>>>>>>>>>>>>>>>>>> "+kid);
        Kiosque k = (Kiosque) queryk.setParameter(1,kid ).getSingleResult();
        return k;
    }

    public Kiosque getKiosque(String idkiosque) {
        Kiosque kiosk = (Kiosque) em.createNamedQuery("Kiosque.findById").setParameter("id", idkiosque).getSingleResult();
        return kiosk;
    }

    public List<Kiosque> getAllKiosques() {
        return em.createNamedQuery("Kiosque.findAll").getResultList();
    }

    public List<QosKDetails> getAllKiosque() {
        List<Obtenir> obtenirs = em.createNamedQuery("Obtenir.findAll").getResultList();
        List<QosKDetails> result = new ArrayList<>();
        for (Obtenir o : obtenirs) {
            Agents a = getAgentWithId(o.getObtenirPK().getIdAgent());
            Kiosque k = getKiosqueWithId(o.getObtenirPK().getIdKiosq());
            if (a != null && k != null) {
                QosKDetails qos = new QosKDetails();
                qos.setDateObtenir(Constants.dateFormat.format(o.getDate()));
                qos.setIdKiosque(k.getId());
                qos.setInfo(k.getInfo());
                qos.setLatitude(k.getLatitude());
                qos.setLongitude(k.getLongitude());
                qos.setNomAgent(a.getPrenom() + " " + a.getNom());
                result.add(qos);
            }
        }
        return result;
    }
    private Kiosque getKiosqueWithId(String id){
        try{
            return (Kiosque) em.createNamedQuery("Kiosque.findById").setParameter("id",id).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    private Agents getAgentWithId(String id){
        try{
            return (Agents) em.createNamedQuery("Agents.findById").setParameter("id", id).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public Kiosque updateKiosque(Kiosque kiosque) {
        em.merge(kiosque);

        return kiosque;
    }


    public QosKDetails getKiosqueDetail(String kiosq) {
        return getQOS(kiosq);
    }
    
    private QosKDetails getQOS(String kiosq){
        try{
            Query q = em.createNamedQuery("Obtenir.findByIdKiosq").setParameter("idKiosq", kiosq);
        List<Obtenir> lo = q.getResultList();
        if (!lo.isEmpty()) {
            Obtenir o = (Obtenir) lo.get(q.getFirstResult());
            Agents a = (Agents) em.createNamedQuery("Agents.findById").setParameter("id", o.getObtenirPK().getIdAgent()).getSingleResult();
            Kiosque k = (Kiosque) em.createNamedQuery("Kiosque.findById").setParameter("id", o.getObtenirPK().getIdKiosq()).getSingleResult();
            QosKDetails qos = new QosKDetails();
            qos.setDateObtenir(Constants.dateFormat.format(o.getDate()));
            qos.setIdKiosque(k.getId());
            qos.setInfo(k.getInfo());
            qos.setLatitude(k.getLatitude());
            qos.setLongitude(k.getLongitude());
            qos.setNomAgent(a.getPrenom() + " " + a.getNom());
            return qos;
        }
        return null;
        }catch(NoResultException e){
            return null;
        }
        
    }

}
