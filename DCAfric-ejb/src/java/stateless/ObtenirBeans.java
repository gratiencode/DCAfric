/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entities.Agents;
import entities.Kiosque;
import interfaces.ObtenirRemote;
import entities.Obtenir;
import entities.ObtenirPK;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName="ejb/Stateless/ObtenirSB")
@LocalBean
public class ObtenirBeans implements ObtenirRemote{ 
    @PersistenceContext(unitName="Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Obtenir insertObtenir(Obtenir obj) {
        em.persist(obj);
        return obj;
        
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Obtenir updateObtenir(Obtenir obj) {
        em.merge(obj);
        return obj;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteObtenir(Obtenir obj) {
        em.remove(em.merge(obj)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Obtenir> AffichageObtenirByAgent(String agent) {
        return em.createNamedQuery("Obtenir.findByIdAgent").setParameter("idAgent", agent).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Obtenir> AffichageObtenirByKiosque(String kiosque) {
        return em.createNamedQuery("Obtenir.findByIdKiosq").setParameter("idKiosque", kiosque).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Obtenir getByKiosq(String kiosq){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT o.id, o.id_agent,o.id_kiosq,o._date ")
                .append("FROM obtenir o ").append("WHERE o.id_kiosq = ? ");
        Query q=em.createNativeQuery(sb.toString(),Obtenir.class);
        q.setParameter(1, kiosq);
        return (Obtenir)q.getSingleResult();
    }

    @Override 
    public List<Obtenir> AffichageObtenirByDate(Date date,Date d2) {
        return em.createNamedQuery("Obtenir.findBySearchDate").setParameter("date1", date,TemporalType.DATE).setParameter("date2", d2,TemporalType.DATE).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Obtenir> AffichageObtenir() {
        return em.createNamedQuery("Obtenir.findAll").getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }
     
    public Obtenir getObtenir(String agnt){
       StringBuilder sb=new StringBuilder();
       sb.append("SELECT o.id,o.id_agent,o.id_kiosq,o._date FROM obtenir o WHERE o.id_agent = ? ");
       Query q=em.createNativeQuery(sb.toString(),Obtenir.class);
       q.setParameter(1, agnt);
       return (Obtenir)q.getSingleResult(); 
    }
    
    

    @Override
    public Obtenir getObtenir(int id,String agent,String kiosq) {
        ObtenirPK opk=new ObtenirPK();
        opk.setId(id);
        opk.setIdAgent(agent);
        opk.setIdKiosq(kiosq);
        return em.find(Obtenir.class, opk);
    }
    
    

    @Override
    public Kiosque getLastObtentionForAgent(String id) {
       Query q=em.createNamedQuery("Obtenir.findLast").setParameter("idAgent", id);
       Obtenir lo=(Obtenir)q.getResultList().get(q.getFirstResult());
       Kiosque k=(Kiosque)em.createNamedQuery("Kiosque.findById").setParameter("id", lo.getObtenirPK().getIdKiosq()).getSingleResult();
       return k;
    }

    @Override
    public List<Obtenir> searchObtenir(String nomComplet) {
        String nc[]=nomComplet.split(" ");
        List<Obtenir> ob=null;
        if(nc.length==1){
            List<Agents> la=em.createNamedQuery("Agents.findByNom").setParameter("nom", nc[0]).getResultList();
            for(Agents a: la){
              ob =em.createNamedQuery("Obtenir.findByIdAgent").setParameter("idAgent", a.getId()).getResultList();
            }
            return ob;
        }else if(nc.length==2){
            List<Agents> la=em.createNamedQuery("Agents.searchAgentByCompleteNamePostnom").setParameter("nom", nc[0]).setParameter("postnom", nc[1]).getResultList();
            for(Agents a: la){
               ob=em.createNamedQuery("Obtenir.findByIdAgent").setParameter("idAgent", a.getId()).getResultList();
            }
            return ob;
        }else if(nc.length==3){
          List<Agents> la=em.createNamedQuery("Agents.searchAgentByCompleteNamePostnameAndPrenom").setParameter("nom", nc[0]).setParameter("postnom", nc[1])
                  .setParameter("prenom", nc[2]).getResultList();
            for(Agents a: la){
               ob=em.createNamedQuery("Obtenir.findByIdAgent").setParameter("idAgent", a.getId()).getResultList();
            }
            return ob;  
                    
        }
        return null;
    }

    @Override
    public List<Obtenir> searchObtenirByKiosque(String kiosq) {
        return em.createNamedQuery("Obtenir.findByIdKiosq").setParameter("idKiosq", kiosq).getResultList();
    }

    @Override
    public List<Obtenir> searchObtenirByKiosqueOnPeriod(String kiosq, Date d1, Date d2) {
       return em.createNamedQuery("Obtenir.findByKiosqOnPeriod").setParameter("idKiosque", kiosq+"%")
               .setParameter("date1", d1,TemporalType.DATE).setParameter("date2", d2,TemporalType.DATE).getResultList();
    }

    @Override
    public List<Obtenir> paginate(Date date1, Date date2) {
        return em.createNamedQuery("Obtenir.findBySearchDate").setParameter("date1", date1).setParameter("date2", date2).getResultList();
    }
    
    
}
