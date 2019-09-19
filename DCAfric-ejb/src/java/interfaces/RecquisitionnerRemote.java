/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Recquisitionner;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface RecquisitionnerRemote {
    public Recquisitionner insertRecquisitionner(Recquisitionner obj);
    public Recquisitionner updateRecquisitionner(Recquisitionner obj);
    public void deleteRecquisitionner(Recquisitionner obj);
    public List<Recquisitionner> AffichageRecquisitionnerByKiosque(String kiosque);
    public List<Recquisitionner> paginate(Date d1,Date d2);
    public List<Recquisitionner> searchByKiosqueOnPeriod(String kiosque,Date d1,Date d2);
    public List<Recquisitionner> AffichageRecquisitionnerByProduit(String produit);
    public List<Recquisitionner> AffichageRecquisitionnerByDate(String date);
    public List<Recquisitionner> AffichageRecquisitionnerByQuantite(String quantite);
    public List<Recquisitionner> searchRecquisition(String produit,Date d1,Date d2);
    public List<Recquisitionner> searchRecquisition(Date d1,Date d2);
    public List<Recquisitionner> AffichageRecquisitionnerByPrix(String prod);
    public List<Recquisitionner> AffichageRecquisitionnerByProd(String prod);
    public List<Recquisitionner> AffichageRecquisitionneeByProduit_ByKiosque(String Produit,String Kiosque, String date1,String date2); 
    public Double AffichageQuantRecquisitionneeByProduit_ByKiosque(String Produit,String Kiosque, String date1,String date2); 
    public List<Recquisitionner> affichageRecquisitionner();
    public Recquisitionner getRecquisition(int uid, String kiosque,String produit);
    public Recquisitionner getLastRecquisitionProduit(String produitName);
}
