/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import entities.CurentSolde;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.CurentSoldeBean;

/**
 * REST Web Service
 *
 * @author gratien
 */
public class SmartSoldeResource {

    private String idag;
    CurentSoldeBean csb;
    CurentSolde cs;

    /**
     * Creates a new instance of SmartSoldeResource
     */
    private SmartSoldeResource(CurentSoldeBean csb,String id_agent, String id_produit) {
        this.idag= id_agent;
        this.csb=csb;
        cs=csb.findExact(id_agent, id_produit);
    }

    /**
     * Get instance of the SmartSoldeResource
     */
    public static SmartSoldeResource getInstance(CurentSoldeBean csb,String id_agent,String id_produit) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of SmartSoldeResource class.
        return new SmartSoldeResource(csb,id_agent,id_produit);
    }

    /**
     * Retrieves representation of an instance of jax_rs.SmartSoldeResource
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of SmartSoldeResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("update_point")
    public void putJson(CurentSolde content) {
        if(cs==null){
            //
           
        }else{
            
        }
    }

    /**
     * DELETE method for resource SmartSoldeResource
     */
    @DELETE
    public void delete() {
    }
}
