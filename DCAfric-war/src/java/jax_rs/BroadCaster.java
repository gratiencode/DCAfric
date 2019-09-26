/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ClientService;
import cdi_impl.ConsommerService;
import cdi_impl.KiosqueService;
import cdi_impl.ParametreService;
import cdi_impl.VenteService;
import com.fazecast.jSerialComm.SerialPort;
import entities.Client;
import entities.Commande;
import entities.CommandePK;
import entities.Consommer;
import entities.CurentSolde;
import entities.CurentSoldePK;
import entities.Obtenir;
import entities.Parametre;
import entities.Vente;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import jax_binding.QosDetailBinder;
import org.jboss.logging.Logger;
import stateless.CurentSoldeBean;
import stateless.ObtenirBeans;
import util.Constants;
import util.GsmStringset;
import util.QosKDetails;
import util.AirtelSoldeChangeListener;
import util.OrangeSoldeChangeListener;
import util.USSDCommand;
import util.VodacomSoldeChangeListener;

/**
 *
 * @author PC
 */
@Path("/realtime")
@Singleton
public class BroadCaster {

    @Context
    private Sse sse;
    @EJB
    KiosqueService qs;
    @EJB
    VenteService vs;
    @EJB
    ParametreService ps;

    @EJB
    ClientService cs;

    @EJB
    CurentSoldeBean csb;

    @EJB
    ObtenirBeans ob;

    String _AGENT, _PRODUIT;

    List<String> initializedUsb;
    List<Client> clientsNouveau;
    List<Client> allClients;
    HashMap<String, Double> soldes;
    Parametre bonus;
    private String PIN_AIRTEL;
    private String PIN_ORANGE;

    private String PIN_VODACOM;
    private static double CURENT_AIRTEL_SOLDE = -4;
    private static double CURENT_ORANGE_SOLDE;
    private static double CURENT_VODACOM_SOLDE;
    SoldeTelecomManager am;

    private volatile SseBroadcaster sseBroadcaster;

    @PostConstruct
    public void init() {
        this.sseBroadcaster = sse.newBroadcaster();
        initializedUsb = new ArrayList<>();
        clientsNouveau = new ArrayList<>();
        soldes = new HashMap<>();
        allClients = cs.getClients();
        bonus = ps.getParametre("CRITERE-BONUS");
        PIN_AIRTEL = ps.getParametre("PIN_AIRTEL").getValeur();
        PIN_ORANGE = ps.getParametre("PIN_ORANGE").getValeur();
        PIN_VODACOM = ps.getParametre("PIN_VODACOM").getValeur();
        soldes.put(Constants.AIRTEL, findInstantSolde("458"));
        soldes.put("MEGA", findInstantSolde("489"));
        soldes.put(Constants.ORANGE, findInstantSolde("733"));
        soldes.put(Constants.VODACOM, findInstantSolde("734"));
        am = new SoldeTelecomManager();
        CURENT_AIRTEL_SOLDE = soldeUniteAirtel();

    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("souscription")
    public void register(@Context SseEventSink eventSink) {
        eventSink.send(sse.newEvent("Bienvenue!"));
        sseBroadcaster.register(eventSink);

    }

    @POST
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("infos")
    public void broadcast(@FormParam("id") String id, @FormParam("latitude") double latitude, @FormParam("longitude") double longitude) {
        QosDetailBinder obj = new QosDetailBinder();
        QosKDetails qos = qs.getKiosqueDetail(id);
        if (qos == null) {
            return;
        }
        Logger.getLogger(this.getClass()).info("Comparaison (A) = (B) => " + qos.getIdKiosque() + " = " + id);
        qos.setLatitude(latitude);
        qos.setLongitude(longitude);
        int index = obj.indexOf(qos);
        if (index == -1) {
            obj.add(qos);
        } else {
            obj.set(index, qos);
        }
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
        OutboundSseEvent obsse = sse.newEventBuilder().id(UUID.randomUUID().toString().replace("-", "").toLowerCase())
                .name("locationUpdate").mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(model).reconnectDelay(10000).comment("GeoMonitoring-location update").build();
        sseBroadcaster.broadcast(obsse);

    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("executer/read/message/{operateur}")
    public Response runShowMessages(@PathParam("operateur") String operateur) {
        Logger.getLogger(this.getClass().getName()).info("Verifiez message " + operateur);
        SerialPort sports[] = SerialPort.getCommPorts();
        String reso = operateur;
        for (SerialPort spi : sports) {
            String s = spi.getDescriptivePortName();
            String ss = spi.getSystemPortName();
            if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {
                spi.setComPortParameters(9600, 8, 1, 0);
                Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                String result = runAT(spi, "AT+CFUN=1", 1000);
                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                if (result.contains("OK")) {

                    runAT(spi, "ATE0", 1000);
                    String rst = runAT(spi, "AT+COPS?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
//                    runAT(spi, "at+cmee=2", 1000);
                    runAT(spi, "AT+CMGF=1", 3000);

                    if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN")) && reso.toUpperCase().contains(Constants.AIRTEL)) {

                        String cmd = runAT(spi, "AT+CMGL=\"ALL\"", 2000);

                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(operateur);
                        ussd.setResult(cmd);
                        return Response.ok(ussd).build();

                    } else if ((rst.toUpperCase().contains("63089") || rst.toUpperCase().contains("63086") || rst.toUpperCase().contains(Constants.ORANGE) || rst.toUpperCase().contains("CCT") || rst.toUpperCase().contains("TIGO")) && reso.toUpperCase().contains(Constants.ORANGE)) {

//                    executeAT(spi, "at+cusd=1");
//                        Logger.getLogger(this.getClass().getName()).info("caracter set" + b);
                        String cmd = runAT(spi, "AT+CMGL=\"ALL\"", 2000);

                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(operateur);
                        ussd.setResult(cmd);
                        return Response.ok(ussd).build();
                    } else if ((rst.contains("63") || rst.contains("63") || rst.toUpperCase().contains(Constants.VODACOM)) && reso.toUpperCase().contains(Constants.VODACOM)) {
                        String cmd = runAT(spi, "AT+CMGL=\"ALL\"", 1000);

                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(operateur);
                        ussd.setResult(cmd);
                        return Response.ok(ussd).build();

                    }

                }
            }
        }
        return Response.noContent().build();
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("executer/ussd/solde/telekom/p/{operateur}/{produit}")
    public Response runUssdSolde(@PathParam("operateur") String operateur, @PathParam("produit") String produit) {
        Logger.getLogger(this.getClass().getName()).info("U-SOLDE!!" + operateur);
        SerialPort sports[] = SerialPort.getCommPorts();
        String reso = operateur;
        label:
        for (SerialPort spi : sports) {
            String s = spi.getDescriptivePortName();
            String ss = spi.getSystemPortName();
            String result = "Init";
            spi.setComPortParameters(9600, 8, 1, 0);
            if (!initializedUsb.contains(ss)) {
                result = runAT(spi, "at+cfun=1", 1000);
                runAT(spi, "ATE0", 1000);
                runAT(spi, "at+cmee=2", 1000);
                runAT(spi, "at+cscs=\"ira\"", 1000);
                runAT(spi, "at+cusd=1", 1000);
                initializedUsb.add(ss);
            }
            if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {

                Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                if (result.contains("OK") || result.contains("Init")) {
                    String rst = runAT(spi, "AT+COPS?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
                    String prod = produit.toUpperCase();
                    if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN")) && reso.toUpperCase().contains(Constants.AIRTEL)) {
                        if (prod.equals(Constants.UNITE)) {

                            String cmd = runAT(spi, "AT+CUSD=1,\"*1000#\",15", 3000);
                            Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                            cmd = runAT(spi, "AT+CUSD=1,\"4\",15", 3000);
                            Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                            String cmd1 = runAT(spi, "AT+CUSD=1,\"" + PIN_AIRTEL + "\",15", 3000);
                            Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                            USSDCommand ussd = new USSDCommand();
                            ussd.setUssdCode("AIR002A00310030003000300023");
                            ussd.setOperator(operateur);
                            ussd.setResult(cmd1.replaceAll("\\r\\n|\\r|\\n", ""));
                            String ssss = ussd.getResult();
                            if (!ssss.contains(",") || !ssss.contains("CO:eTopUP")) {
                                continue label;
                            }
                            return Response.ok(ussd).build();
                        } else if (prod.equals(Constants.MEGA)) {

                            String cmd0 = runAT(spi, "AT+CUSD=1,\"*4425#\",15", 3000);
                            String cmd0o = runAT(spi, "AT+CUSD=1,\"8\",15", 1000);
                            Logger.getLogger(this.getClass().getName()).info("TE mega sold > " + cmd0);
//                            Logger.getLogger(this.getClass().getName()).info("TE solde > " + cmd0);
                            USSDCommand ussd0 = new USSDCommand();
                            ussd0.setUssdCode("*425#");
                            ussd0.setOperator(operateur);
                            ussd0.setResult(cmd0o.replaceAll("\\r\\n|\\r|\\n", ""));
                            String ssss = ussd0.getResult();
                            if (!ssss.contains(",") || !ssss.contains("MB") || !ssss.contains("aucun forfait")) {
                                continue label;
                            }
                            return Response.ok(ussd0).build();
                        }

                    } else if ((rst.toUpperCase().contains("63089") || rst.toUpperCase().contains("63086") || rst.toUpperCase().contains(Constants.ORANGE) || rst.toUpperCase().contains("CCT") || rst.toUpperCase().contains("TIGO")) && reso.toUpperCase().contains(Constants.ORANGE)) {

                        String cmd = runAT(spi, "at+cusd=1,\"*135*" + PIN_ORANGE + "#\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("Rep " + cmd);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("OR002A0031003300350023");
                        ussd.setOperator(operateur);
                        ussd.setResult(cmd.replaceAll("\\r\\n|\\r|\\n", ""));
                        return Response.ok(ussd).build();
                    } else if ((rst.contains("63") || rst.contains("63") || rst.toUpperCase().contains(Constants.VODACOM)) && reso.toUpperCase().contains(Constants.VODACOM)) {
                        String codeVodac = ps.getParametre("solde-vodacom").getValeur();
//                    executeAT(spi, "at+cusd=1");

                        String cmd = runAT(spi, "at+cusd=1,\"" + codeVodac + "\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("Rep v " + cmd);
                        cmd = runAT(spi, "at+cusd=1,\"1\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("Rep v " + cmd);
                        String u = cmd.replaceAll("\\r\\n|\\r|\\n", "");
                        USSDCommand ussd = new USSDCommand();
                        ussd.setOperator(operateur);
                        ussd.setResult(u);
                        ussd.setUssdCode("VOD002A0031003100310031002A00300037002A0x00023");
                        String ssss = ussd.getResult();
                        if (!ssss.contains(",")) {
                            continue label;
                        }
                        return Response.ok(ussd).build();
                    }

                }
            }
        }
        return Response.noContent().build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("executer/u/telekom/p")
    public Response runUssd(USSDCommand ucmd) {
        SerialPort sports[] = SerialPort.getCommPorts();
        Logger.getLogger(this.getClass().getName()).info("tailles port " + sports.length);
        List<Vente> ventes = ucmd.getVentes();
        String s;
        String code = ucmd.getUssdCode();
        String product = ucmd.getProduit().toUpperCase().split("/")[0];
        String reso = ucmd.getOperator().toUpperCase().trim();
        Commande com = ucmd.getCommande();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        d1.format(df);
        Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        com.setDate(d);
        String codit = GsmStringset.gsmToStringDecoded(code);
        String kiosq = com.getCommandePK().getIdKiosque();
        Obtenir obt = ob.getByKiosq(kiosq);
        _AGENT = obt.getObtenirPK().getIdAgent();
        _PRODUIT = ucmd.getVentes().get(0).getVentePK().getIdProduit();

        String phone = ucmd.getProduit().toUpperCase().split("/")[1];

        for (SerialPort spi : sports) {
            s = spi.getDescriptivePortName();
            String a = spi.getSystemPortName();
            product = product.toUpperCase();
            spi.setComPortParameters(9600, 8, 1, 0);
            String result = "Init";
            if (!initializedUsb.contains(a)) {
                result = executeAT(spi, "at+cfun=1", 1000);
                executeAT(spi, "ATE0", 1000);
                executeAT(spi, "at+cmee=2", 1000);
                executeAT(spi, "at+cscs=\"ira\"", 1000);
                executeAT(spi, "at+cusd=1", 1000);
                initializedUsb.add(a);
            }
            if (result.contains("OK") || result.contains("Init")) {

                if (a.contains("ttyUSB0") || a.contains("USB5") || a.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {
                    String rst = executeAT(spi, "at+cops?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);

                    if (((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN")) && reso.toUpperCase().contains(Constants.AIRTEL))) {
                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#ai> " + code);

                        String cmd;
                        switch (product) {
                            case Constants.UNITE:
                                String quant = GsmStringset.gsmToStringDecoded(code.split("002A")[3]);
                                double q = Double.parseDouble(quant);
                                double dispo = findInstantSolde("458");

                                if (q <= dispo) {

                                    String c = "at+cusd=1,\"" + GsmStringset.gsmToStringDecoded(code) + "\",15";
                                    cmd = executeAT(spi, c, Constants.TIMEOUT_USSD);
                                    String sss = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                    Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss);
                                    ucmd.setUssdCode(code);
                                    ucmd.setResult(sss);
                                    am.setOnSoldeAirtelChangeListener(new AirtelSoldeChangeListener() {
                                        @Override
                                        public void onAirtelSoldeChange(double solde, String result) {
                                            CommandePK cpk = com.getCommandePK();
                                            cpk.setIdClient(phone);
                                            com.setCommandePK(cpk);
                                            if (CURENT_AIRTEL_SOLDE < 0) {
                                                CURENT_AIRTEL_SOLDE = solde;
                                                //enregistrer
                                                vs.createCommande(com);
                                                ventes.stream().forEach((ve) -> {
                                                    ve.setDate(d);
                                                    vs.createVente(ve);
                                                });
                                                f(_AGENT, _PRODUIT, q);
                                                return;
                                            }
                                            if (solde < CURENT_AIRTEL_SOLDE) {
                                                //insertion
                                                vs.createCommande(com);
                                                ventes.stream().forEach((ve) -> {
                                                    ve.setDate(d);
                                                    vs.createVente(ve);
                                                });
                                                f(_AGENT, _PRODUIT, q);
                                            } else if (solde == CURENT_AIRTEL_SOLDE) {
                                                ucmd.setResult("OK-ERROR Credit (Unite) non envoye du soit au solde recquis insuffisant ou erreur reseau cellulaire reesayer SVP!");
                                            }
                                            Logger.getLogger(this.getClass().getName()).info(" REsultat ***************SOLDE AIRTEL******* " + solde + " rst " + result);
                                        }
                                    });
                                    am.notifySoldeAirtelChange();
                                } else {
                                    ucmd.setUssdCode(code);
                                    ucmd.setResult("OK-ERROR Solde Insffisant il ne reste que " + dispo + " U disponible");
                                }

                                break;
                            case Constants.MEGA:
                                String quantity = GsmStringset.gsmToStringDecoded(code.split("002A")[3]);
                                double qu = Double.parseDouble(quantity);
                                double dispon = findInstantSolde("489");

                                if (qu <= dispon) {
                                    String cl = "at+cusd=1,\"" + GsmStringset.gsmToStringDecoded(code) + "\",15";
                                    cmd = executeAT(spi, cl, Constants.TIMEOUT_USSD);
                                    String sss1 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                    Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss1);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> " + code);
                                    ucmd.setUssdCode(code);
                                    ucmd.setResult(sss1);
                                    f(_AGENT, _PRODUIT, qu);
                                } else {
                                    ucmd.setUssdCode(code);
                                    ucmd.setResult("OK-ERROR Solde Insffisant il ne reste que " + dispon + " MB disponible");
                                }
                                break;
                        }
                    } else if (((rst.contains("63089") || rst.contains("63086") || rst.toUpperCase().contains(Constants.ORANGE) || rst.contains("CCT") || rst.toUpperCase().contains("TIGO")) && reso.toUpperCase().contains(Constants.ORANGE))) {
                        Logger.getLogger(this.getClass().getName()).info(" GSMSH@or> " + GsmStringset.stringToGsmEncoded(code));
                        String quantity = GsmStringset.gsmToStringDecoded(code.split("002A")[3]);
                        double qu = Double.parseDouble(quantity);
                        double dispon = findInstantSolde("733");
                        if (qu <= dispon) {
                            String cmd;
                            switch (product) {
                                case Constants.UNITE:

                                    String decoded = GsmStringset.gsmToStringDecoded(code).replace("P", PIN_ORANGE);
                                    cmd = executeAT(spi, "at+cusd=1,\"" + decoded + "\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE OR1" + code + "\nresult : " + cmd + " decoded " + decoded);
                                    cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE OR2" + code + "\nresult : " + cmd + " decoded " + decoded);
                                    String sss2 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                    Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss2);

                                    ucmd.setUssdCode(code);
                                    ucmd.setResult(sss2);
                                    f(_AGENT, _PRODUIT, qu);
                                    //insertion bd puis return result to client
                                    break;
                                case Constants.MEGA:

                                    String tarif = ucmd.getResult();
                                    if (tarif.contains("Home Box")) {

                                        String ussd[] = (GsmStringset.gsmToStringDecoded(code).replace("P", PIN_ORANGE)).split("/");
                                        String ss = GsmStringset.gsmToStringDecoded("002A0031003300310023");
                                        cmd = executeAT(spi, "at+cusd=1,\"" + ss + "\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"" + ussd[1] + "\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        switch (ussd[2]) {
                                            case "8":
                                                cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE 8 " + code + "\nresult : " + cmd);
                                                break;
                                            case "30":
                                                cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE 30 " + code + "\nresult : " + cmd);
                                                break;
                                            case "130":
                                                cmd = executeAT(spi, "at+cusd=1,\"3\",15", Constants.TIMEOUT_USSD);
                                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE 130 " + code + "\nresult : " + cmd);
                                                break;
                                            case "300":
                                                cmd = executeAT(spi, "at+cusd=1,\"4\",15", Constants.TIMEOUT_USSD);
                                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE 300 " + code + "\nresult : " + cmd);
                                                break;
                                        }
                                        cmd = executeAT(spi, "at+cusd=1,\"" + ussd[3] + "\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        String sss3 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                        Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss3);

                                        ucmd.setUssdCode(code);
                                        ucmd.setResult(sss3);

                                    } else if (tarif.contains("Bundle")) {
                                        //"*134*" + phone.getText().toString() + "*" + qte.getText().toString() + "*" + pswd.getText().toString().toString() + "#"

                                        String ussd[] = (GsmStringset.gsmToStringDecoded(code).replace("P", PIN_ORANGE)).split("/");
                                        String ss = GsmStringset.gsmToStringDecoded("002A0031003300310023");
                                        cmd = executeAT(spi, "at+cusd=1,\"" + ss + "\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"" + ussd[1] + "\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        String quanti = ussd[2];
                                        switch (quanti) {
                                            case "50":
                                                cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE 50" + code + "\nresult : " + cmd);
                                                break;
                                            case "100":
                                                cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE 100" + code + "\nresult : " + cmd);

                                                break;
                                        }
                                        cmd = executeAT(spi, "at+cusd=1,\"" + ussd[3] + "\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                        String sss4 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                        Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss4);

                                        ucmd.setUssdCode(code);
                                        ucmd.setResult(sss4);
                                        updateMegaSolde(_AGENT, _PRODUIT, Double.parseDouble(quanti));

                                    }
                            }
                        } else {
                            ucmd.setUssdCode(code);
                            ucmd.setResult("OK-ERROR Solde Insffisant il ne reste que " + dispon + " MB disponible");
                        }
                        break;

                    } else if (((rst.contains("63001") || rst.toUpperCase().contains(Constants.VODACOM)) && reso.toUpperCase().contains(Constants.VODACOM))) {
                        Logger.getLogger(this.getClass().getName()).info(" GSMSH$vo> " + code);
                        String quantity = GsmStringset.gsmToStringDecoded(code.split("002A")[3]);
                        double qu = Double.parseDouble(quantity);
                        double dispon = findInstantSolde("734");
                        if (qu <= dispon) {
                            String cmd;
                            switch (product) {
                                case Constants.UNITE:

                                    cmd = executeAT(spi, "at+cusd=1,\"" + GsmStringset.gsmToStringDecoded(code) + "\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH$> CODE " + code + "\nresult : " + cmd);
                                    cmd = executeAT(spi, "at+cusd=1,\"1\",15", (Constants.TIMEOUT_USSD));
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH$> CODE " + code + "\nresult : " + cmd);
                                    String sss5 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                    Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss5);
                                    ucmd.setUssdCode(code);
                                    ucmd.setResult(sss5);
                                    f(_AGENT, _PRODUIT, qu);
                                    break;
                                case Constants.MEGA:

                                    String ussd[] = (GsmStringset.gsmToStringDecoded(code)).split("/");
                                    String ss = GsmStringset.gsmToStringDecoded("002A00310031003100370023");
                                    executeAT(spi, "at+cusd=1,\"" + ss + "\",15", 600);
                                    cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    String quantite = ussd[1];
                                    if (quantite.equals("50")) {
                                        cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    } else if (quantite.equals("100")) {
                                        cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    } else if (quantite.equals("1024")) {
                                        cmd = executeAT(spi, "at+cusd=1,\"3\",15", Constants.TIMEOUT_USSD);
                                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    }
                                    cmd = executeAT(spi, "at+cusd=1,\"" + ussd[2] + "\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    cmd = executeAT(spi, "at+cusd=1,\"" + ussd[3] + "\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
//                                
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                    String sss6 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                    Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss6);
                                    ucmd.setUssdCode(code);
                                    ucmd.setResult(sss6);
                                    updateMegaSolde(_AGENT, _PRODUIT, Double.parseDouble(quantite));
                                    break;
                            }
                        } else {
                            ucmd.setUssdCode(code);
                            ucmd.setResult("OK-ERROR Solde Insffisant il ne reste que " + dispon + " MB disponible");
                        }
                    }
                }
            }
        }

        ucmd.setOperator("EXPRESSOL      Ref/Vente :" + com.getCommandePK().getReference() + " Vous avez acheté le(s) " + com.getComment() + " de " + ucmd.getVentes().get(0).getMantant() + ucmd.getVentes().get(0).getDevise() + " au kiosque : " + com.getCommandePK().getIdKiosque() + " de Districom Afric. Apres " + bonus.getValeur() + " achats vous serez eligible au bonus. Conservez ce message SVP!");

        CommandePK cpk = com.getCommandePK();
        cpk.setIdClient(phone);
        com.setCommandePK(cpk);
//        
        String rf = ucmd.getResult().toUpperCase().replaceAll("\\s|\\r\\n|\\r|\\n", "");
        if (rf.contains("OK") && rf.contains("ERROR")) {
            return Response.ok(ucmd).build();
        }
        if (rf.contains("OK") && (rf.contains("essayer plus") || rf.contains("problem") || rf.contains("invalid"))) {
            String r = "Reseau cellulaire temporairement indisponible!\n Message cellulaire : " + rf;
            ucmd.setResult(r);
            return Response.ok(ucmd).build();
        }
        if (!reso.equals(Constants.AIRTEL) && (rf.contains("+CUSD:0") || rf.contains("+CUSD:2"))) {
            vs.createCommande(com);
            ventes.stream().forEach((ve) -> {
                ve.setDate(d);
                vs.createVente(ve);
            });
        }
//        else {
//            String r = "OK-ERROR Erreur temporaire veuillez reessayer svp! ";
//            ucmd.setResult(r);
//        }
        if (isClientNotExist(phone)) {
            String id = String.valueOf((int) (Math.random() * 1000000));
            Client client = new Client();
            client.setId(phone);
            client.setPhone(phone);
            client.setPrenom("CLT-" + id);
            if (!clientsNouveau.contains(client)) {
                clientsNouveau.add(client);
            }
        }
        return Response.ok(ucmd).build();
    }

    private void updateMegaSolde(String id_agent, String id_prod, double quant) {
        double radical = 0;
        if (id_prod.equals("736")) {
            if (quant == 50) {
                radical = 28;
            } else if (quant == 100) {
                radical = 54;
            }
            f(id_agent, id_prod, radical);
        } else if (id_prod.equals("735")) {
            if (quant == 50) {
                radical = 30;
            } else if (quant == 100) {
                radical = 60;
            } else if (quant == 150 || quant == 1024) {
                radical = 100;
            }
            f(id_agent, id_prod, radical);
        }
    }

    private void f(String id_agent, String id_prod, double radical) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateSmartSolde(id_agent, id_prod, radical);
            }
        }).start();
    }

    private void updateSmartSolde(String id_agent, String id_pord, double radical) {
        CurentSolde cs = csb.findExact(id_agent, id_pord);
        if (cs != null) {
            double balance = cs.getSolde();
            cs.setSolde(balance - radical);
            csb.updateSolde(cs);
        }
    }

    @GET
    @Produces("text/plain")
    @Path("executer/refresh/save/client")
    public Response saveClients() {
        Iterator it = clientsNouveau.iterator();
        while (it.hasNext()) {
            Client c = (Client) it.next();
            if (!allClients.contains(c)) {
                createNewClient(c.getPhone());
                allClients.add(c);
                it.remove();
            }

        }
        return Response.ok("Merci! Courrage").build();
    }

    private Client createNewClient(String phone) {
        String id = String.valueOf((int) (Math.random() * 1000000));
        Client client = new Client();
        client.setId(phone);
        client.setPhone(phone);
        client.setPrenom("CLT-" + id);
        cs.createClient(client);

        return client;
    }

    private Client searchClient(List<Client> lClient, String phone) {
        for (Client c : lClient) {
            if (c.getPhone().contains(phone) || phone.contains(c.getPhone())) {
                return c;
            }
        }
        return null;
    }

    private boolean isClientNotExist(String phone) {
        return searchClient(allClients, phone) == null;
    }

    private double findInstantSolde(String idProduit) {
        double solde = (vs.getSommeQuantByProduitRecquit(idProduit) - vs.getSommeQuantVenteVendu(idProduit));
        return (solde < 0 ? 0 : solde);
    }

    private double soldeUniteAirtel() {
        String ssss;
        SerialPort sports[] = SerialPort.getCommPorts();
        double solde = -1;
        label:
        for (SerialPort spi : sports) {
            String s = spi.getDescriptivePortName();
            String ss = spi.getSystemPortName();
            String result = "Init";
            spi.setComPortParameters(9600, 8, 1, 0);
            if (!initializedUsb.contains(ss)) {
                result = runAT(spi, "at+cfun=1", 1000);
                runAT(spi, "ATE0", 1000);
                runAT(spi, "at+cmee=2", 1000);
                runAT(spi, "at+cscs=\"ira\"", 1000);
                runAT(spi, "at+cusd=1", 1000);
                initializedUsb.add(ss);
            }

            if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {

                Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                if (result.contains("OK") || result.contains("Init")) {
                    String rst = runAT(spi, "AT+COPS?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
//                                   String prod = produit.toUpperCase();
                    if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN"))) {
                        String cmd = runAT(spi, "AT+CUSD=1,\"*1000#\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        cmd = runAT(spi, "AT+CUSD=1,\"4\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        String cmd1 = runAT(spi, "AT+CUSD=1,\"" + PIN_AIRTEL + "\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(Constants.AIRTEL);
                        ussd.setResult(cmd1.replaceAll("\\r\\n|\\r|\\n|\\s", ""));
                        ssss = ussd.getResult();
                        if (!ssss.contains(",") || !ssss.contains("CO:eTopUP")) {
                            continue label;
                        }
                        String resultat[] = ssss.split(",");
                        String rep = resultat[1];
                        Logger.getLogger(this.getClass().getName()).info("TE REP >>xxxxxxxxxx " + rep);
                        rep = rep.replaceAll("\\s", "");
                        Logger.getLogger(this.getClass().getName()).info("TE REP NOW >< >< <> " + rep);
                        int end = rep.lastIndexOf("\"");
                        int start = rep.lastIndexOf(":");
                        String sl = rep.substring(start + 1, end);
                        Logger.getLogger(this.getClass().getName()).info("Solde " + sl);
                        try {
                            sl = sl.replace("\\s", "");
                            solde = Double.parseDouble(sl);
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass().getName()).info("Solde " + e.getMessage());
                        }
                    }
                }
            }

        }
        return solde;
    }

    private double soldeMegaAirtel() {
        SerialPort sports[] = SerialPort.getCommPorts();
        double solde = -1;
        for (SerialPort spi : sports) {
            String s = spi.getDescriptivePortName();
            String ss = spi.getSystemPortName();
            String result = "Init";
            spi.setComPortParameters(9600, 8, 1, 0);
            if (!initializedUsb.contains(ss)) {
                result = runAT(spi, "at+cfun=1", 1000);
                runAT(spi, "ATE0", 1000);
                runAT(spi, "at+cmee=2", 1000);
                runAT(spi, "at+cscs=\"ira\"", 1000);
                runAT(spi, "at+cusd=1", 1000);
                initializedUsb.add(ss);
            }

            if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {

                Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                if (result.contains("OK") || result.contains("Init")) {
                    String rst = runAT(spi, "AT+COPS?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
//                    String prod = produit.toUpperCase();
                    if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN"))) {

                        String cmd0 = runAT(spi, "AT+CUSD=1,\"*4425#\",15", 3000);
                        String cmd1 = runAT(spi, "AT+CUSD=1,\"8\",15", 1000);
                        Logger.getLogger(this.getClass().getName()).info("TE mega sold > " + cmd0);
//                            Logger.getLogger(this.getClass().getName()).info("TE solde > " + cmd0);

                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(Constants.AIRTEL);
                        ussd.setResult(cmd1.replaceAll("\\r\\n|\\r|\\n", ""));
                        String ssss = ussd.getResult();
                        String resultat[] = ssss.split(",");
                        String rep = resultat[1];
                        if (rep.contains("vous n'avez aucun forfait active")) {
                            return 0;
                        }
                        String reponse = rep.split(" ")[2];
//                        int end=reponse.lastIndexOf("\"");
                        int start = reponse.lastIndexOf("MB");
                        String sl = reponse.substring(0, start);
                        Logger.getLogger(this.getClass().getName()).info("Solde " + sl);
                        try {
//                            sl=sl.replace("\\s", "");
                            solde = Double.parseDouble(sl);
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass().getName()).info("message d'erreur " + e.getMessage());
                        }
                    }
                }
            }

        }
        return solde;
    }

    private double soldeUniteOrange() {
        SerialPort sports[] = SerialPort.getCommPorts();
        double solde = -1;
        for (SerialPort spi : sports) {
            String s = spi.getDescriptivePortName();
            String ss = spi.getSystemPortName();
            String result = "Init";
            spi.setComPortParameters(9600, 8, 1, 0);
            if (!initializedUsb.contains(ss)) {
                result = runAT(spi, "at+cfun=1", 1000);
                runAT(spi, "ATE0", 1000);
                runAT(spi, "at+cmee=2", 1000);
                runAT(spi, "at+cscs=\"ira\"", 1000);
                runAT(spi, "at+cusd=1", 1000);
                initializedUsb.add(ss);
            }

            if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {

                Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                if (result.contains("OK") || result.contains("Init")) {
                    String rst = runAT(spi, "AT+COPS?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
//                    String prod = produit.toUpperCase();
                    if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN"))) {
                        String cmd = runAT(spi, "AT+CUSD=1,\"*1000#\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        cmd = runAT(spi, "AT+CUSD=1,\"4\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        String cmd1 = runAT(spi, "AT+CUSD=1,\"" + PIN_AIRTEL + "\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(Constants.AIRTEL);
                        ussd.setResult(cmd1.replaceAll("\\r\\n|\\r|\\n", ""));
                        String ssss = ussd.getResult();
                        String resultat[] = ssss.split(",");
                        String rep = resultat[1];
                        String reponse = rep.split(" ")[5];
                        int end = reponse.lastIndexOf("\"");
                        int start = reponse.lastIndexOf(":");
                        String sl = reponse.substring(start, end);
                        Logger.getLogger(this.getClass().getName()).info("Solde " + sl);
                        try {
                            sl = sl.replace("\\s", "");
                            solde = Double.parseDouble(sl);
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass().getName()).info("Solde " + e.getMessage());
                        }
                    }
                }
            }

        }
        return solde;
    }

    private double soldeUniteVodacom() {
        SerialPort sports[] = SerialPort.getCommPorts();
        double solde = -1;
        for (SerialPort spi : sports) {
            String s = spi.getDescriptivePortName();
            String ss = spi.getSystemPortName();
            String result = "Init";
            spi.setComPortParameters(9600, 8, 1, 0);
            if (!initializedUsb.contains(ss)) {
                result = runAT(spi, "at+cfun=1", 1000);
                runAT(spi, "ATE0", 1000);
                runAT(spi, "at+cmee=2", 1000);
                runAT(spi, "at+cscs=\"ira\"", 1000);
                runAT(spi, "at+cusd=1", 1000);
                initializedUsb.add(ss);
            }

            if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {

                Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                if (result.contains("OK") || result.contains("Init")) {
                    String rst = runAT(spi, "AT+COPS?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
//                    String prod = produit.toUpperCase();
                    if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.VODACOM) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN"))) {
                        String codeVodac = ps.getParametre("solde-vodacom").getValeur();
//                    executeAT(spi, "at+cusd=1");

                        String cmd1 = runAT(spi, "at+cusd=1,\"" + codeVodac + "\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("Rep v " + cmd1);
                        cmd1 = runAT(spi, "at+cusd=1,\"1\",15", 3000);
                        Logger.getLogger(this.getClass().getName()).info("Rep v " + cmd1);

                        Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd1);
                        USSDCommand ussd = new USSDCommand();
                        ussd.setUssdCode("AIR002A00310030003000300023");
                        ussd.setOperator(Constants.VODACOM);
                        ussd.setResult(cmd1.replaceAll("\\r\\n|\\r|\\n", ""));
                        String ssss = ussd.getResult();
                        String resultat[] = ssss.split(",");
                        String rep = resultat[1];
                        String reponse = rep.split(" ")[5];
                        int end = reponse.lastIndexOf("\"");
                        int start = reponse.lastIndexOf(":");
                        String sl = reponse.substring(start, end);
                        Logger.getLogger(this.getClass().getName()).info("Solde " + sl);
                        try {
                            sl = sl.replace("\\s", "");
                            solde = Double.parseDouble(sl);
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass().getName()).info("Solde " + e.getMessage());
                        }
                    }
                }
            }

        }
        return solde;
    }

    private String executeAT(SerialPort spi, String code, int timeout) {
        spi.openPort();
        try {
            OutputStream os = spi.getOutputStream();
            if (os == null) {
                return "ERROR NUL";
            }
            os.write((code + "\r\n").getBytes(), 0, (code + "\r\n").getBytes().length);
            spi.getOutputStream().flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BroadCaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException ex) {
            Logger.getLogger(this.getClass().getName()).info(ex.getMessage());
        }
        int taille = spi.bytesAvailable();
        if (taille < 0) {
            return "ERROR -1";
        }
        byte[] newData0 = new byte[taille];
        int numRead0 = spi.readBytes(newData0, newData0.length);
        if (numRead0 < 0) {
            return "ERROR -1";
        }
        String rst = new String(newData0, 0, numRead0);
        spi.closePort();
        return rst;
    }

    private String runAT(SerialPort spi, String code, int timewait) {
        spi.openPort();
        try {
            OutputStream os = spi.getOutputStream();
            if (os == null) {
                return "ERROR NUL";
            }
            os.write((code + "\r\n").getBytes(), 0, (code + "\r\n").getBytes().length);
            spi.getOutputStream().flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BroadCaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Thread.sleep(timewait);
        } catch (InterruptedException ex) {
            Logger.getLogger(this.getClass().getName()).info(ex.getMessage());
        }
        int taille = spi.bytesAvailable();
        if (taille < 0) {
            return "ERROR -1";
        }
        byte[] newData0 = new byte[taille];
        int numRead0 = spi.readBytes(newData0, newData0.length);
        if (numRead0 < 0) {
            return "ERROR -1";
        }
        String rst = new String(newData0, 0, numRead0);
        spi.closePort();
        return rst;
    }

    @PreDestroy
    public void destroyBean() {
        Iterator it = clientsNouveau.iterator();
        while (it.hasNext()) {
            Client c = (Client) it.next();
            if (!allClients.contains(c)) {
                createNewClient(c.getPhone());
                allClients.add(c);
                it.remove();
            }

        }
    }

    public class SoldeTelecomManager {

        String ssss;
        AirtelSoldeChangeListener listenner;
        OrangeSoldeChangeListener olistener;
        VodacomSoldeChangeListener vlistener;

        public void notifySoldeAirtelChange() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SerialPort sports[] = SerialPort.getCommPorts();
                    double solde = -1;
                    label:
                    for (SerialPort spi : sports) {
                        String s = spi.getDescriptivePortName();
                        String ss = spi.getSystemPortName();
                        String result = "Init";
                        spi.setComPortParameters(9600, 8, 1, 0);
                        if (!initializedUsb.contains(ss)) {
                            result = runAT(spi, "at+cfun=1", 1000);
                            runAT(spi, "ATE0", 1000);
                            runAT(spi, "at+cmee=2", 1000);
                            runAT(spi, "at+cscs=\"ira\"", 1000);
                            runAT(spi, "at+cusd=1", 1000);
                            initializedUsb.add(ss);
                        }

                        if (ss.contains("ttyUSB0") || ss.contains("USB5") || ss.contains("USB9") || s.contains("UI AT") || s.contains("Modem #")) {

                            Logger.getLogger(this.getClass().getName()).info("Port " + s + "Sys portnme " + spi.getSystemPortName() + "Descript " + spi.getPortDescription());
                            Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + result + " " + s);
                            if (result.contains("OK") || result.contains("Init")) {
                                String rst = runAT(spi, "AT+COPS?", 1000);
                                Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
//                                   String prod = produit.toUpperCase();
                                if ((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN"))) {
                                    String cmd = runAT(spi, "AT+CUSD=1,\"*1000#\",15", 3000);
                                    Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                                    cmd = runAT(spi, "AT+CUSD=1,\"4\",15", 3000);
                                    Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                                    String cmd1 = runAT(spi, "AT+CUSD=1,\"" + PIN_AIRTEL + "\",15", 3000);
                                    Logger.getLogger(this.getClass().getName()).info("TE resp " + cmd);
                                    USSDCommand ussd = new USSDCommand();
                                    ussd.setUssdCode("AIR002A00310030003000300023");
                                    ussd.setOperator(Constants.AIRTEL);
                                    ussd.setResult(cmd1.replaceAll("\\r\\n|\\r|\\n|\\s", ""));
                                    ssss = ussd.getResult();
                                    if (!ssss.contains(",") || !ssss.contains("CO:eTopUP")) {
                                        continue label;
                                    }
                                    String resultat[] = ssss.split(",");
                                    String rep = resultat[1];
                                    Logger.getLogger(this.getClass().getName()).info("TE REP >>xxxxxxxxxx " + rep);
                                    rep = rep.replaceAll("\\s", "");
                                    Logger.getLogger(this.getClass().getName()).info("TE REP NOW >< >< <> " + rep);
                                    int end = rep.lastIndexOf("\"");
                                    int start = rep.lastIndexOf(":");
                                    String sl = rep.substring(start + 1, end);
                                    Logger.getLogger(this.getClass().getName()).info("Solde " + sl);
                                    try {
                                        sl = sl.replace("\\s", "");
                                        solde = Double.parseDouble(sl);
                                    } catch (Exception e) {
                                        Logger.getLogger(this.getClass().getName()).info("Solde " + e.getMessage());
                                    }
                                }
                            }
                        }

                    }
                    notifyIt(ssss, solde);
                }
            }).start();

        }

        public void setOnSoldeAirtelChangeListener(AirtelSoldeChangeListener l) {
            this.listenner = l;
        }

        public void setOnSoldeOrangeChangeListener(OrangeSoldeChangeListener listener) {
            this.olistener = listener;
        }

        public void setOnSoldeVodacomChangeListener(VodacomSoldeChangeListener vlistener) {
            this.vlistener = vlistener;
        }

        private void notifyOrange(String res, double s) {
            if (this.olistener != null) {
                this.olistener.onOrangeSoldeChange(s, res);
            }
        }

        private void notifyVodacom(String result, double s) {
            if (this.vlistener != null) {
                this.vlistener.onVodacomSoldeChange(s, result);
            }
        }

        private void notifyIt(String r, double s) {
            if (this.listenner != null) {
                this.listenner.onAirtelSoldeChange(s, r);
            }
        }
    }

}
