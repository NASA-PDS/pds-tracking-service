/**
 * Copyright 2010-2017, by the California Institute of Technology.
 */
package gov.nasa.pds.tracking.tracking.jsoninterfaces;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import gov.nasa.pds.tracking.tracking.db.Releases;
import gov.nasa.pds.tracking.tracking.db.ReleasesDao;

/**
 * @author danyu dan.yu@jpl.nasa.gov
 *
 */
@Path("json/releases")
public class JSONBasedReleases {
	
	public static Logger logger = Logger.getLogger(JSONBasedReleases.class);

	private static final String FAILURE_RESULT="Failure";
	private ReleasesDao relD;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * @return
	 * @throws JSONException
	 */
	@GET
    @Produces("application/json")
    public Response defaultReleases() throws JSONException {
 
        JSONObject jsonReleases = new JSONObject();
        
        JSONObject jsonRelease = new JSONObject();
        
        ReleasesDao rel;
		try {
			
			rel = new ReleasesDao();
			List<Releases> rels = rel.getReleasesList();
			logger.info("number of Releases: "  + rels.size());
			
			Iterator<Releases> itr = rels.iterator();
			int count = 1;
			while(itr.hasNext()) {
				Releases r = itr.next();
		         logger.debug("Releases " + count + ":\n " + r.getLogIdentifier() + " : " + r.getDate().toString());
					
		         jsonRelease = new JSONObject();
		     	
		     	jsonRelease.put(ReleasesDao.LOGIDENTIFIERCOLUME, r.getLogIdentifier());
		     	jsonRelease.put(ReleasesDao.VERSIONCOLUME, r.getVersion());
		     	jsonRelease.put(ReleasesDao.DATECOLUME, df.format(r.getDate()));
		     	jsonRelease.put(ReleasesDao.ANCMDATECOLUME, df.format(r.getAnnouncement_date()));
		     	jsonRelease.put(ReleasesDao.NAMECOLUME, r.getName());
		     	jsonRelease.put(ReleasesDao.DESCCOLUME, r.getDescription());
		     	jsonRelease.put(ReleasesDao.EMAILCOLUME, r.getEmail());
		     	jsonRelease.put(ReleasesDao.COMMENTCOLUME, r.getComment() != null ? r.getComment() : "");
		         
		     	jsonReleases.append("Releases", jsonRelease);
		         count++;
		    }
			
						
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonReleases.toString(4);
        return Response.status(200).entity(result).build();
    }
	
	/**
	 * @param id
	 * @param version
	 * @return
	 * @throws JSONException
	 */
	@Path("{id : (.+)?}/{version : (.+)?}/{latest}")
    @GET
    @Produces("application/json")
    public Response Releases(@PathParam("id") String id, @PathParam("version") String version, @PathParam("latest") boolean latest)  throws JSONException {
		
		JSONObject jsonReleases = new JSONObject();
        
        JSONObject jsonRelease = new JSONObject();
        
        ReleasesDao relO;
        Releases rel;
		try {
			
			relO = new ReleasesDao();
			List<Releases> rels = new ArrayList<Releases>();
			
			if (latest) {
				rel = relO.getLatestReleases(id, version);
				if (rel != null)
				rels.add(rel);
			}else{
				rels = relO.getReleasesList(id, version);
			}
			logger.info("number of Releases: "  + rels.size());
			
			Iterator<Releases> itr = rels.iterator();
			int count = 1;
			while(itr.hasNext()) {
				Releases r = itr.next();
		         logger.debug("Releases " + count + ":\n " + r.getLogIdentifier() + " : " + r.getDate().toString());
					
		         jsonRelease = new JSONObject();
		     	
		     	jsonRelease.put(ReleasesDao.LOGIDENTIFIERCOLUME, r.getLogIdentifier());
		     	jsonRelease.put(ReleasesDao.VERSIONCOLUME, r.getVersion());
		     	jsonRelease.put(ReleasesDao.DATECOLUME, df.format(r.getDate()));
		     	jsonRelease.put(ReleasesDao.ANCMDATECOLUME, df.format(r.getAnnouncement_date()));
		     	jsonRelease.put(ReleasesDao.NAMECOLUME, r.getName());
		     	jsonRelease.put(ReleasesDao.DESCCOLUME, r.getDescription());
		     	jsonRelease.put(ReleasesDao.EMAILCOLUME, r.getEmail());
		     	jsonRelease.put(ReleasesDao.COMMENTCOLUME, r.getComment() != null ? r.getComment() : "");
		         
		     	jsonReleases.append("Releases", jsonRelease);
		         count++;
		    }
			
						
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonReleases.toString(4);
        return Response.status(200).entity(result).build();
	
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response createReleases(@FormParam("LogicalIdentifier") String logicalIdentifier,
			@FormParam("Version") String ver,
			@FormParam("Date") String date,
			@FormParam("Announcement_Date") String ancmDate,
			@FormParam("Name") String name,
			@FormParam("Desc") String desc,
			@FormParam("Email") String email,
			@FormParam("Comment") String comment) throws IOException{
		
		JSONObject relt = new JSONObject();
		JSONObject message = new JSONObject();
		
		try {
			relD = new ReleasesDao();
			java.util.Date rels_date = df.parse(date);
			logger.debug("util date time");
			logger.debug(rels_date);
			java.util.Date ancm_date = df.parse(ancmDate);
			logger.debug(ancm_date);
			
			logger.debug("sql date time");
			logger.debug(new Timestamp(rels_date.getTime()));
			logger.debug(new Timestamp(ancm_date.getTime()));

			Releases rel = new Releases(logicalIdentifier, ver, new Timestamp(rels_date.getTime()), new Timestamp(ancm_date.getTime()), name, desc, email, comment);
			int result = relD.insertReleases(rel);
			
			if(result == 1){
				message.put(ReleasesDao.LOGIDENTIFIERCOLUME, rel.getLogIdentifier());
				message.put(ReleasesDao.VERSIONCOLUME, rel.getVersion());
				logger.debug("back date");
				logger.debug(df.format(rel.getDate()));
				message.put(ReleasesDao.DATECOLUME, df.format(rel.getDate()));
				logger.debug(df.format(rel.getAnnouncement_date()));
				message.put(ReleasesDao.ANCMDATECOLUME, df.format(rel.getAnnouncement_date()));
				message.put(ReleasesDao.NAMECOLUME, rel.getName());
				message.put(ReleasesDao.DESCCOLUME, rel.getDescription());
				message.put(ReleasesDao.EMAILCOLUME, rel.getEmail());		         
				message.put(ReleasesDao.COMMENTCOLUME, rel.getComment() != null ? rel.getComment() : "");
			}
		} catch (ClassNotFoundException | SQLException | ParseException e) {			
			e.printStackTrace();
			message.put("Message", FAILURE_RESULT);
		}
		//logger.debug("Result: " + message);
		relt.append("Releases", message);
		String jsonOutput = "" + relt.toString(4);
		return Response.status(200).entity(jsonOutput).build();
	}
}
