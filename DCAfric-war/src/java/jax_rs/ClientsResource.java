/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ClientService;
import cdi_impl.ConsommerService;
import cdi_impl.ParametreService;
import cdi_impl.VenteService;
import entities.Client;
import entities.Commande;
import entities.Consommer;
import entities.Parametre;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jax_binding.ClientBinder;
import stateless.ClientBeans;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/clients")
@Stateless
public class ClientsResource {

    @Context
    private UriInfo context;
    
    @EJB
    ClientService cs;
    
    @EJB
    VenteService vs;

    @EJB
    ConsommerService css;
    
    @EJB
    ParametreService ps;

    
    /**
     * Creates a new instance of ClientsResource
     */
    public ClientsResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.ClientsResource
     * @param prenom
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("client_name/{nom}")
    public List<Client> getClient(@PathParam("nom") String prenom) {
        return new ClientBinder(cs.getClientByName(prenom));
    }
    @GET
    @Produces("application/json")
    @Path("get_client/all")
    public List<Client> getClient() {
        return new ClientBinder(cs.getClients());
    }
    
    @GET
    @Produces("application/json")
    @Path("client_phone/{phone}")
    public Response getClientByPhone(@PathParam("phone") String phone){
       Client c=null;
       try{
             Logger.getLogger(this.getClass().getName()).log(Level.INFO,">>>>>>>>>>>>>"+ phone);
            c=cs.getClientByPhone(phone);
            if(c==null){
                String id=String.valueOf((int)(Math.random()*1000000));
                c=new Client();
                c.setId(id);
                c.setPhone(phone);
                c.setPrenom("CLT-"+id);
                cs.createClient(c);
            }
           return Response.ok(c).build(); 
        }catch(EJBException | javax.persistence.NoResultException e){
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, phone+" : "+e.getMessage());
            return Response.ok(c).build();
        }
        
    }
    
    
    @GET
    @Produces("application/json")
    @Path("client/eligibility/byphone/{phone}")
    public Response checkEligibility(@PathParam("phone") String phone){
        Client c=cs.getClientByPhone(phone);
            if(c==null){
                String id=String.valueOf((int)(Math.random()*1000000));
                c=new Client();
                c.setId(id);
                c.setPhone(phone);
                c.setPrenom("CLT-"+id);
                cs.createClient(c);
            }
        List<Commande> cl=vs.getClientPoints(c.getId());
        List<Consommer> csl=css.getClientPoints(c.getId());
        int somme=cl.size()+csl.size();
        Parametre p=ps.getParametre("CRITERE-BONUS");
        int value=Integer.parseInt(p.getValeur());
        return Response.ok((somme>=value)).build();
    }
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("client/input/data")
    public Response createClient(Client c){ 
        Client cl=cs.createClient(c);
        return Response.ok(cl).build();
    }
    

    /**
     * Sub-resource locator method for {name}
     */
    @Path("{name}")
    public ClientResource getClientResource(@PathParam("name") String name) {
        return ClientResource.getInstance(cs,name);
    }
}
