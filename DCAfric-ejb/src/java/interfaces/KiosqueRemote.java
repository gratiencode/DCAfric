/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Kiosque;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface KiosqueRemote {
    public Kiosque insertKiosque(Kiosque obj);
    public Kiosque updateKiosque(Kiosque obj);
    public void deleteKiosque(Kiosque obj);
    public List<Kiosque> AffichageKiosqueByLongitude(String obj);
    public List<Kiosque> AffichageKiosqueByLatitude(String Latitude);
    public List<Kiosque> AffichageKiosque();
    public List<Kiosque> localizeKiosques();
    public Kiosque localizeKiosque(String Qosk);
    public Kiosque getKiosque(String uid);
    
}
