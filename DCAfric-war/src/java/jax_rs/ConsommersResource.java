/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ClientService;
import cdi_impl.ConsommerService;
import cdi_impl.Services;
import com.districom.endeleya.districomservices.tools.Promotion;
import entities.Client;
import entities.Consommer;
import entities.Service;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import jax_binding.PromotionBinder;
import jax_binding.ServiceVenduBinder;
import util.ServiceVendu;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/consummers")
@Stateless
public class ConsommersResource {

    @Context
    private UriInfo context;
    @EJB
    ConsommerService cs;
    
    @EJB
    ClientService cltsvc;
    
    @EJB
    Services svcs;
    
    SimpleDateFormat df =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Creates a new instance of ConsommersResource
     */
    public ConsommersResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.ConsommersResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("consumer/vendus/interval/{kiosk}/{date1}/{date2}")
    public List<ServiceVendu> getServices(@PathParam("kiosk") String qosk, @PathParam("date1") String date1, @PathParam("date2") String date2) {
        return new ServiceVenduBinder(cs.getServiceVendu(qosk, date1, date2));
    }

    @GET
    @Produces("application/json")
    @Path("consumer/vendus/interval/client/{id_clt}/{date1}/{date2}")
    public List<ServiceVendu> getServicesByClient(@PathParam("id_clt") String clt, @PathParam("date1") String date1, @PathParam("date2") String date2) {
        return new ServiceVenduBinder(cs.getServiceVenduPerClient(clt, date1, date2));
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("consumer/input/service")
    public Response createService(Consommer c) {
        DateTimeFormatter daf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        d1.format(daf);
        Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        c.setDate(d);
        String lib=c.getLibelle();
        c.setLibelle((lib==null)?"Normal":lib);
        c.setValide(true);
               
        Consommer cc = cs.createVenteService(c);
         
        if(c.getLibelle().equals("Bonus")){
           List<Consommer> lc=cs.getClientPoints(c.getConsommerPK().getIdClient()); 
            for(Consommer cr:lc){
                cr.setValide(true);
                cs.updateConsommer(cr);
            }
        }
        ServiceVendu sv = cs.getServiceVendu(cc);
        return Response.ok(sv).build();
    }
    
    
    
    @GET
    @Produces("application/json")
    @Path("consumer/promotion/clients/{phone}")
    public List<Promotion> searchForEligibility(@PathParam("phone") String phone){
         Client clt=cltsvc.getClientByPhone(phone);
            if(clt==null){
                String id=String.valueOf((int)(Math.random()*1000000));
                clt=new Client();
                clt.setId(id);
                clt.setPhone(phone);
                clt.setPrenom("CLT-"+id);
                cltsvc.createClient(clt);
            }
       
        List<Consommer> lc=cs.getClientPoints(clt.getId());
        List<Promotion> lp=new ArrayList<>();
        for(Consommer c:lc){
            Service s=svcs.getService(c.getConsommerPK().getIdService());
            Promotion p=new Promotion();
            p.setCode(c.getIdKiosq()+"-"+c.getConsommerPK().getId());
            p.setProduit(s.getNomService());
            p.setQuantite(c.getQuantite());
            p.setPhone(clt.getPhone());
            p.setDate(df.format(c.getDate()));
            lp.add(p);
        }
      return new PromotionBinder(lp) ;  
    }

    /**
     * Sub-resource locator method for {name}
     */
    @Path("uniq/uid-{uid}/svc-{service}/clt-{clt}")
    public ConsommerResource getConsommerResource(@PathParam("uid") int id, @PathParam("service") String svc, @PathParam("clt") String idClt) {
        return ConsommerResource.getInstance(cs, id, svc, idClt);
    }
}
