/**
 * Copyright 2010-2017, by the California Institute of Technology.
 */
package gov.nasa.pds.tracking.tracking.jsoninterfaces;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
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

import gov.nasa.pds.tracking.tracking.db.Product;
import gov.nasa.pds.tracking.tracking.db.ProductDao;
import gov.nasa.pds.tracking.tracking.db.ReferenceDao;

/**
 * @author danyu dan.yu@jpl.nasa.gov
 *
 */
@Path("json/products")
public class JSONBasedProducts {
	
	public static Logger logger = Logger.getLogger(JSONBasedProducts.class);
	
	private static final String FAILURE_RESULT="Failure";

	private ProductDao prodD;
	
	@GET
    @Produces("application/json")
    public Response defaultProducts() throws JSONException {
 
        JSONObject jsonProducts = new JSONObject();
        
        JSONObject jsonProd = new JSONObject();
        
        ProductDao prod;
		try {
			prod = new ProductDao();
			List<Product> prods = prod.getProductsOrderByTitle();
			logger.info("number of products: "  + prods.size());
			Iterator<Product> itr = prods.iterator();
			int count = 1;
			
			while(itr.hasNext()) {
		         Product p = itr.next();
		         logger.debug("Product " + count + ":\n " + p.getIdentifier() + " : " + p.getTitle());
		         
		         jsonProd = new JSONObject();
		         jsonProd.put(ProductDao.IDENTIFIERCOLUMN, p.getIdentifier());
		         jsonProd.put(ProductDao.VERSIONCOLUMN, p.getVersion());
		         jsonProd.put(ProductDao.TITLECOLUMN, p.getTitle());
		         jsonProd.put(ProductDao.TYPECOLUMN, p.getType());
		         jsonProd.put(ProductDao.ALTERNATECOLUMN, p.getAlternate() != null ? p.getAlternate() : "");

		         jsonProducts.append("products", jsonProd);
		         count++;
		    }
			
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonProducts.toString(4);
        return Response.status(200).entity(result).build();
    }

	@Path("{instRef : (.+)?}/{investRef : (.+)?}")
    @GET
    @Produces("application/json")
	public Response products(@PathParam("instRef") String insRef, @PathParam("investRef") String invRef)  throws JSONException {	
		JSONObject jsonProducts = new JSONObject();
        
        JSONObject jsonProd = new JSONObject();
        JSONObject jsonRef = new JSONObject();
        
        ProductDao prod;
		try {
			prod = new ProductDao();
			List<Product> prods = prod.getProductsAssociatedDeliveriesOrderByTitle(insRef, invRef);
			
			logger.info("number of products: "  + prods.size());
			Iterator<Product> itr = prods.iterator();
			int count = 1;
			
			while(itr.hasNext()) {
		         Product p = itr.next();
		         logger.debug("Product " + count + ":\n " + p.getIdentifier() + " : " + p.getTitle());
		         
		         jsonProd = new JSONObject();
		         jsonProd.put(ProductDao.IDENTIFIERCOLUMN, p.getIdentifier());
		         jsonProd.put(ProductDao.VERSIONCOLUMN, p.getVersion());
		         jsonProd.put(ProductDao.TITLECOLUMN, p.getTitle());
		         jsonProd.put(ProductDao.TYPECOLUMN, p.getType());
		         jsonProd.put(ProductDao.ALTERNATECOLUMN, p.getAlternate() != null ? p.getAlternate() : "");
		         
		         jsonRef = new JSONObject();		         
		         jsonRef.put(ReferenceDao.REFERENCECOLUMN, p.getInstRef());
		         jsonRef.put(ReferenceDao.TITLECOLUMN, p.getInstTitle());
		         jsonProd.append("instrument", jsonRef);
		         
		         jsonRef = new JSONObject();		         
		         jsonRef.put(ReferenceDao.REFERENCECOLUMN, p.getInveRef());
		         jsonRef.put(ReferenceDao.TITLECOLUMN, p.getInveTitle());
		         jsonProd.append("investigation", jsonRef);
		         
		         jsonRef = new JSONObject();		         
		         jsonRef.put(ReferenceDao.REFERENCECOLUMN, p.getNodeRef());
		         jsonRef.put(ReferenceDao.TITLECOLUMN, p.getNodeTitle());
		         jsonProd.append("node", jsonRef);

		         jsonProducts.append("products", jsonProd);
		         count++;
		    }
			
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonProducts.toString(4);
        return Response.status(200).entity(result).build();
	}
	
	@Path("type/{Type}")
    @GET
    @Produces("application/json")
	public Response products(@PathParam("Type") String type)  throws JSONException {	
		JSONObject jsonProducts = new JSONObject();
        
        JSONObject jsonProd = new JSONObject();
        
        ProductDao prod;
		try {
			prod = new ProductDao();
			List<Product> prods = prod.getProducts(type);
			
			logger.info("number of products: "  + prods.size());
			Iterator<Product> itr = prods.iterator();
			int count = 1;
			
			while(itr.hasNext()) {
		         Product p = itr.next();
		         logger.debug("Product " + count + ":\n " + p.getIdentifier() + " : " + p.getTitle());
		         
		         jsonProd = new JSONObject();
		         jsonProd.put(ProductDao.IDENTIFIERCOLUMN, p.getIdentifier());
		         jsonProd.put(ProductDao.VERSIONCOLUMN, p.getVersion());
		         jsonProd.put(ProductDao.TITLECOLUMN, p.getTitle());
		         jsonProd.put(ProductDao.TYPECOLUMN, p.getType());
		         jsonProd.put(ProductDao.ALTERNATECOLUMN, p.getAlternate() != null ? p.getAlternate() : "");

		         jsonProducts.append("products", jsonProd);
		         count++;
		    }
			
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonProducts.toString(4);
        return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response createProduct(@FormParam("LogicalIdentifier") String logicalIdentifier,
			@FormParam("Version") String ver,
			@FormParam("Title") String title,
			@FormParam("Type") String type,
			@FormParam("AlternateId") String altId) throws IOException{
		
		JSONObject relt = new JSONObject();
		JSONObject message = new JSONObject();
		try {
			prodD = new ProductDao();

			Product prod = new Product(logicalIdentifier, ver, title, type, altId);
			int result = prodD.insertProduct(prod);
			
			if(result == 1){
				message.put(ProductDao.IDENTIFIERCOLUMN, prod.getIdentifier());
				message.put(ProductDao.VERSIONCOLUMN, prod.getVersion());
				message.put(ProductDao.TITLECOLUMN, prod.getTitle());
				message.put(ProductDao.TYPECOLUMN, prod.getType());
				message.put(ProductDao.ALTERNATECOLUMN, prod.getAlternate() != null ? prod.getAlternate() : "");
			}
		} catch (ClassNotFoundException | SQLException e) {			
			e.printStackTrace();
			message.put("Message", FAILURE_RESULT);
		}
		//logger.debug("Result: " + message);
		relt.append("Product", message);
		String jsonOutput = "" + relt.toString(4);
		return Response.status(200).entity(jsonOutput).build();
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response updateProduct(@FormParam("LogicalIdentifier") String logicalIdentifier,
			@FormParam("Version") String ver,
			@FormParam("Title") String title,
			@FormParam("Type") String type,
			@FormParam("AlternateId") String altId) throws IOException{
		
		JSONObject relt = new JSONObject();
		JSONObject message = new JSONObject();
		try {
			prodD = new ProductDao();

			Product prodNew = new Product(logicalIdentifier, ver, title, type, altId);
			Product prod = prodD.updateProduct(prodNew);
			
			if(prod != null && prod.getIdentifier() != null){
				message.put(ProductDao.IDENTIFIERCOLUMN, prod.getIdentifier());
				message.put(ProductDao.VERSIONCOLUMN, prod.getVersion());
				message.put(ProductDao.TITLECOLUMN, prod.getTitle());
				message.put(ProductDao.TYPECOLUMN, prod.getType());
				message.put(ProductDao.ALTERNATECOLUMN, prod.getAlternate() != null ? prod.getAlternate() : "");
			}else{
				message.put("Message", FAILURE_RESULT);
			}
		} catch (ClassNotFoundException | SQLException e) {			
			e.printStackTrace();
			message.put("Message", FAILURE_RESULT);
		}
		//logger.debug("Rerult: " + message);
		relt.append("Product", message);
		String jsonOutput = "" + relt.toString(4);
		return Response.status(200).entity(jsonOutput).build();
	}
	@Path("/prodwithstatus")
    @GET
    @Produces("application/json")
	public Response getProductsWithLatestStatus() throws JSONException {
		 
        JSONObject jsonProducts = new JSONObject();
        
        JSONObject jsonProd = new JSONObject();
        
        ProductDao prod;
		try {
			prod = new ProductDao();
			List<Product> prods = prod.getProductsWithStatus();
			logger.info("number of products with status: "  + prods.size());
			Iterator<Product> itr = prods.iterator();
			int count = 1;
			
			while(itr.hasNext()) {
		         Product p = itr.next();
		         logger.debug("Product " + count + ":\n " + p.getIdentifier() + " : " + p.getTitle());
		         
		         jsonProd = new JSONObject();
		         jsonProd.put(ProductDao.IDENTIFIERCOLUMN, p.getIdentifier());
		         jsonProd.put(ProductDao.VERSIONCOLUMN, p.getVersion());
		         jsonProd.put(ProductDao.TITLECOLUMN, p.getTitle());
		         jsonProd.put(ProductDao.TYPECOLUMN, p.getType());
		         jsonProd.put(ProductDao.ALTERNATECOLUMN, p.getAlternate() != null ? p.getAlternate() : "");
		         //logger.debug("AStatus " + p.getAStatus());
		         jsonProd.put("astatus", p.getAStatus());
		         jsonProd.put("cstatus", p.getCStatus());
		         jsonProd.put("nssdca", p.getNssdca());

		         jsonProducts.append("products", jsonProd);
		         count++;
		    }
			
		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
        String result = "" + jsonProducts.toString(4);
        return Response.status(200).entity(result).build();
    }
}
