/**
 * Copyright 2010-2017, by the California Institute of Technology.
 */
package gov.nasa.pds.tracking.tracking.jsoninterfaces;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import org.json.JSONException;
import org.json.JSONObject;

import gov.nasa.pds.tracking.tracking.db.Doi;
import gov.nasa.pds.tracking.tracking.db.DoiDao;

/**
 * @author danyu dan.yu@jpl.nasa.gov
 *
 */
@Path("json/doi")
public class JSONBasedDOI {
	
	public static Logger logger = Logger.getLogger(JSONBasedDOI.class);
	private static final String FAILURE_RESULT="Failure";
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DoiDao dd;
	
	/**
	 * @return
	 * @throws JSONException
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response defaultDOI() throws JSONException {
 
        JSONObject jsonDOIs = new JSONObject();
        
        JSONObject jsonDOI = new JSONObject();
        
        DoiDao doi;
		try {
			
			doi = new DoiDao();
			List<Doi> dois = doi.getDOIList();
			logger.info("number of DOI: "  + dois.size());
			
			Iterator<Doi> itr = dois.iterator();
			int count = 1;
			while(itr.hasNext()) {
		         Doi d = itr.next();
		         logger.debug("DOI " + count + ":\n " + d.getLog_identifier() + " : " + d.getDoi());
					
		         jsonDOI = new JSONObject();	         
		         jsonDOI.put(Doi.LOG_IDENTIFIERCOLUME, d.getLog_identifier());
		         jsonDOI.put(Doi.VERSIONCOLUME, d.getVersion());
		         jsonDOI.put(Doi.DOICOLUME, d.getDoi());
		         jsonDOI.put(Doi.DATECOLUME, df.format(d.getDate()));
		         jsonDOI.put(Doi.URLCOLUME, d.getUrl());		         
		         jsonDOI.put(Doi.EMAILCOLUME, d.getEmail());
		         jsonDOI.put(Doi.COMMENTCOLUME, d.getComment() != null ? d.getComment() : "");
		         
		         jsonDOIs.append("doi", jsonDOI);
		         count++;
		    }
			
						
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonDOIs.toString(4);
        return Response.status(200).entity(result).build();
    }
	
	/**
	 * @param id
	 * @param version
	 * @return
	 * @throws JSONException
	 */
	@Path("{id : (.+)?}/{version : (.+)?}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDOIsByIDAndVersion(@PathParam("id") String id, @PathParam("version") String version)  throws JSONException {
		
		JSONObject jsonDOIs = new JSONObject();
        
        JSONObject jsonDOI = new JSONObject();
        
        DoiDao dd;
		try {
			
			dd = new DoiDao();
			Doi d = dd.getDOI(id, version);
			jsonDOI = new JSONObject();	         
			jsonDOI.put(Doi.LOG_IDENTIFIERCOLUME, d.getLog_identifier());
			jsonDOI.put(Doi.VERSIONCOLUME, d.getVersion());
			jsonDOI.put(Doi.DOICOLUME, d.getDoi());
			jsonDOI.put(Doi.DATECOLUME, df.format(d.getDate()));
			jsonDOI.put(Doi.URLCOLUME, d.getUrl());		         
			jsonDOI.put(Doi.EMAILCOLUME, d.getEmail());
			jsonDOI.put(Doi.COMMENTCOLUME, d.getComment() != null ? d.getComment() : "");
			
			jsonDOIs.append("doi", jsonDOI);
		         
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonDOIs.toString(4);
        return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response createDOI(@FormParam("LOGICAL_ID") String id,
			@FormParam("Version") String ver,
			@FormParam("Doi") String doi,
			@FormParam("URL") String url,
			@FormParam("Email") String email,
			@FormParam("Comment") String comment) throws IOException{
		
		JSONObject relt = new JSONObject();
		JSONObject message = new JSONObject();
		try {
			dd = new DoiDao();

			Doi d = new Doi(id, ver, doi, new Timestamp(new Date().getTime()), url, email, comment);
			
			int result = dd.insertDOI(d);
			
			if(result == 1){
				message.put(DoiDao.LOG_IDENTIFIERCOLUME, d.getLog_identifier());
				message.put(DoiDao.VERSIONCOLUME, d.getVersion());
				message.put(DoiDao.DOICOLUME, d.getDoi());
				message.put(DoiDao.DATECOLUME, df.format(d.getDate()));
				message.put(DoiDao.URLCOLUME, d.getUrl());
				message.put(DoiDao.EMAILCOLUME, d.getEmail());
				message.put(DoiDao.COMMENTCOLUME, d.getComment() != null ? d.getComment() : "");
			}
		} catch (ClassNotFoundException | SQLException e) {			
			e.printStackTrace();
			message.put("Message", FAILURE_RESULT);
		}
		//logger.debug("Result: " + message);
		relt.append("doi", message);
		String jsonOutput = "" + relt.toString(4);
		return Response.status(200).entity(jsonOutput).build();
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response updateDOI(@FormParam("LOGICAL_ID") String id,
			@FormParam("Version") String ver,
			@FormParam("URL") String url,
			@FormParam("Email") String email,
			@FormParam("Comment") String comment) throws IOException{
		
		JSONObject relt = new JSONObject();
		JSONObject message = new JSONObject();
		try {
			dd = new DoiDao();
		
			Doi d = new Doi(id, ver, null, null, url, email, comment);
			
			Doi updatedDoi = dd.updateDOI(d);
			
			if(updatedDoi != null && updatedDoi.getLog_identifier() != null){		

				message.put(DoiDao.LOG_IDENTIFIERCOLUME, updatedDoi.getLog_identifier());
				message.put(DoiDao.VERSIONCOLUME, updatedDoi.getVersion());
				message.put(DoiDao.DOICOLUME, updatedDoi.getDoi());
				message.put(DoiDao.DATECOLUME, df.format(updatedDoi.getDate()));
				message.put(DoiDao.URLCOLUME, updatedDoi.getUrl());
				message.put(DoiDao.EMAILCOLUME, updatedDoi.getEmail());
				message.put(DoiDao.COMMENTCOLUME, updatedDoi.getComment() != null ? updatedDoi.getComment() : "");
				
			}else{
				message.put("Message", FAILURE_RESULT);
			}
		} catch (ClassNotFoundException | SQLException e) {			
			e.printStackTrace();
			message.put("Message", FAILURE_RESULT);
		}
		//logger.debug("Result: " + message);
		relt.append("doi", message);
		String jsonOutput = "" + relt.toString(4);
		return Response.status(200).entity(jsonOutput).build();
	}
}
