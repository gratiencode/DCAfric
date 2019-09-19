/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import interfaces.ClientRemote;
import entities.Client;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pasfree
 */
@Stateless(mappedName="ejb/Stateless/clientSB")
@LocalBean
public class ClientBeans implements ClientRemote {
    @PersistenceContext(unitName = "Districom_afric-ejbPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Client insertClient(Client obj) { 
        em.persist(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Client updateClient(Client obj) {
        em.merge(obj);
        return obj;
        //To change body of generated methods, choose Tools | Templates.
    }
    
    public Client getClientLocaly(String phone){ 
        return em.find(Client.class, phone);
    }

    @Override
    public void deleteClient(Client obj) {
        em.remove(em.merge(obj));
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Client> AffichageClientByPrenom(String prenom) {
        return em.createNamedQuery("Client.findByPrenom").setParameter("prenom", prenom).getResultList(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Client> AffichageClientByPhone(String phone) {
       return em.createNamedQuery("Client.findByPhone").setParameter("phone", phone).getResultList(); 
    }

    @Override
    public List<Client> AffichageClientByJeton(String jeton) {
        return em.createNamedQuery("Client.findByNumeroJeton").setParameter("numeroJeton", jeton).getResultList();
    }

    @Override
    public List<Client> AffichageClient() {
        return em.createNamedQuery("Client.findAll").getResultList(); 
    }

    @Override
    public Client getClient(String id) {
       return (Client)em.createNamedQuery("Client.findById").setParameter("id", id).getSingleResult(); 
    }

    @Override
    public Client affichageOneByPhone(String phone) {
        return (Client)em.createNamedQuery("Client.findByPhone").setParameter("phone", phone).getSingleResult();
    }
}
