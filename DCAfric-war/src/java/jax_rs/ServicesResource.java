/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.Services;
import entities.Service;
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
import jax_binding.ServiceBinder;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/service")
@Stateless
public class ServicesResource {

    @Context
    private UriInfo context;
    
    @EJB
    private Services svc;

    /**
     * Creates a new instance of ServicesResource
     */
    public ServicesResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.ServicesResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("list_all")
    public List<Service> getOurServices() {
       return new ServiceBinder(svc.getServices());
    }
    
//    @GET
//    @Produces("application/json")
//    @Path("get_svc/svc_id/{name}")
//    public Response getService(@PathParam("name") String servName){
//        
//    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servc/one/{svc}")
    public Response getServiceByName(@PathParam("svc") String s){
        Service sc=svc.getServiceByName(s);
        return Response.ok(sc).build();
    }

    /**
     * Sub-resource locator method for {name}
     */
    @Path("id-{id}")
    public ServiceResource getServiceResource(@PathParam("id") String name) {
        return ServiceResource.getInstance(name,svc);
    }
}
