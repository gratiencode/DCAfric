/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces; 

import entities.Parametre; 
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface ParametreRemote {
    public Parametre insertParamtre(Parametre obj);
    public Parametre updateParametre(Parametre obj);
    public void delete(Parametre obj);
    public List<Parametre> AffichageParametre();
    public Parametre getParametre(String key);
}
