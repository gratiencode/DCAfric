/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Service;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface ServiceRemote {
    public Service insertService(Service obj);
    public Service updateService(Service obj);
    public void deleteService(Service obj);
    public List<Service> AffichageServiceByName(String name);
    public List<Service> AffichageServiceByUnitMesure(String unit);
    public List<Service> AffichageService();
    public Service getService(String id);
    public Service affichageOneByName(String name);
}
