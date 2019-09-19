/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ParametreService;
import entities.Parametre;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author PC
 */
public class ParametreResource {

    private String key;
    Parametre p;
    ParametreService ps;

    /**
     * Creates a new instance of ParametreResource
     */
    private ParametreResource(ParametreService ps,String key) {
        this.key = key;
        this.ps=ps;
        p=ps.getParametre(key);
    }

    /**
     * Get instance of the ParametreResource
     */
    public static ParametreResource getInstance(ParametreService ps,String key) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ParametreResource class.
        return new ParametreResource(ps,key);
    }

    /**
     * Retrieves representation of an instance of jax_rs.ParametreResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Response getParam() {
       return Response.ok(p).build();
    }

    /**
     * PUT method for updating or creating an instance of ParametreResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    /**
     * DELETE method for resource ParametreResource
     */
    @DELETE
    public void delete() {
    }
}
