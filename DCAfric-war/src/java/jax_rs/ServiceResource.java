/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.Services;
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
public class ServiceResource {

    private String name;
    Services svc;
    /**
     * Creates a new instance of ServiceResource
     */
    private ServiceResource(String name, Services svc) {
        this.name = name;
        this.svc=svc;
    }

    /**
     * Get instance of the ServiceResource
     */
    public static ServiceResource getInstance(String name,Services svc) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ServiceResource class.
        return new ServiceResource(name,svc);
    }

    /**
     * Retrieves representation of an instance of jax_rs.ServiceResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ServiceResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
       //TODO return proper representation object  
    }

    /**
     * DELETE method for resource ServiceResource
     */
    @DELETE
    public void delete() {
    }
}
