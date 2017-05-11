/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST.service;

import Entities.Evenement;
import Exception.notFoundEvenementException;
import Metier.IgestionEvenement;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.*;
/**
 *
 * @author fez
 */
@javax.enterprise.context.RequestScoped
@Path("evenements")
public class EvenementFacadeREST{
    
      @EJB
    private IgestionEvenement gE;

    @GET
    @Path("getListeEvenements")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListeEvenements(@QueryParam("token") int pid) {
        
        try{
           List<Evenement> lesEvents = gE.getListeEvenements(pid);
           
           JSONArray events = new JSONArray();
           JSONObject obj = new JSONObject();
           
           for(Evenement e: lesEvents){
               JSONObject tempo = new JSONObject();
               tempo.put("id", e.getId());
               tempo.put("intitule", e.getIntitule());
               tempo.put("etat",e.getEtatEvenement());
                   
               events.put(tempo);
           }
           
          obj.put("Evenements", events);
               
           return Response.ok(events.toString(), MediaType.APPLICATION_JSON).build();
        }catch(notFoundEvenementException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    
    @POST 
    @Path("creerEvenement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creerEvenement(@QueryParam("token") int pid ,String data ){
        String dateFin = "";
       try{
             
           JSONObject obj = new JSONObject(data);
           
           if(!obj.has("dateFin")){
               dateFin = null;
           }else{
              dateFin =  obj.getString("dateFin");
           }
                 
            // int idUser, String intitule, String description, String dateDebut, String dateFin, String lieu, int nbInvite, String msg)
           gE.creationEvenement(pid, obj.getString("intitule"), obj.getString("description"),
           obj.getString("dateDebut"), dateFin, obj.getString("lieu"), obj.getInt("nombreInvite"), obj.getString("message"));
           
                 
           return Response.ok(new JSONObject().put("Statut", "ok").toString(), MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    
    @PUT
    @Path("{id}/modifierEvenement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifierEvenement(@QueryParam("token") int pid ,@PathParam("id") Integer idEvent,String data ){
        String dateFin = "";
       try{

           JSONObject obj = new JSONObject(data);
           
           if(!obj.has("dateFin")){
               dateFin = null;
           }else{
              dateFin =  obj.getString("dateFin");
           }
                 
            // int idEvent, int idUser, String intitule, String description, String dateDebut, String dateFin, String lieu, int nbInvite, String msg
            gE.modifierEvenement(idEvent, pid, obj.getString("intitule"), obj.getString("description"), 
                    obj.getString("dateDebut"), dateFin, obj.getString("lieu"), obj.getInt("nombreInvite"), obj.getString("message"));
          
           return Response.ok(new JSONObject().put("Statut", "ok").toString(), MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    @GET
    @Path("{id}/annulerEvenement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response annulerEvenement(@QueryParam("token") int pid ,@PathParam("id") Integer idEvent ){
       try{
          //TODO voir si on vérifie la valider du token ici
           gE.annulerEvenement(idEvent,pid);
                 
           return Response.ok(new JSONObject().put("Statut", "ok").toString(), MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
}