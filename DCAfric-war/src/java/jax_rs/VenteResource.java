/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.VenteService;
import entities.Vente;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author PC
 */
public class VenteResource {

    private int name;
    private String ref;
    private String prod;
    private VenteService vs;
    Vente v;
    

    /**
     * Creates a new instance of VenteResource
     */
    private VenteResource(VenteService vs,int name,String ref,String prod) {
        this.name = name;
        this.vs=vs;
        this.ref=ref;
        this.prod=prod; 
    }

    /**
     * Get instance of the VenteResource
     */
    public static VenteResource getInstance(VenteService vs,int n,String ref,String prod) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of VenteResource class.
        return new VenteResource(vs,n,ref,prod);
    }

    /**
     * Retrieves representation of an instance of jax_rs.VenteResource
     * @return an instance of javax.json.JsonObject
     */
    @GET
    @Produces("application/json")
    @Path("view")
    public Vente getJson() {
        v=vs.getVente(name,this.ref,this.prod);
        return v;
    }

    /**
     * PUT method for updating or creating an instance of VenteResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(JsonObject content) {
    }

    /**
     * DELETE method for resource VenteResource
     */
    @DELETE
    public void delete() {
    }
}
