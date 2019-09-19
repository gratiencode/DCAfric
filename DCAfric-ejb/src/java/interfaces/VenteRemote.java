/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Vente;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.Recette;

/**
 *
 * @author pasfree
 */
@Remote
public interface VenteRemote {
    public Vente insertVente(Vente obj);
    public Vente updateVente(Vente obj);
    public void deleteVente(Vente obj);
    public List<Vente> paginate(Date d1,Date d2);
    public List<Vente> AffichageVenteByProduit(String produit);
    public List<Vente> AffichageVenteByKiosque(String kiosque);
    public List<Vente> AffichageVenteByClient(String client);
    public List<Vente> AffichageVenteByMontant(double montant);
    public List<Vente> AffichageVenteByDate(String date);
    public List<Vente> AffichageMontantVenteByDate_ByKiosque(String date,String kiosque);
    public List<Vente> AffichageParRefernce(String reference);
    public List<Vente> AffichageVente();
    public double getSommeParDevise(String devise,Date d1,Date d2);
    public double getSommeParReference(String ref,Date d1,Date d2);
    public double getSommeParDevise(String devise,String product,Date d1,Date d2);
    public double getSommeParDeviseParKiosque(String devise,String kiosque,Date d1,Date d2);
    public double getSommeParProduit(String product,Date d1,Date d2);
    public List<Vente> getVenteParProduit(String product,Date d1,Date d2);
    
    public List<Vente> getVenteForKiosqueGroupByProductOnPeriod(String kiosque,Date date1,Date date2);
    public Double getSumVenteForKiosqueOnPeriod(String kiosque,String dev,Date date1,Date date2);
    public List<Vente> getVenteForKiosqueAllDetails(String kiosque,Date date1,Date date2);
    
    public List<Vente> getRecetteParProduit(String product,Date d1,Date d2);
    public Vente getVente(int id,String produit,String reference);
}
