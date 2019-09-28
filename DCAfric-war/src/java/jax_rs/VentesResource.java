/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import cdi_impl.ClientService;
import cdi_impl.ParametreService;
import cdi_impl.ProduitService;
import cdi_impl.VenteService;
import com.districom.endeleya.districomservices.tools.Promotion;
import com.fazecast.jSerialComm.SerialPort;
import entities.Client;
import entities.Commande;
import entities.CommandePK;
import entities.Kiosque;
import entities.Produit;
import entities.Vente;
import entities.VentePK;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
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
import jax_binding.CommandBinder;
import jax_binding.ProduitVenduBinder;
import jax_binding.PromotionBinder;
import jax_binding.TopClientBinder;
import jax_binding.VenteBinder;
import org.jboss.logging.Logger;
import stateless.ObtenirBeans;
import stateless.VenteBeans;
import util.Constants;
import util.GsmStringset;
import util.ProduitVendu;
import util.TopClient;
import util.USSDCommand;

/**
 * REST Web Service
 *
 * @author PC
 */
@Path("/ventes")
@Stateless
public class VentesResource {
    
    @Context
    private UriInfo context;
    @EJB
    VenteService vs;
    @EJB
    ProduitService ps;
    
    @EJB
    ClientService svc;
    
    @EJB
    ObtenirBeans ob;
    
    @EJB
    VenteBeans ventesRepport;
    
    @EJB
    ParametreService params;
    
    private double unite_per_mega_orange = 0;
    private double unite_per_mega_vodacom = 0;
    private String base_kiosque;
    
    @PostConstruct
    public void init() {
        unite_per_mega_orange = Double.parseDouble(params.getParametre("UNITE_PAR_MEGA_ORANGE").getValeur());
        unite_per_mega_vodacom = Double.parseDouble(params.getParametre("UNITE_PAR_MEGA_VODACOM").getValeur());
        base_kiosque = params.getParametre("BASE_KIOSQ").getValeur();
    }

    /**
     * Retrieves representation of an instance of jax_rs.VentesResource
     *
     * @return an instance of javax.json.JsonObject
     */
    @GET
    @Produces("application/json")
    @Path("recette/prouduit/intrval/{kiosk}/{date1}/{date2}")
    public List<ProduitVendu> getJsons(@PathParam("kiosk") String ksk, @PathParam("date1") String date1, @PathParam("date2") String date2) {
        return new ProduitVenduBinder(vs.getVentes(ksk, date1, date2));
    }
    
    @GET
    @Produces("text/plain")
    @Path("instant/solde/for/{produit}/{kiosq}")
    public double getSoldeFor(@PathParam("produit") String pro, @PathParam("kiosq") String kiosq) {
        if (pro.equals("733") || pro.equals("736")) {
            //unite or
            double achat = vs.getSommeQuantAchatPerKiosq("733", kiosq);
            double unite_vendu = vs.getSommeQuantVenduPerKiosq("733", kiosq);
            double mega_vendu = vs.getSommeQuantVenduPerKiosq("736", kiosq);
            double mult = mega_vendu * unite_per_mega_orange;
            return (achat - (unite_vendu + mult));
        } else if (pro.equals("734") || pro.equals("735")) {
            double achat = vs.getSommeQuantAchatPerKiosq("734", kiosq);
            double unite_vendu = vs.getSommeQuantVenduPerKiosq("734", kiosq);
            double mega_vendu = vs.getSommeQuantVenduPerKiosq("735", kiosq);
            double mult = mega_vendu * unite_per_mega_vodacom;
            return (achat - (unite_vendu + mult));
        } else {
            double achat = vs.getSommeQuantAchatPerKiosq(pro, kiosq);
            double vente = vs.getSommeQuantVenduPerKiosq(pro, kiosq);
            return achat - vente;
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("billing/ventes/commands/interval/{kiosk}/{date1}/{date2}")
    public List<Commande> getCommandes(@PathParam("kiosk") String idKiosk, @PathParam("date1") String date1, @PathParam("date2") String date2) {
        return new CommandBinder(vs.getCommands(idKiosk, date1, date2));
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("inventaire/pour/{idKiosque}/{idProduit}")
    public Response getSums(@PathParam("idKiosque") String idKiosque, @PathParam("idProduit") String idPro) {
        return Response.ok(vs.getSumVendu(idKiosque, idPro)).build();
    }
    
    @GET
    @Produces("application/json")
    @Path("commands/from/reference/{reference}/kiosk_id/{idKiosque}")
    public List<ProduitVendu> getCommandDetails(@PathParam("reference") String reference, @PathParam("idKiosque") String idKiosque) {
        Logger.getLogger(this.getClass().getName()).info(reference + " =mm= " + idKiosque);
        return vs.loadVentesForCommande(reference, idKiosque);
    }
    
    @POST
    @Produces("application/json")
    @Path("customers/input/vente/")
    public Response createVente(Vente v) {
        v.setDate(new Date());
        Vente ve = vs.createVente(v);
        Produit p = ps.getProduct(ve.getVentePK().getIdProduit());
        ProduitVendu pv = new ProduitVendu();
        pv.setDate(Constants.dateFormat.format(ve.getDate()));
        pv.setFournisseur(p.getFournisseur());
        pv.setIdProduit(p.getId());
        pv.setMontantTotalVendu(ve.getMantant());
        pv.setQuaniteVendu(ve.getQuantite());
        pv.setPrixUnitaire(vs.getLastPrice(p.getId()));
        pv.setNomProduit(p.getNomProduit());
        return Response.ok(pv).build();
    }
    
    @GET
    @Path("rapport/view/parpage/details/toutvente/{reference}/{dateDebut}/{dateFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vente> getAllVentes(@PathParam("reference") String kiosque, @PathParam("dateDebut") Date date1, @PathParam("dateFin") Date date2) {
        List<Vente> ventes = ventesRepport.getVenteForKiosqueAllDetails(kiosque, date1, date2);
        return new VenteBinder(ventes);
    }
    
    @GET
    @Path("rapport/view/parpage/groupe/parproduit/{reference}/{dateDebut}/{dateFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vente> getAllVentesForKiosqueParProduit(@PathParam("reference") String kiosque, @PathParam("dateDebut") Date date1, @PathParam("dateFin") Date date2) {
        List<Vente> ventes = ventesRepport.getVenteForKiosqueGroupByProductOnPeriod(kiosque, date1, date2);
        return new VenteBinder(ventes);
    }
    
    @GET
    @Path("rapport/view/allsales/for/{reference}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vente> getAllVentesWithKiosq(@PathParam("reference") String kiosque) {
        List<Vente> ventes = vs.getAllVentes(kiosque);
        return new VenteBinder(ventes);
    }
    
    @GET
    @Path("rapport/view/sum/de/toutvente/en/{devise}/{reference}/{dateDebut}/{dateFin}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSumTotalForKiosque(@PathParam("devise") String devise, @PathParam("reference") String kiosque, @PathParam("dateDebut") Date date1, @PathParam("dateFin") Date date2) {
        Double d = ventesRepport.getSumVenteForKiosqueOnPeriod(kiosque, devise, date1, date2);
        return Response.ok(d + " " + devise).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("customers/clients/commande/vente_on_cmd")
    public Response createCommand(Commande cmd) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd à HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        d1.format(df);
        Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        cmd.setDate(d);
        Commande cmde = vs.createCommande(cmd);
        if (cmd.getLibelle().equals("Bonus")) {
            List<Commande> cmds = vs.getClientPoints(cmd.getCommandePK().getIdClient());
            for (Commande c : cmds) {
                c.setValide(true);
                vs.updateCommande(c);
            }
        }
        return Response.ok(cmde).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("customers/clients/vente/plusieurs")
    public Response createGroupVente(List<Vente> ventes) {
        try {
            ventes.stream().forEach((ve) -> {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd à HH:mm:ss");
                LocalDateTime d1 = LocalDateTime.now();
                d1.format(df);
                Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
                ve.setDate(d);
                vs.createVente(ve);
            });
            Vente v = ventes.get(0);
            Commande cmd = vs.getFromReference(v.getVentePK().getReference());
            return Response.ok(cmd).build();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(VentesResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("vbase/cl/formulaire")
    public Response createVenteForm(Vente V) {
        String agent=V.getDevise();
        String kiosq = ob.getObtenir(agent).getObtenirPK().getIdKiosq();
        String reference=base_kiosque + "-" + ((int) (Math.random() * 100001));
        Commande cmd = new Commande();
        CommandePK cmdPK = new CommandePK();
        cmd.setDate(Constants.Datetime.todayTime());
        cmd.setLibelle("Normal");
        cmd.setMethode("CASH");
        cmd.setNombreArticle(1);
        cmd.setValide(true);  
        
        cmd.setComment("No comment");
        cmdPK.setId(System.currentTimeMillis());
        cmdPK.setIdClient(kiosq);
        cmdPK.setIdKiosque(base_kiosque);
        cmdPK.setReference(reference);
        cmd.setCommandePK(cmdPK);
        vs.createCommande(cmd);
       
        V.setDate(Constants.Datetime.todayTime());
        V.setDevise("USD"); 
        VentePK vpk=V.getVentePK();
        vpk.setId((int) (Math.random() * 100001));
        vpk.setReference(reference);
        V.setVentePK(vpk);
        vs.createVente(V);
        return Response.ok(V).build();
    }
    
    @GET
    @Produces("application/json")
    @Path("achats/promotion/clients/{phone}")
    public List<Promotion> searchForEligibility(@PathParam("phone") String phone) {
        Client clt = svc.getClientByPhone(phone);
        if (clt == null) {
            String id = String.valueOf((int) (Math.random() * 1000000));
            clt = new Client();
            clt.setId(phone);
            clt.setPhone(phone);
            clt.setPrenom("CLT-" + id);
            svc.createClient(clt);
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Commande> lc = vs.getClientPoints(clt.getId());
        List<Promotion> lp = new ArrayList<>();
        for (Commande c : lc) {
            String ref = c.getCommandePK().getReference();
            Vente v = vs.getVentesByReference(ref).get(0);
            Promotion p = new Promotion();
            p.setCode(ref);
            p.setProduit(c.getComment());
            p.setQuantite(v.getQuantite());
            p.setPhone(clt.getPhone());
            //LocalDateTime d1 = LocalDateTime.parse(c.getDate().toString());
            p.setDate(Constants.dateFormat.format(c.getDate()));
            lp.add(p);
        }
        return new PromotionBinder(lp);
    }
    
    
    @GET
    @Produces("application/json")
    @Path("top10/kiosque")
    public List<TopClient> getTop10Kiosque(){
        //select count(*) as freq , max(quantite) somme, commande.id_client FROM vente,commande where commande.reference = vente.reference group by commande.id_client  order by freq desc limit 10
        return new TopClientBinder(vs.gettop10()); 
    }
    
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("executer/u/telekom/p")
    public Response execute(USSDCommand u) {
        SerialPort sports[] = SerialPort.getCommPorts();
        Logger.getLogger(this.getClass().getName()).info("tailles port " + sports.length);
        List<Vente> ventes = u.getVentes();
        String s;
        String code = u.getUssdCode();
        String product = u.getProduit().toUpperCase();
        String reso = u.getOperator().toUpperCase().trim();
        Commande com = u.getCommande();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        d1.format(df);
        Date d = java.util.Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        com.setDate(d);
        
        for (SerialPort spi : sports) {
            s = spi.getDescriptivePortName();
            product = product.toUpperCase();
            spi.setComPortParameters(9600, 8, 1, 0);
            String result = executeAT(spi, "at+cfun=1", 1000);
            if (result.contains("OK")) {
                
                if (s.contains("UI AT") || s.contains("Modem #")) {
                    String rst = executeAT(spi, "at+cops?", 1000);
                    Logger.getLogger(this.getClass().getName()).info("TE found : Operator > " + rst);
                    executeAT(spi, "ATE0", 1000);
                    executeAT(spi, "at+cmee=2", 1000);
                    executeAT(spi, "at+cscs=\"ira\"", 1000);
                    executeAT(spi, "at+cusd=1", 1000);
                    
                    if (((rst.contains("63002") || rst.toUpperCase().contains(Constants.AIRTEL) || rst.toUpperCase().contains("CELTEL DRC") || rst.toUpperCase().contains("ZAIN")) && reso.toUpperCase().contains(Constants.AIRTEL))) {
                        Logger.getLogger(this.getClass().getName()).info(" GSMSH#ai> " + code);
                        
                        String cmd;
                        switch (product) {
                            case Constants.UNITE:
                                
                                String c = "at+cusd=1,\"" + GsmStringset.gsmToStringDecoded(code) + "\",15";
                                cmd = executeAT(spi, c, Constants.TIMEOUT_USSD);
                                String sss = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss);
                                if ((sss.contains("OK") && sss.contains("+CUSD:2"))) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    vs.createCommande(com);
                                    ventes.stream().forEach((ve) -> {
                                        ve.setDate(d);
                                        vs.createVente(ve);
                                    });
                                    return Response.ok(u).build();
                                } else if (cmd.trim().contains("ERROR")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    return Response.ok(u).build();
                                    
                                }
                                
                                break;
                            case Constants.MEGA:
                                
                                String cl = "at+cusd=1,\"" + GsmStringset.gsmToStringDecoded(code) + "\",15";
                                cmd = executeAT(spi, cl, Constants.TIMEOUT_USSD);
                                String sss1 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss1);
                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> " + code);
                                if ((sss1.contains("OK") && sss1.contains("+CUSD:2")) || sss1.contains("+CUSD:0")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    vs.createCommande(com);
                                    ventes.stream().forEach((ve) -> {
                                        ve.setDate(d);
                                        vs.createVente(ve);
                                    });
                                    return Response.ok(u).build();
                                } else if (cmd.trim().contains("ERROR")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    return Response.ok(u).build();
                                    
                                }
                                
                                break;
                        }
                    } else if (((rst.contains("63089") || rst.contains("63086") || rst.toUpperCase().contains(Constants.ORANGE) || rst.contains("CCT") || rst.toUpperCase().contains("TIGO")) && reso.toUpperCase().contains(Constants.ORANGE))) {
                        Logger.getLogger(this.getClass().getName()).info(" GSMSH@or> " + GsmStringset.stringToGsmEncoded(code));
                        
                        String cmd;
                        switch (product) {
                            case Constants.UNITE:
                                
                                String decoded = GsmStringset.gsmToStringDecoded(code);
                                cmd = executeAT(spi, "at+cusd=1,\"" + decoded + "\",15", Constants.TIMEOUT_USSD);
                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE OR1" + code + "\nresult : " + cmd + " decoded " + decoded);
                                cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE OR2" + code + "\nresult : " + cmd + " decoded " + decoded);
                                String sss2 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss2);
                                if ((sss2.contains("OK") && sss2.contains("+CUSD:2")) || sss2.contains("+CUSD:0")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    vs.createCommande(com);
                                    ventes.stream().forEach((ve) -> {
                                        ve.setDate(d);
                                        vs.createVente(ve);
                                    });
                                    return Response.ok(u).build();
                                } else if (cmd.trim().contains("ERROR")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    return Response.ok(u).build();
                                    
                                }

                                //insertion bd puis return result to client
                                break;
                            case Constants.MEGA:
                                
                                String tarif = u.getResult();
                                if (tarif.contains("Home Box")) {
                                    
                                    String ussd[] = (GsmStringset.gsmToStringDecoded(code)).split("/");
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
                                    if ((sss3.contains("OK") && sss3.contains("+CUSD:2")) || sss3.contains("+CUSD:0")) {
                                        u.setUssdCode(code);
                                        u.setResult(cmd);
                                        vs.createCommande(com);
                                        ventes.stream().forEach((ve) -> {
                                            ve.setDate(d);
                                            vs.createVente(ve);
                                        });
                                        return Response.ok(u).build();
                                    } else if (cmd.trim().contains("ERROR")) {
                                        u.setUssdCode(code);
                                        u.setResult(cmd);
                                        return Response.ok(u).build();
                                        
                                    }
                                } else if (tarif.contains("Bundle")) {
                                    //"*134*" + phone.getText().toString() + "*" + qte.getText().toString() + "*" + pswd.getText().toString().toString() + "#"

                                    String ussd[] = (GsmStringset.gsmToStringDecoded(code)).split("/");
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
                                    
                                    switch (ussd[2]) {
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
                                    if ((sss4.contains("OK") && sss4.contains("+CUSD:2")) || sss4.contains("+CUSD:0")) {
                                        u.setUssdCode(code);
                                        u.setResult(cmd);
                                        vs.createCommande(com);
                                        ventes.stream().forEach((ve) -> {
                                            ve.setDate(d);
                                            vs.createVente(ve);
                                        });
                                        return Response.ok(u).build();
                                    } else if (cmd.trim().contains("ERROR")) {
                                        u.setUssdCode(code);
                                        u.setResult(cmd);
                                        return Response.ok(u).build();
                                        
                                    }
                                }
                        }
                        break;
                        
                    } else if (((rst.contains("63001") || rst.toUpperCase().contains(Constants.VODACOM)) && reso.toUpperCase().contains(Constants.VODACOM))) {
                        Logger.getLogger(this.getClass().getName()).info(" GSMSH$vo> " + code);
                        String cmd;
                        switch (product) {
                            case Constants.UNITE:
                                
                                cmd = executeAT(spi, "at+cusd=1,\"" + GsmStringset.gsmToStringDecoded(code) + "\",15", Constants.TIMEOUT_USSD);
                                Logger.getLogger(this.getClass().getName()).info(" GSMSH$> CODE " + code + "\nresult : " + cmd);
                                cmd = executeAT(spi, "at+cusd=1,\"1\",15", (Constants.TIMEOUT_USSD - 1000));
                                Logger.getLogger(this.getClass().getName()).info(" GSMSH$> CODE " + code + "\nresult : " + cmd);
                                String sss5 = cmd.replaceAll("\\s|\\r\\n|\\r|\\n", "");
                                Logger.getLogger(this.getClass().getName()).info(" REsultat ********************** " + sss5);
                                if ((sss5.contains("OK") && sss5.contains("+CUSD:0")) || sss5.contains("+CUSD:2")) {
                                    u.setUssdCode(code);
                                    u.setResult(sss5);
                                    vs.createCommande(com);
                                    ventes.stream().forEach((ve) -> {
                                        ve.setDate(d);
                                        vs.createVente(ve);
                                    });
                                    return Response.ok(u).build();
                                } else if (cmd.trim().contains("ERROR")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    return Response.ok(u).build();
                                }
                                
                                break;
                            case Constants.MEGA:
                                
                                String ussd[] = (GsmStringset.gsmToStringDecoded(code)).split("/");
                                String ss = GsmStringset.gsmToStringDecoded("002A00310031003100370023");
                                executeAT(spi, "at+cusd=1,\"" + ss + "\",15", 600);
                                cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                if (ussd[1].equals("50")) {
                                    cmd = executeAT(spi, "at+cusd=1,\"1\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                } else if (ussd[1].equals("100")) {
                                    cmd = executeAT(spi, "at+cusd=1,\"2\",15", Constants.TIMEOUT_USSD);
                                    Logger.getLogger(this.getClass().getName()).info(" GSMSH#> CODE " + code + "\nresult : " + cmd);
                                } else if (ussd[1].equals("1024")) {
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
                                if ((sss6.contains("OK") && sss6.contains("+CUSD:0")) || sss6.contains("+CUSD:2")) {
                                    u.setUssdCode(code);
                                    u.setResult(sss6);
                                    vs.createCommande(com);
                                    ventes.stream().forEach((ve) -> {
                                        ve.setDate(d);
                                        vs.createVente(ve);
                                    });
                                    return Response.ok(u).build();
                                } else if (cmd.trim().contains("ERROR")) {
                                    u.setUssdCode(code);
                                    u.setResult(cmd);
                                    return Response.ok(u).build();
                                    
                                }
                                
                                break;
                        }
                    }
                }
            }
        }
        
        return Response.status(Response.Status.NO_CONTENT).build();
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

    /**
     * Sub-resource locator method for {name}Ha
     */
    @Path("/one/uid-{name}/pr-{prod}/rf-{ref}")
    public VenteResource getVenteResource(@PathParam("name") int name, @PathParam("prod") String prod, @PathParam("ref") String ref) {
        return VenteResource.getInstance(vs, name, ref, prod);
    }
    
}
