/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Produit;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface ProduitRemote {
    public Produit insertProduit(Produit obj);
    public Produit updateProduit(Produit obj);
    public void deleteProduit(Produit obj);
    public List<Produit> AffichageProduitByName(String name);
    public List<Produit> searchProduit(String name);
    public List<Produit> AffichageProduitByFournisseur(String fournisseur);
    public List<Produit> AffichageProduit();
    public Produit getProduit(String id);
    public Produit affichageByOneByName(String name);
    
}
