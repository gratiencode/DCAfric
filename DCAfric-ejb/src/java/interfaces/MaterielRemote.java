/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Materiel;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface MaterielRemote {
    public Materiel insertMateriel(Materiel obj);
    public Materiel updateMateriel(Materiel obj);
    public void deleteMateriel(Materiel obj);
    public List<Materiel> AffichageMaterielByName(String name);
    public List<Materiel> AffichageMaterielByModel(String model);
    public List<Materiel> AffichageMaterielByFabricant(String fabricant);
    public List<Materiel> AffichageMateriel();
    public List<Materiel> searchMateriel(String name);
    public Materiel getMateriel(String id);
    public Materiel affichageOneMaterielByName(String name);
}
