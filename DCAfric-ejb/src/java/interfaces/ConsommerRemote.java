/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Consommer;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.Recette;

/**
 *
 * @author pasfree
 */
@Remote
public interface ConsommerRemote {
    public Consommer insertConsommer(Consommer obj);
    public Consommer updateConsommer(Consommer obj);
    public void deleteConsommer(Consommer obj);
    public List<Consommer> AffichageConsommerByService(String service);
    public List<Consommer> searchByService(String service);
    public List<Consommer> searchByService(String service, Date d1,Date d2);
    public List<Consommer> searchByKiosque(String kiosque, Date d1,Date d2);
    public List<Consommer> paginate(Date d1,Date d2);
    public List<Consommer> AffichageConsommerByClient(String client);
    public List<Consommer> AffichageConsommerDate(String date);
    public List<Consommer> AffichageConsommerByKiosque(String kiosque);
    public List<Consommer> AffichageConsommerByQuantite(String quantite);
    public List<Consommer> AffichageConsommerByDay_ByKiosque(String day,String Kiosque);
    public List<Consommer> AffichageConsommerQuantiteByClient(String client);
    public List<Consommer> AffichageConsommerByDay(String date);
    public List<Consommer> AffichageConsommerByBlocked(String etat);
    public List<Consommer> AffichageConsommerByMethode(String method,Date d1,Date d2);
    public double getSumRecetteByDevise(String devise,Date d1,Date d2);
    public double getSumRecetteByDeviseOnService(String devise,String svc,Date d1,Date d2);
    public List<Consommer> getRecetteByService(String svc,Date d1,Date d2);
    public List<Consommer> getRecettesOnPeriod(Date d1,Date d2); 
    public double getSommeByService(String svc,String devise,Date d1,Date d2);
    public double getQuantiteForService(String svc,String devise,Date d1,Date d2);
    public List<Consommer> getRecetteForPeriod(Date d1,Date d2);
    public List<Consommer> AffichageConsommer();
    public Consommer getConsommer(int id,String service,String client);
}
