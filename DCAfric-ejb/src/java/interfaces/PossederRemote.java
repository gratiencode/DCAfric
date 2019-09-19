/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Posseder;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface PossederRemote {
    public Posseder insertPosseder(Posseder obj);
    public Posseder updatePosseder(Posseder obj);
    public void deleteposseder(Posseder obj);
    public List<Posseder> AffichagePossederByKiosque(String kiosque);
    public List<Posseder> AffichagePossederByMateriel(String materiel);
    public List<Posseder> AffichagePossederByDate(String date);
    public List<Posseder> paginate(Date d1,Date d2);
    public List<Posseder> searchMaterielOnKiosque(String idKiosque);
    public List<Posseder> searchMateriel(String materiel,Date d1,Date d2);
    public List<Posseder> searchMateriel(String materiel);
    public List<Posseder> AffichagePosseder();
    public Posseder getPossession(int id,String kiosque,String materiel);
}
