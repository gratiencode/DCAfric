/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.KiosqueService;
import cdi_impl.ProduitService;
import entities.CurentSolde;
import entities.CurentSoldePK;
import entities.Obtenir;
import entities.Recquisitionner;
import entities.RecquisitionnerPK;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jax_binding.RecquisitionBinder;
import stateless.CurentSoldeBean;
import stateless.ObtenirBeans;
import stateless.RecquisitionnerBeans;
import util.Constants;

/**
 * REST Web Service
 *
 * @author gratien
 */
@Path("/recharge")
@Stateless
public class RecquisitionsResource {

    @Context
    private UriInfo context;
    
    @EJB
    RecquisitionnerBeans reqB;
    
    @EJB
    ObtenirBeans ob;
    
    @EJB
    KiosqueService kiosq;
    
    @EJB
    CurentSoldeBean csb;
    
    Calendar cal;
    
    @PostConstruct
    public void init(){
        cal=Calendar.getInstance();
    }
    
    

    /**
     * Creates a new instance of RecquisitionsResource
     */
    public RecquisitionsResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.RecquisitionsResource
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/all")
    public List<Recquisitionner> getList() {
       cal.add(Calendar.MONTH, -1);
       return new RecquisitionBinder(reqB.paginate(cal.getTime(),Constants.Datetime.todayTime()));
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("save_point")
    public Response saveReq(@FormParam("comment") String commentaire,@FormParam("prix") double prix, @FormParam("id_produit") String id_PRODUIT,@FormParam("quant") double quant,@FormParam("id_kiosque") String kiosque) {
        RecquisitionnerPK rpk=new RecquisitionnerPK();
        rpk.setId(System.currentTimeMillis());
        rpk.setIdKiosque(kiosque);
        rpk.setIdProduit(id_PRODUIT);
        Recquisitionner req=new Recquisitionner();
        req.setCommentaire(commentaire);
        req.setDevise("CDF");
        req.setPrixUnit(prix);
        req.setQuant(quant);
        req.setRecquisitionnerPK(rpk);
        req.setDate(Constants.Datetime.todayTime());
        reqB.insertRecquisitionner(req);
        Obtenir obt=ob.getByKiosq(kiosque);
        String idAgent=obt.getObtenirPK().getIdAgent();
        CurentSolde cs=csb.findExact(idAgent, id_PRODUIT);
        if(cs==null){
            int id=(int)(Math.random()*100000);
            CurentSoldePK cspk=new CurentSoldePK();
            cspk.setAgentId(idAgent);
            cspk.setId(id);
            cspk.setProductId(id_PRODUIT);
            cs.setCurentSoldePK(cspk);
            cs.setSolde(quant); 
            csb.insertSolde(cs);
        }else{
            double solde=cs.getSolde()+quant;
            cs.setSolde(solde);
            csb.updateSolde(cs);
        }
        return Response.ok(req).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public RecquisitionResource getRecquisitionResource(@PathParam("id") String id) {
        return RecquisitionResource.getInstance(id);
    }
}
