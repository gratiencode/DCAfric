/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ClientService;
import cdi_impl.ConsommerService;
import cdi_impl.Services;
import entities.Client;
import entities.Consommer;
import entities.ConsommerPK;
import entities.Service;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.PATCH; 
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import util.Constants;
import util.ServiceVendu;

/**
 * REST Web Service
 *
 * @author PC
 */
public class ConsommerResource {

    private ConsommerService cs;
    private Consommer c;
    @EJB
    Services s;
    @EJB
    ClientService csvc;

    /**
     * Creates a new instance of ConsommerResource
     */
    private ConsommerResource(ConsommerService cs, int id, String idService, String idClient) {
        this.cs = cs;
        this.c = cs.getConsommer(id, idService, idClient);
        Logger.getLogger(this.getClass()).info("Updating dev "+this.c.getDevise()+" id "+this.c.getConsommerPK().getId());
    }

    /**
     * Get instance of the ConsommerResource
     *
     * @param cs
     * @param id
     * @param idService
     * @param idClient
     * @return
     */
    public static ConsommerResource getInstance(ConsommerService cs, int id, String idService, String idClient) {
        return new ConsommerResource(cs, id, idService, idClient);
    }

    /**
     * Retrieves representation of an instance of jax_rs.ConsommerResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ConsommerResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PATCH 
    @Produces("application/json")
    @Path("/modifier/update")
    public Response update() {
        Logger.getLogger(this.getClass()).info("Updating dev "+this.c.getDevise()+" id "+this.c.getConsommerPK().getId());
        this.c.setEtat("Terminé");
         cs.updateConsommer(this.c);
         ConsommerPK cp=this.c.getConsommerPK();
        Service ss = s.getService(cp.getIdService());
        Client clt = csvc.getClient(this.c.getConsommerPK().getIdClient());
        ServiceVendu sv = new ServiceVendu();
        sv.setClientName(clt.getPrenom());
        sv.setDate(Constants.dateFormat.format(this.c.getDate()));
        sv.setEtat(c.getEtat());
        sv.setIdService(ss.getId());
        sv.setMontatTotal(c.getMontant());
        sv.setDevise(this.c.getDevise());
        sv.setNomService(ss.getNomService());
        sv.setQuantVendu(c.getQuantite());
        sv.setUnitMesure(ss.getUnitMesure());
        sv.setoInfo(c.getAutreInfo());
        return Response.ok(sv).build();
    }

    /**
     * DELETE method for resource ConsommerResource
     */
    @DELETE
    public void delete() {
    }
}
