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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author gratien
 */
public class RecquisitionResource {

    private String id;

    /**
     * Creates a new instance of RecquisitionResource
     */
    private RecquisitionResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the RecquisitionResource
     */
    public static RecquisitionResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of RecquisitionResource class.
        return new RecquisitionResource(id);
    }

    /**
     * Retrieves representation of an instance of jax_rs.RecquisitionResource
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of RecquisitionResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }

    /**
     * DELETE method for resource RecquisitionResource
     */
    @DELETE
    public void delete() {
    }
}
