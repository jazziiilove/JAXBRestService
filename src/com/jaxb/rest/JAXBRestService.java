/* 																														*
 * Programmer: Baran Topal                   																			*
 * Project name: JAXBRestService       																					*
 * Folder name: src        																								*
 * Package name: com.jaxb.rest  																						*
 * File name: JAXBRestService.java                     																	*
 *                                           																			*      
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *	                                                                                         							*
 *  LICENSE: This source file is subject to have the protection of GNU General Public License.             				*
 *	You can distribute the code freely but storing this license information. 											*
 *	Contact Baran Topal if you have any questions. barantopal@barantopal.com 										    *
 *	                                                                                         							*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package com.jaxb.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@Path("/")
public class JAXBRestService {

	@POST
	@Path("/JAXBWebService")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response JAXBRest(InputStream incomingData) {

		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader in  = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;

			while((line = in.readLine()) != null) {
				builder.append(line);
			}

			JSONObject json = new JSONObject(new String(builder));
			String xmlContent = XML.toString(json);					
			
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder dbuilder = factory.newDocumentBuilder();
			    Document doc = dbuilder.parse(new InputSource(new StringReader(xmlContent)));

			    // Write the parsed document to an xml file
			    TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);

			    StreamResult result =  new StreamResult(new File("C:\\Users\\Baran.Topal\\Documents\\your.xml"));
			    transformer.transform(source, result);

		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error parsing: -" + e.getMessage());
		}
		System.out.println("Received: " + builder.toString());

		return Response.status(200).entity(builder.toString()).build();
	}

	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyJAXBRest(InputStream incomingData) {

		String result = "JAXB service is started successfully";
		return Response.status(200).entity(result).build();
	}

}

