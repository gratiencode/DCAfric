/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.AgentService;
import entities.Agents;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import util.EncryptUtil;

/**
 * REST Web Service
 *
 * @author PC
 */
public class AgentResource {

    private String name;
    Agents agent;

    /**
     * Creates a new instance of AgentResource
     */
    private AgentResource(AgentService as,String id,String psd) {
        try{
            
        this.agent=as.authenicate(id, psd);
//       / Logger.getLogger(AgentResource.class).info("Compte "+agent.getNom()+" found with "+agent.getBloquee()+" state");
        if(this.agent==null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    /**
     * Get instance of the AgentResource
     */
    public static AgentResource getInstance(AgentService as,String id,String pwd) {
        return new AgentResource(as,id,pwd);
    }

    /**
     * Retrieves representation of an instance of jax_rs.AgentResource
     * @return an instance of javax.json.JsonObject
     */
    @GET
    @Produces("application/json")
    @Path("show")
    public Response getAgent() {
        return Response.ok(this.agent).build();
    }

    /**
     * PUT method for updating or creating an instance of AgentResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(JsonObject content) {
    }

    /**
     * DELETE method for resource AgentResource
     */
    @DELETE
    public void delete() {
    }
}
