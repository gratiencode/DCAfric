/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import entities.CurentSolde;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jax_binding.SoldeBinder;
import stateless.CurentSoldeBean;

/**
 * REST Web Service
 *
 * @author gratien
 */
@Path("/smart")
@Stateless
public class SmartSoldesResource {

    @Context
    private UriInfo context;
    
    @EJB
    private CurentSoldeBean csb;

    /**
     * Creates a new instance of SmartSoldesResource
     */
    public SmartSoldesResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.SmartSoldesResource
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("view/{id_produit}/{id_agent}")
    public Response getSold(@PathParam("id_agent") String param_id_agent,@PathParam("id_produit") String param_id_pro) {
        CurentSolde cs=csb.findExact(param_id_agent, param_id_pro);
      return Response.ok(cs).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("view")
    public List<CurentSolde> getSolds() {
      return new SoldeBinder(csb.findAllSolde());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("view/{id_produit}")
    public List<CurentSolde> getSolds(@PathParam("id_produit") String produit) {
      return new SoldeBinder(csb.findAllSolde(produit));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("view/agent/{id_ag}")
    public List<CurentSolde> getSoldsForAgent(@PathParam("id_ag") String ag) {
      return new SoldeBinder(csb.findAllSoldeByAgent(ag)); 
    }

    /**
     * Sub-resource locator method for {name}
     */
    @Path("{id_agent}/{produit_id}")
    public SmartSoldeResource getSmartSoldeResource(@PathParam("id_agent") String agent,@PathParam("produit_id") String id_pro) {
        return SmartSoldeResource.getInstance(csb,agent,id_pro);
    }
}
