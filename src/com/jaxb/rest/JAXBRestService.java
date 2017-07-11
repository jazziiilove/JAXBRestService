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
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
		} catch(Exception e) {
			System.out.println("Error parsing: -");
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

