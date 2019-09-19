/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Commande;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import stateless.Recette;

/**
 *
 * @author pasfree
 */
@Remote
public interface CommandeRemote {
    public Commande insertCommande(Commande obj);
    public Commande updateCommande(Commande obj);
    public void deleteCommande(Commande obj);
    public List<Commande> AffichageCommandeByKiosque(String Kiosque);
    public List<Commande> AffichageCommandeByClient(String client);
    public List<Commande> searchByProduit(String nomProduit);
    public List<Commande> searchByProduit(String nomProduit,Date d1,Date d2);
    public List<Commande> searchByKiosque(String nomProduit,Date d1,Date d2);
    public List<Commande> searchByReference(String reference);
    public List<Commande> paginate(Date d1,Date d2);
    public Commande AffichageCommandeByReference(String reference);
    public List<Commande> AffichageCommandeByDate(String dateCommande);
     public List<Commande> AffichageCommandeByMethode(String method, Date d1,Date d2);
    public List<Commande> AffichageCommande(Date d1,Date d2);
    public List<Commande> getRecettesForPeriod(Date d1,Date d2);
    public List<Commande> getRecettesForPeriodByProduit(String nomProd,Date d1,Date d2);
    public List<Commande> getRecettesForPeriodByKiosque(String kiosque,Date d1,Date d2);
    public Commande getCommande(int uid,String kiosque,String client,String reference);
}
