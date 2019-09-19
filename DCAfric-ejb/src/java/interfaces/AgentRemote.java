
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Agents;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pasfree
 */
@Remote
public interface AgentRemote {
    public Agents insertAgents(Agents obj);
    public Agents updateAgents(Agents obj);
    public void deleteAgents(Agents obj);
    public List<Agents> AffichageAgentsByNom(String nom);
    public List<Agents> AffichageAgentsByPostNom(String postnom);
    public List<Agents> AffichageAgentsByPrenom(String prenom);
    public List<Agents> AffichageAgentsBySexe(String sexe);
    public List<Agents> AffichageAgentsByRole(String role);
    public List<Agents> AffichageAgents();
    public Agents getAgent(String user,String password);
    public Agents getAgent(String id);
    public List<Agents> searchAgent(String nom);
    public List<Agents> searchAgent(String nom,String postnom);
    public List<Agents> searchAgent(String nom,String postnom,String prenom);
    public List<Agents> searchAgentSexe(String sexe);
    public List<Agents> searchBlockedAgent(boolean blocked);
    public Agents findByCorespondance(String nom, String postnom,String prenom);
}
