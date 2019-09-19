/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi_impl;

import entities.Service;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

/**
 *
 * @author PC
 */
@Stateless
@LocalBean
public class Services {

    @PersistenceContext(unitName = "dds_web", type = PersistenceContextType.TRANSACTION)
    EntityManager em;

    public Service createService(Service s) {
        em.persist(s);
        return s;
    }

    public List<Service> getServices() {
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT s.ID, s.info, s.nom_service, s.unit_mesure, s.tarif_cash, s.devise_cash, s.tarif_mobmoney, s.devise_mobmoney ")
                .append("FROM Service s ");
        Query query=em.createNativeQuery(sb.toString(),Service.class);
        return query.getResultList();
    }

    public Service getService(String id) {
        if (id != null) {
            return (Service) em.createNamedQuery("Service.findById").setParameter("id", id).getSingleResult();
        }else{
            return null;
        }
    }

    public Service getServiceByName(String name) {
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT s.ID, s.info, s.nom_service, s.unit_mesure, s.tarif_cash, s.devise_cash, s.tarif_mobmoney, s.devise_mobmoney ")
                .append("FROM Service s ").append(" WHERE s.nom_service LIKE ? ");
        Query query=em.createNativeQuery(sb.toString(),Service.class);
        return (Service) query.setParameter(1, "%" + name + "%").getSingleResult();
    }
}
