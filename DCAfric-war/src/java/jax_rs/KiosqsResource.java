/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.KiosqueService;
import entities.Kiosque;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.activation.MimeType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import jax_binding.KiosquerBinder;
import jax_binding.QosDetailBinder;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.server.ChunkedOutput;
import org.jboss.logging.Logger;
import util.QosKDetails;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/kiosques")
@Stateless
public class KiosqsResource {

//    @Context
//    private UriInfo context;
    @EJB
    KiosqueService qs;

    Executor exec = Executors.newSingleThreadExecutor();

   

   
    /**
     * Creates a new instance of KiosqsResource
     */
    public KiosqsResource() {
    }

    /**
     * Retrieves representation of an instance of jax_rs.KiosqsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("for/agent/{idagent}")
    public Response getKiosquefor(@PathParam("idagent") String idag) {
        return Response.ok(qs.getKiosqueForAgent(idag)).build();
    }

    @GET
    @Produces("application/json")
    @Path("locations/all")
    public List<Kiosque> getAllKiosques() {

        return new KiosquerBinder(qs.getAllKiosques());
    }

    @GET
    @Produces("application/json")
    @Path("locations/more/infos")
    public List<QosKDetails> getAllKiosqueAgent() {
        return new QosDetailBinder(qs.getAllKiosque());
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void getRealLocation(@Context SseEventSink eventSink, @Context Sse sse) {
        Random rnd = new Random();
        IntStream rndStream = IntStream.generate(() -> rnd.nextInt(90));
        List<Integer> lottery = rndStream.limit(10).boxed().collect(Collectors.toList());
        Logger.getLogger(this.getClass()).infov("lotto size " + lottery.size());
//        eventSink.close();
        ChunkedOutput outp = new EventOutput();

        exec.execute(() -> {
QosDetailBinder obj = new QosDetailBinder(qs.getAllKiosque());
//            lottery.forEach(value -> {
            while (true) {
                try {
                  //  QosDetailBinder obj = new QosDetailBinder(qs.getAllKiosque());
                    JsonArrayBuilder jsonAr = Json.createArrayBuilder();
                    for (QosKDetails det : obj) {
                        jsonAr.add(Json.createObjectBuilder()
                                .add("id", det.getIdKiosque())
                                .add("nom", det.getNomAgent())
                                .add("info", det.getInfo())
                                .add("date", det.getDateObtenir())
                                .add("latitude", det.getLatitude())
                                .add("longitude", det.getLongitude()));
                    }
                    JsonObject model = Json.createObjectBuilder()
                            .add("datas", jsonAr).build();
                    TimeUnit.SECONDS.sleep(5);
                    Logger.getLogger(this.getClass()).infov("sending lA value " + obj.get(0).getLatitude());
//                    s.send(sse.newEvent("data"));
                    OutboundSseEvent ose = sse.newEventBuilder()
                            .id(String.valueOf(rnd.nextInt()))
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .name("lottery")
                            .data(model).build();
//                    outp.write(ose);
                    Logger.getLogger(this.getClass()).infov("sending lA value " + ose.getName());
                    eventSink.send(ose);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                eventSink.close();
            }
//        });

        });
//        Runnable r = new Runnable() {
//      @Override
//      public void run() {
//        QosDetailBinder obj = new QosDetailBinder(qs.getAllKiosque());
//            JsonArrayBuilder jsonAr = Json.createArrayBuilder();
//            for (QosKDetails det : obj) {
//                jsonAr.add(Json.createObjectBuilder()
//                        .add("id", det.getIdKiosque())
//                        .add("nom", det.getNomAgent())
//                        .add("info", det.getInfo())
//                        .add("date", det.getDateObtenir())
//                        .add("latitude", det.getLatitude())
//                        .add("longitude", det.getLongitude()));
//            }
//            JsonObject model = Json.createObjectBuilder()
//                    .add("datas", "objet").build();
//        while (model != null) {
//          OutboundSseEvent event = ss.newEventBuilder().id(UUID.randomUUID().toString().replace("-", "").toLowerCase())
//                    .name("locationUpdate").mediaType(MediaType.APPLICATION_JSON_TYPE)
//                    .data(model).reconnectDelay(1000).comment("GeoMonitoring-location update").build();
//          eventSink.send(event);
//          try {
//            Thread.currentThread().sleep(1000);
//          } catch (InterruptedException ex) {
//            // ...
//          }
//        }  
//      }
//    };
//    new Thread(r).start();
//        try (SseEventSink ses = eventSink) {
//            ses.send(ss.newEvent("data"));
//            ses.send(ss.newEvent("brodLoc", "more data"));
//
//            
//            OutboundSseEvent obsse = ss.newEventBuilder().id(UUID.randomUUID().toString().replace("-", "").toLowerCase())
//                    .name("locationUpdate").mediaType(MediaType.APPLICATION_JSON_TYPE)
//                    .data(model).reconnectDelay(10000).comment("GeoMonitoring-location update").build();
//            ses.send(obsse);
//        }
    }

    @GET
    @Produces("application/json")
    @Path("locations/web/plus/d1fo")
    public Response webUsers() {
        QosDetailBinder obj = new QosDetailBinder(qs.getAllKiosque());
        JsonArrayBuilder jsonAr = Json.createArrayBuilder();
        for (QosKDetails det : obj) {
            jsonAr.add(Json.createObjectBuilder()
                    .add("id", det.getIdKiosque())
                    .add("nom", det.getNomAgent())
                    .add("info", det.getInfo())
                    .add("date", det.getDateObtenir())
                    .add("latitude", det.getLatitude())
                    .add("longitude", det.getLongitude()));
        }
        JsonObject model = Json.createObjectBuilder()
                .add("datas", jsonAr).build();
        return Response.ok(model).header("Access-Control-Allow-Origin", "*").build();
    }

    /**
     * Sub-resource locator method for {name}
     */
    @Path("uuid/{id}")
    public KiosqResource getKiosqResource(@PathParam("id") String name) {
        return KiosqResource.getInstance(qs, name); 
    }
}
