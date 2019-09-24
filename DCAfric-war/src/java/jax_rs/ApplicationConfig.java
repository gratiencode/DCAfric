/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jax_rs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;


/**
 *
 * @author PC
 */
@javax.ws.rs.ApplicationPath("/providing")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
//        resources.add(JacksonFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

//    @Override
//    public Map<String, Object> getProperties() {
//        HashMap<String,Object> propertis=new HashMap<>();
//        propertis.put("jersey.config.server.provider.packages", "")
//        return super.getProperties(); //To change body of generated methods, choose Tools | Templates.
//    }
    
    

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(jax_rs.AgentResource.class);
        resources.add(jax_rs.AgentsResource.class);
        resources.add(jax_rs.BroadCaster.class);
        resources.add(jax_rs.CORSFilter.class);
        resources.add(jax_rs.ClientResource.class);
        resources.add(jax_rs.ClientsResource.class);
        resources.add(jax_rs.ConsommerResource.class);
        resources.add(jax_rs.ConsommersResource.class);
        resources.add(jax_rs.KiosqResource.class);
        resources.add(jax_rs.KiosqsResource.class);
        resources.add(jax_rs.ParametreResource.class);
        resources.add(jax_rs.ParametresResource.class);
        resources.add(jax_rs.ProduitResource.class);
        resources.add(jax_rs.ProduitsResource.class);
        resources.add(jax_rs.RecquisitionResource.class);
        resources.add(jax_rs.RecquisitionsResource.class);
        resources.add(jax_rs.ServiceResource.class);
        resources.add(jax_rs.ServicesResource.class);
        resources.add(jax_rs.SmartSoldeResource.class);
        resources.add(jax_rs.SmartSoldesResource.class);
        resources.add(jax_rs.VentesResource.class);
    }
    
}
