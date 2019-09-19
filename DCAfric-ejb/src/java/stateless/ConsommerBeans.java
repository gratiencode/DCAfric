/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.ConsommerRemote;
import entities.Consommer;
import entities.ConsommerPK;
import entities.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import util.Recette;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName = "ejb/Stateless/consommerSB")
@LocalBean
public class ConsommerBeans implements ConsommerRemote {

    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Consommer insertConsommer(Consommer obj) {
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Consommer updateConsommer(Consommer obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteConsommer(Consommer obj) {
        em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByService(String service) {
        return em.createNamedQuery("Consommer.findByIdService").setParameter("idService", service).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByClient(String client) {
        return em.createNamedQuery("Consommer.findByIdClient").setParameter("idClient", client).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerDate(String date) {
        return em.createNamedQuery("Consommer.findByDate").setParameter("date", date).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByKiosque(String kiosque) {
        return em.createNamedQuery("Consommer.findByIdKiosq").setParameter("idKiosq", kiosque).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByQuantite(String quantite) {
        return em.createNamedQuery("Consommer.findByQuantite").setParameter("quantite", quantite).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByDay_ByKiosque(String day, String Kiosque) {

        return em.createNamedQuery("Consommer.findByMontantByDay_ByKiosque").setParameter("date", day).setParameter("idKiosq", Kiosque).getResultList();
        // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByDay(String date) {
        return em.createNamedQuery("Consommer.findByDate").setParameter("date", date).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerByBlocked(String etat) {
        return em.createNamedQuery("Consommer.findByEtat").setParameter(etat, etat).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommer() {
        return em.createNamedQuery("Consommer.findAll").getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Consommer> AffichageConsommerQuantiteByClient(String client) {
        return em.createNamedQuery("Consommer.findByQantiteByClient").setParameter("client", client).getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Consommer getConsommer(int id, String service, String client) {
        return (Consommer) em.find(Consommer.class, new ConsommerPK(id, service, client));
    }

    @Override
    public List<Consommer> getRecetteForPeriod(Date d1, Date d2) {
        List<Consommer> lc = em.createNamedQuery("Consommer.findOnPeriod").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
        return lc;
    }

    @Override
    public double getSumRecetteByDevise(String devise, Date d1, Date d2) {
        return getRecetteSvc(devise, d1, d2);
    }

    private double getRecetteSvc(String devise, Date d1, Date d2) {
        try {
            TypedQuery tq = em.createNamedQuery("Consommer.findSumDeviseOnPeriod", Double.class);
            Double d = (Double) tq.setParameter("devise", devise).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
            if (d == null) {
                return 0;
            }
            return d;
        } catch (NoResultException ne) {
            return 0;
        }
    }

    @Override
    public List<Consommer> getRecetteByService(String svc, Date d1, Date d2) {
        List<Consommer> lc = em.createNamedQuery("Consommer.findByServiceOnPeriod").setParameter("servc", svc).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
        return lc;
    }

    @Override
    public double getSumRecetteByDeviseOnService(String devise, String svc, Date d1, Date d2) {
        return getRecetteSvc(devise, svc, d1, d2);
    }

    private double getRecetteSvc(String devise, String svc, Date d1, Date d2) {
        try {
            TypedQuery tq = em.createNamedQuery("Consommer.findSumBySrvcWithDeviseOnPeriod", Double.class);
            double d = (double) tq.setParameter("devise", devise).setParameter("idServ", svc).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
            return d;
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<Consommer> searchByService(String service) {
        List<Consommer> rst = new ArrayList<>();
        List<Service> ls = em.createNamedQuery("Service.findBySearching").setParameter("nomService", service).getResultList();
        for (Service s : ls) {
            List<Consommer> lc = em.createNamedQuery("Consommer.findByIdService").setParameter("idService", s.getId()).getResultList();
            for (Consommer c : lc) {
                if (!rst.contains(c)) {
                    rst.add(c);
                }
            }
        }
        return rst;
    }

    @Override
    public List<Consommer> searchByService(String service, Date d1, Date d2) {
        List<Consommer> rst = new ArrayList<>();
        List<Service> ls = em.createNamedQuery("Service.findBySearching").setParameter("nomService", service + "%").getResultList();
        for (Service s : ls) {
            List<Consommer> lc = em.createNamedQuery("Consommer.findByServiceOnPeriod")
                    .setParameter("servc", s.getId())
                    .setParameter("date1", d1, TemporalType.TIMESTAMP)
                    .setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
            for (Consommer c : lc) {
                if (!rst.contains(c)) {
                    rst.add(c);
                }
            }
        }
        return rst;
    }

    @Override
    public List<Consommer> searchByKiosque(String kiosque, Date d1, Date d2) {
        List<Consommer> lc = em.createNamedQuery("Consommer.findByKiosqOnPeriod").setParameter("kiosq", kiosque + "%").setParameter("date1", d1, TemporalType.TIMESTAMP)
                .setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
        return lc;
    }

    @Override
    public List<Consommer> paginate(Date d1, Date d2) {
        return em.createNamedQuery("Consommer.findOnPeriod").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public double getSommeByService(String svc,String devise, Date d1, Date d2) {
        try {
            TypedQuery tquery = em.createNamedQuery("Consommer.findSumServiceOnPeriod", Double.class);
            Double s = (Double) tquery.setParameter("idService", svc).setParameter("devise", devise).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
            if (s == null) {
                return 0;
            }
            return s;
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public double getQuantiteForService(String svc, String devise,Date d1, Date d2) {
        try {
            TypedQuery tquery = em.createNamedQuery("Consommer.findQuantityForServiceOnPeriod", Double.class);
            Double d = (Double) tquery.setParameter("idService", svc).setParameter("devise", devise).setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getSingleResult();
            if (d == null) {
                return 0;
            }
            return d;
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<Consommer> getRecettesOnPeriod(Date d1, Date d2) {
        return em.createNamedQuery("Consommer.findRecetteForService").setParameter("date1", d1, TemporalType.TIMESTAMP).setParameter("date2", d2, TemporalType.TIMESTAMP).getResultList();
    }

    @Override
    public List<Consommer> AffichageConsommerByMethode(String method, Date d1, Date d2) {
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT c.autre_info, c._date, c.devise, c.etat, c.id_kiosq, c.libelle, c.montant, c.quantite, c.methode, c.valide, c.id_client, c.id_service, c.ID ")
                .append("FROM Consommer c ").append("WHERE c.methode LIKE ? AND c._date BETWEEN ? AND ? ");
        Query query=em.createNativeQuery(sb.toString(),Consommer.class);
        query.setParameter(1, method+"%");
        query.setParameter(2, d1,TemporalType.TIMESTAMP);
        query.setParameter(3, d2,TemporalType.TIMESTAMP);  
        return query.getResultList();
    }

}
