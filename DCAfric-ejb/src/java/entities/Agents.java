/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SERVER
 */
@Entity
@Table(name = "agents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agents.findAll", query = "SELECT a FROM Agents a"),
    @NamedQuery(name = "Agents.findById", query = "SELECT a FROM Agents a WHERE a.id = :id"),
    @NamedQuery(name = "Agents.findByIdNpswd", query = "SELECT a FROM Agents a WHERE a.id = :id AND a.agentPassword = :password"),
    @NamedQuery(name = "Agents.findByNom", query = "SELECT a FROM Agents a WHERE a.nom LIKE :nom"),
    @NamedQuery(name = "Agents.findByCorrespondance", query = "SELECT a FROM Agents a WHERE a.nom = :nom AND a.postnom = :postnom AND a.prenom = :prenom"),
    @NamedQuery(name = "Agents.searchAgentByCompleteNamePostnameAndPrenom", query = "SELECT a FROM Agents a WHERE a.nom LIKE :nom AND a.postnom LIKE :postnom AND a.prenom LIKE :prenom"),
    @NamedQuery(name = "Agents.searchAgentByCompleteNamePostnom", query = "SELECT a FROM Agents a WHERE a.nom LIKE :nom AND a.postnom LIKE :postnom "),
    @NamedQuery(name = "Agents.findByPostnom", query = "SELECT a FROM Agents a WHERE a.postnom = :postnom"),
    @NamedQuery(name = "Agents.findByPrenom", query = "SELECT a FROM Agents a WHERE a.prenom = :prenom"),
    @NamedQuery(name = "Agents.findBySexe", query = "SELECT a FROM Agents a WHERE a.sexe LIKE :sexe"),
    @NamedQuery(name = "Agents.findByDateNaisse", query = "SELECT a FROM Agents a WHERE a.dateNaisse = :dateNaisse"),
    @NamedQuery(name = "Agents.findByAdresse", query = "SELECT a FROM Agents a WHERE a.adresse = :adresse"),
    @NamedQuery(name = "Agents.findByAgentPassword", query = "SELECT a FROM Agents a WHERE a.agentPassword = :agentPassword"),
    @NamedQuery(name = "Agents.findByRoleAgent", query = "SELECT a FROM Agents a WHERE a.roleAgent = :roleAgent"),
    @NamedQuery(name = "Agents.findByRoleWithPassword", query = "SELECT a FROM Agents a WHERE a.roleAgent = :roleAgent AND a.agentPassword = :agentPassword"),
    @NamedQuery(name = "Agents.findByBloquee", query = "SELECT a FROM Agents a WHERE a.bloquee = :bloquee")})
public class Agents implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "postnom")
    private String postnom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "prenom")
    private String prenom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sexe")
    private String sexe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_naisse")
    @Temporal(TemporalType.DATE)
    private Date dateNaisse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "adresse")
    private String adresse;
    @NotNull
    @Lob
    @Size(max = 65535)
    @Column(name = "agent_password")
    private String agentPassword;
    @NotNull
    @Lob
    @Size(max = 65535)
    @Column(name = "role_agent")
    private String roleAgent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "_bloquee")
    private boolean bloquee;

    public Agents() {
    }

    public Agents(String id) {
        this.id = id;
    }

    public Agents(String id, String nom, String postnom, String prenom, String sexe, Date dateNaisse, String adresse, String agentPassword, String roleAgent, boolean bloquee) {
        this.id = id;
        this.nom = nom;
        this.postnom = postnom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaisse = dateNaisse;
        this.adresse = adresse;
        this.agentPassword = agentPassword;
        this.roleAgent = roleAgent;
        this.bloquee = bloquee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPostnom() {
        return postnom;
    }

    public void setPostnom(String postnom) {
        this.postnom = postnom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaisse() {
        return dateNaisse;
    }

    public void setDateNaisse(Date dateNaisse) {
        this.dateNaisse = dateNaisse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAgentPassword() {
        return agentPassword;
    }

    public void setAgentPassword(String agentPassword) {
        this.agentPassword = agentPassword;
    }

    public String getRoleAgent() {
        return roleAgent;
    }

    public void setRoleAgent(String roleAgent) {
        this.roleAgent = roleAgent;
    }

    public boolean getBloquee() {
        return bloquee;
    }

    public void setBloquee(boolean bloquee) {
        this.bloquee = bloquee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agents)) {
            return false;
        }
        Agents other = (Agents) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Agents[ id=" + id + " ]";
    }
    
}
