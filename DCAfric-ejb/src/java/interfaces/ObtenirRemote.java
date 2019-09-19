/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Kiosque;
import entities.Obtenir;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface ObtenirRemote {
    public Obtenir insertObtenir(Obtenir obj);
    public Obtenir updateObtenir(Obtenir obj);
    public void deleteObtenir(Obtenir obj);
    public List<Obtenir> AffichageObtenirByAgent(String agent);
    public List<Obtenir> searchObtenir(String nomComplet);
    public List<Obtenir> searchObtenirByKiosque(String kiosq);
    public List<Obtenir> searchObtenirByKiosqueOnPeriod(String kiosq,Date d1,Date d2);
    public List<Obtenir> AffichageObtenirByKiosque(String kiosque);
    public List<Obtenir> AffichageObtenirByDate(Date date,Date d2);
    public List<Obtenir> paginate(Date date1,Date date2);
    public List<Obtenir> AffichageObtenir();
    public Kiosque getLastObtentionForAgent(String id); 
    public Obtenir getObtenir(int id,String agent,String kiosq);
}
