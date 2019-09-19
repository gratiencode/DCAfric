/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Client;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface ClientRemote {
    public Client insertClient(Client obj);
    public Client updateClient(Client obj);
    public void deleteClient(Client obj);
    public List<Client> AffichageClientByPrenom(String prenom);
    public List<Client> AffichageClientByPhone(String phone);
    public List<Client> AffichageClientByJeton(String jeton);
    public List<Client> AffichageClient();
    public Client getClient(String id);
    public Client affichageOneByPhone(String phone);
}
