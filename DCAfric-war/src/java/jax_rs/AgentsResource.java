/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.AgentService;
import entities.Agents;
import java.text.ParseException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import jax_binding.AgentBinder;
import util.Constants;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/agents")
@Stateless
public class AgentsResource {

    @Context
    private UriInfo context;
    @EJB
    AgentService asvc;

    /**
     * Creates a new instance of AgentsResource
     */
    public AgentsResource() {
    }

    @GET
    @Path("/showall")
    @Produces("application/json")
    public List<Agents> getAgentsWhenTesting() {
        return new AgentBinder(asvc.getAgents());
    }

    /**
     * Retrieves representation of an instance of jax_rs.AgentsResource
     *
     * @return an instance of javax.json.JsonObject
     */
    @GET
    @Produces("application/json")
    public JsonObject getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("u5/expso1/newuser")
    @Produces("application/json")
    @Consumes("application/json")
    public Response signUpNewUser(Agents agent) throws ParseException {
        Agents ag = null;
        if (agent != null) {
            String password = agent.getAgentPassword().split(":")[0];
            String dateNs = agent.getAgentPassword().split(":")[1];
            agent.setDateNaisse(Constants.dateFormat.parse(dateNs + " 00:00:00"));
            agent.setAgentPassword(password);
            agent.setRoleAgent("Client");
            Agents exist = asvc.getAgent(agent.getId());
            if (exist == null) {
                ag = asvc.createAgent(agent);
            }else{
                ag=new Agents();
                ag.setNom("Un autre compte utilise ce numero choisissez un autre");
            }
        }
        
        return Response.ok(ag).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("agent/{id}/{pswd}")
    public AgentResource getAgentResource(@PathParam("id") String name, @PathParam("pswd") String pwd) {
        return AgentResource.getInstance(asvc, name, pwd);
    }
}
