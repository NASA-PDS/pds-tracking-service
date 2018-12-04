/**
 * Copyright 2010-2017, by the California Institute of Technology.
 */
package gov.nasa.pds.tracking.tracking.xmlinterfaces;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.nasa.pds.tracking.tracking.db.Reference;
import gov.nasa.pds.tracking.tracking.db.ReferenceDao;

/**
 * @author danyu dan.yu@jpl.nasa.gov
 *
 */
@Path("xml/references")
public class XMLBasedRefernces {
	
	public static Logger logger = Logger.getLogger(XMLBasedRefernces.class);

	private ReferenceDao rD;
	
	@GET
    @Produces("application/xml")
    public Response defaultReferences() {
        
		StringBuffer xmlOutput = new StringBuffer();
		
		ReferenceDao refD;
		try {
											
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder refBuilder;

            refBuilder = factory.newDocumentBuilder();
            Document doc = refBuilder.newDocument();
            Element rootElement = doc.createElement("References");
            
            //Instrument Reference
			refD = new ReferenceDao();
			List<Reference> refInsts = refD.getProductAllReferences(ReferenceDao.INST_TABLENAME);
						
			logger.info("number of Instrument Reference: "  + refInsts.size());
            
			if (refInsts.size() > 0){
				
	            Iterator<Reference> itr = refInsts.iterator();
				int countInst = 1;	 			
				while(itr.hasNext()) {
					Reference r = itr.next();
					logger.debug("instrument Reference " + countInst + ":\n " + r.getLog_identifier() + " : " + r.getReference());
			        
					Element instElement = doc.createElement("Instrument");
					
		            Element idElement = doc.createElement(ReferenceDao.LOG_IDENTIFIERCOLUMN);
		            idElement.appendChild(doc.createTextNode(r.getLog_identifier()));
		            instElement.appendChild(idElement);
		            
		            Element refElement = doc.createElement(ReferenceDao.REFERENCECOLUMN);
		            refElement.appendChild(doc.createTextNode(r.getReference()));
		            instElement.appendChild(refElement);
		            
		            Element titleElement = doc.createElement(ReferenceDao.TITLECOLUMN);
		            titleElement.appendChild(doc.createTextNode(r.getTitle()));
		            instElement.appendChild(titleElement);
		            
		            rootElement.appendChild(instElement);

		            countInst++;
				}
			}
				
			//investigation_reference
			refD = new ReferenceDao();
			List<Reference> refInves = refD.getProductAllReferences(ReferenceDao.INVES_TABLENAME);
						
			logger.info("number of Investigation Reference: "  + refInves.size());
            
			if (refInves.size() > 0){
				
				Iterator<Reference> itr = refInves.iterator();
				int countInve = 1;	 			
				while(itr.hasNext()) {
					Reference r = itr.next();
					logger.debug("Investigation Reference " + countInve + ":\n " + r.getLog_identifier() + " : " + r.getReference());
			        
					Element investElement = doc.createElement("Investigation");
					
		            Element idElement = doc.createElement(ReferenceDao.LOG_IDENTIFIERCOLUMN);
		            idElement.appendChild(doc.createTextNode(r.getLog_identifier()));
		            investElement.appendChild(idElement);
		            
		            Element refElement = doc.createElement(ReferenceDao.REFERENCECOLUMN);
		            refElement.appendChild(doc.createTextNode(r.getReference()));
		            investElement.appendChild(refElement);
		            
		            Element titleElement = doc.createElement(ReferenceDao.TITLECOLUMN);
		            titleElement.appendChild(doc.createTextNode(r.getTitle()));
		            investElement.appendChild(titleElement);

		            rootElement.appendChild(investElement);

		            countInve++;
				}
			}	
			//node_reference
			refD = new ReferenceDao();
			List<Reference> refNodes = refD.getProductAllReferences(ReferenceDao.NODE_TABLENAME);
						
			logger.info("number of Node Reference: "  + refNodes.size());
            
			if (refNodes.size() > 0){
				
				Iterator<Reference> itr = refNodes.iterator();
				int countNode = 1;	 			
				while(itr.hasNext()) {
					Reference r = itr.next();
					logger.debug("Node Reference " + countNode + ":\n " + r.getLog_identifier() + " : " + r.getReference());					
			        
					Element nodeElement = doc.createElement("Node");
					
		            Element idElement = doc.createElement(ReferenceDao.LOG_IDENTIFIERCOLUMN);
		            idElement.appendChild(doc.createTextNode(r.getLog_identifier()));
		            nodeElement.appendChild(idElement);
		            
		            Element refElement = doc.createElement(ReferenceDao.REFERENCECOLUMN);
		            refElement.appendChild(doc.createTextNode(r.getReference()));
		            nodeElement.appendChild(refElement);
		            
		            Element titleElement = doc.createElement(ReferenceDao.TITLECOLUMN);
		            titleElement.appendChild(doc.createTextNode(r.getTitle()));
		            nodeElement.appendChild(titleElement);

		            rootElement.appendChild(nodeElement);

		            countNode++;
				}
			}	
			doc.appendChild(rootElement);
			
			DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, result);
            
            logger.debug("References:\n" + writer.toString());
            
			xmlOutput.append(writer.toString());
			
		} catch (ParserConfigurationException ex) {
			logger.error(ex);
        } catch (TransformerConfigurationException ex) {
        	logger.error(ex);
        }catch (TransformerException ex) {
        	logger.error(ex);
        } catch (ClassNotFoundException ex) {
        	logger.error(ex);
		} catch (SQLException ex) {
			logger.error(ex);
		}

        return Response.status(200).entity(xmlOutput.toString()).build();
    }

	@Path("{id : (.+)?}/{refType : (.+)?}")
    @GET
    @Produces("application/xml")
	public Response getReferences(@PathParam("id") String id, @PathParam("refType") String refTableName){
		
		StringBuffer xmlOutput = new StringBuffer();
		
		ReferenceDao refD;
		try {
											
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder refBuilder;

            refBuilder = factory.newDocumentBuilder();
            Document doc = refBuilder.newDocument();
            Element rootElement = doc.createElement(refTableName);
            
            
            
            refD = new ReferenceDao();
			List<Reference> refs = refD.getProductReferences(id, refTableName);
						
			logger.info("number of " + refTableName + ": "  + refs.size());
            
			if (refs.size() > 0){
				
				Iterator<Reference> itr = refs.iterator();
				int count = 1;	 			
				while(itr.hasNext()) {
					Reference r = itr.next();
					logger.debug(refTableName + " " + count + ":\n " + r.getLog_identifier() + " : " + r.getReference());					
					
		            Element idElement = doc.createElement(ReferenceDao.LOG_IDENTIFIERCOLUMN);
		            idElement.appendChild(doc.createTextNode(r.getLog_identifier()));
		            rootElement.appendChild(idElement);
		            
		            Element refElement = doc.createElement(ReferenceDao.REFERENCECOLUMN);
		            refElement.appendChild(doc.createTextNode(r.getReference()));
		            rootElement.appendChild(refElement);
		            
		            Element titleElement = doc.createElement(ReferenceDao.TITLECOLUMN);
		            titleElement.appendChild(doc.createTextNode(r.getTitle()));
		            rootElement.appendChild(titleElement);

		            count++;
				}
			}	
			doc.appendChild(rootElement);
			
			DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, result);
            
            logger.debug(refTableName + ":\n" + writer.toString());
            xmlOutput.append(writer.toString());
            
		} catch (ParserConfigurationException ex) {
			logger.error(ex);
        } catch (TransformerConfigurationException ex) {
        	logger.error(ex);
        }catch (TransformerException ex) {
        	logger.error(ex);
        } catch (ClassNotFoundException ex) {
        	logger.error(ex);
		} catch (SQLException ex) {
			logger.error(ex);
		}

        return Response.status(200).entity(xmlOutput.toString()).build();
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response createReference(@FormParam("LogicalIdentifier") String log_id, 
			@FormParam("Reference") String ref, 
			@FormParam("Title") String title, 
			@FormParam("referenceType") String refType) throws IOException{
		
		StringBuffer xmlOutput = new StringBuffer();
		
		try {
			rD = new ReferenceDao();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder refBuilder;

            refBuilder = factory.newDocumentBuilder();
            Document doc = refBuilder.newDocument();
            Element rootElement = doc.createElement("reference");
            
			Reference refObj = new Reference(log_id, ref, title, refType);
			int result = rD.insertReference(refObj);
			
			if(result == 1){
				Element idElement = doc.createElement(ReferenceDao.LOG_IDENTIFIERCOLUMN);
				idElement.appendChild(doc.createTextNode(log_id));
	            rootElement.appendChild(idElement);
	            
	            Element refElement = doc.createElement(ReferenceDao.REFERENCECOLUMN);
	            refElement.appendChild(doc.createTextNode(ref));
	            rootElement.appendChild(refElement);
	            
	            Element titleElement = doc.createElement(ReferenceDao.TITLECOLUMN);
	            titleElement.appendChild(doc.createTextNode(title));
	            rootElement.appendChild(titleElement);
		    }else{
			
				Element messageElement = doc.createElement("Message");
				messageElement.appendChild(doc.createTextNode("Add reference for " + log_id  + " failure!"));
				rootElement.appendChild(messageElement);
			}
		doc.appendChild(rootElement);
		
		DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult strResult = new StreamResult(writer);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(domSource, strResult);
        
        logger.debug("Reference:\n" + writer.toString());
        
		xmlOutput.append(writer.toString());
        
	} catch (ParserConfigurationException ex) {
		logger.error(ex);
    } catch (TransformerConfigurationException ex) {
    	logger.error(ex);
    }catch (TransformerException ex) {
    	logger.error(ex);
    } catch (ClassNotFoundException ex) {
    	logger.error(ex);
	} catch (SQLException ex) {
		logger.error(ex);
	}

		return Response.status(200).entity(xmlOutput.toString()).build();
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)	
	public Response updateReference(@FormParam("LogicalIdentifier") String log_id, 
			@FormParam("Reference") String ref, 
			@FormParam("Title") String title, 
			@FormParam("referenceType") String refType) throws IOException{
		
		StringBuffer xmlOutput = new StringBuffer();
		
		try {
			rD = new ReferenceDao();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder refBuilder;

            refBuilder = factory.newDocumentBuilder();
            Document doc = refBuilder.newDocument();
            Element rootElement = doc.createElement("Reference");
            
            Reference refObj = new Reference(log_id, ref, title, refType);
            Reference updatedRef = rD.updateReference(refObj);
			
			if(updatedRef != null){
				Element idElement = doc.createElement(ReferenceDao.LOG_IDENTIFIERCOLUMN);
				idElement.appendChild(doc.createTextNode(updatedRef.getLog_identifier()));
	            rootElement.appendChild(idElement);
	            
	            Element refElement = doc.createElement(ReferenceDao.REFERENCECOLUMN);
	            refElement.appendChild(doc.createTextNode(updatedRef.getReference()));
	            rootElement.appendChild(refElement);
	            
	            Element titleElement = doc.createElement(ReferenceDao.TITLECOLUMN);
	            titleElement.appendChild(doc.createTextNode(updatedRef.getTitle()));
	            rootElement.appendChild(titleElement);
		    }else{
			
				Element messageElement = doc.createElement("Message");
				messageElement.appendChild(doc.createTextNode("Updated reference for " + log_id  + " failure!"));
				rootElement.appendChild(messageElement);
			}
			doc.appendChild(rootElement);
			
			DOMSource domSource = new DOMSource(doc);
	        StringWriter writer = new StringWriter();
	        StreamResult strResult = new StreamResult(writer);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.transform(domSource, strResult);
	        
	        logger.debug("Updated Reference:\n" + writer.toString());
	        
			xmlOutput.append(writer.toString());
        
		} catch (ParserConfigurationException ex) {
			logger.error(ex);
	    } catch (TransformerConfigurationException ex) {
	    	logger.error(ex);
	    }catch (TransformerException ex) {
	    	logger.error(ex);
	    } catch (ClassNotFoundException ex) {
	    	logger.error(ex);
		} catch (SQLException ex) {
			logger.error(ex);
		}
	
	    return Response.status(200).entity(xmlOutput.toString()).build();
	}
}
