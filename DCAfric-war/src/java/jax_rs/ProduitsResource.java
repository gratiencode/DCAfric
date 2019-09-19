/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ProduitService;
import entities.Produit;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jax_binding.ProduitBinder;
import jax_binding.ProduitRecquisBinder;
import util.ProduitRecquis;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/products")
@Stateless
public class ProduitsResource {

    @Context
    private UriInfo context;
    @EJB
    ProduitService ps;

    /**
     * Creates a new instance of ProduitsResource
     */
    public ProduitsResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.ProduitsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("product_elements/for/myqosc/{idkiosq}")
    public List<Produit> getListProduct(@PathParam("idkiosq") String idkiosk) {
       return new ProduitBinder(ps.getAllMyProducts(idkiosk));
    }
    @GET
    @Produces("application/json")
    @Path("product_element/showall")
    public List<Produit> getProducts() {
       return new ProduitBinder(ps.getAllProducts());
    }
    
    @GET
    @Produces("application/json")
    @Path("product_elements/tosell/for/{idkiosq}")
    public List<ProduitRecquis> getProduct2Sell(@PathParam("idkiosq") String idkiosk) {
       return new ProduitRecquisBinder(ps.getAllProducts(idkiosk));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("customers/clients/ussd/v/{produit}")
    public Response getCompteGeneral(@PathParam("produit") String produit) {
        
        return Response.ok().build();
    }

    /**
     * Sub-resource locator method for {name}
     */
    @Path("{id}")
    public ProduitResource getProduitResource(@PathParam("id") String id) {
        return ProduitResource.getInstance(id);
    }
}
