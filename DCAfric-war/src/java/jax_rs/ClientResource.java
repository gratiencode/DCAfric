/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ClientService;
import entities.Client;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author PC
 */
public class ClientResource {

    private String name;
    ClientService cs;
    Client clt;

    /**
     * Creates a new instance of ClientResource
     */
    private ClientResource(ClientService sc,String name) {
        this.name = name;
        this.cs=sc;
        clt=sc.getClient(name);
    }

    /**
     * Get instance of the ClientResource
     */
    public static ClientResource getInstance(ClientService cs,String name) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ClientResource class.
        return new ClientResource(cs,name);
    }

    /**
     * Retrieves representation of an instance of jax_rs.ClientResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("one_line")
    public Response getXml() {
        return Response.ok(clt).build();
    }

    /**
     * PUT method for updating or creating an instance of ClientResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    /**
     * DELETE method for resource ClientResource
     */
    @DELETE
    public void delete() {
    }
}
