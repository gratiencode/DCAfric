/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;

/**
 * REST Web Service
 *
 * @author PC
 */
public class ProduitResource {

    private String name;

    /**
     * Creates a new instance of ProduitResource
     */
    private ProduitResource(String name) {
        this.name = name;
    }

    /**
     * Get instance of the ProduitResource
     */
    public static ProduitResource getInstance(String name) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ProduitResource class.
        return new ProduitResource(name);
    }

    /**
     * Retrieves representation of an instance of jax_rs.ProduitResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ProduitResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    /**
     * DELETE method for resource ProduitResource
     */
    @DELETE
    public void delete() {
    }
}
