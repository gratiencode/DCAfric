/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.KiosqueService;

import entities.Kiosque;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.DELETE;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import jax_binding.QosDetailBinder;
import org.glassfish.jersey.media.sse.OutboundEvent;
import util.QosKDetails;


/**
 * REST Web Service
 *
 * @author PC
 */
public class KiosqResource {

    private String name;
    Kiosque kiosque;
    KiosqueService qs;
    
    SseBroadcaster sbroadcaster;
    
    

    /**
     * Creates a new instance of KiosqResource
     */
    private KiosqResource(KiosqueService qs,String name) {
        this.name = name;
        this.qs=qs;
        this.kiosque=qs.getKiosque(name);
        if(this.kiosque==null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
    }

    /**
     * Get instance of the KiosqResource
     */
    public static KiosqResource getInstance(KiosqueService qs,String name) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of KiosqResource class.
        return new KiosqResource(qs,name);
    }

    /**
     * Retrieves representation of an instance of jax_rs.KiosqResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("mykiosque")
    public Response getJsonQos() {
       return Response.ok(this.kiosque).build();
    }

    /**
     * PUT method for updating or creating an instance of KiosqResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     * localhost:8087/services/providing/kiosques/uuid/{id}/mykiosque/location/{latitude}/{longitude}
     */
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("mykiosque/location/{latitude}/{longitude}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Response updateLocation(@PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
        if(this.kiosque==null)
            return Response.noContent().build();
        this.kiosque.setLatitude(latitude);
        this.kiosque.setLongitude(longitude);
       
        qs.updateKiosque(kiosque);
        
        return Response.ok(this.kiosque).build(); 
    }

    /**
     * DELETE method for resource KiosqResource
     */
    @DELETE
    public void delete() {
    }
}
