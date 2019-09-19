/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ConsommerService;
import cdi_impl.ParametreService;
import cdi_impl.VenteService;
import entities.Parametre;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jax_binding.ParameterBinder;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/params")
@Stateless
public class ParametresResource {

    @Context
    private UriInfo context;
    @EJB
    private ParametreService ps;

   

    /**
     * Creates a new instance of ParametresResource
     */
    public ParametresResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.ParametresResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/byvalue/{value}")
    public List<Parametre> getListParams(@PathParam("value") String value) {
        return new ParameterBinder(ps.getParametreByValue(value));
    }

    /**
     * Sub-resource locator method for {key}
     */
    @Path("{key}")
    public ParametreResource getParametreResource(@PathParam("key") String key) {
        return ParametreResource.getInstance(ps, key);
    }
}
