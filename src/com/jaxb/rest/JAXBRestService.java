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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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

			convertXMLToObject();

		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error parsing: -" + e.getMessage());
		}
		System.out.println("Received: " + builder.toString());

		return Response.status(200).entity(builder.toString()).build();
	}

	public void convertXMLToObject() {

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse(new File("C:\\Users\\Baran.Topal\\Documents\\your.xml"));

			// remove /Personnels for fun

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			XPathExpression expression = xpath.compile("/Personnels");

			Node personnelsNode = (Node) expression.evaluate(document, XPathConstants.NODE);

			NodeList nodeList = personnelsNode.getChildNodes();

			personnelsNode .getParentNode().removeChild(personnelsNode);

			DocumentBuilderFactory dbfNew = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document newXmlDocument = dbfNew.newDocumentBuilder().newDocument();

			// add a new root as employees

			Element root = newXmlDocument.createElement("employees");
			newXmlDocument.appendChild(root);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Node copyNode = newXmlDocument.importNode(node, true);
				root.appendChild(copyNode);
			}

			DOMImplementationLS domImplementationLS = 
					(DOMImplementationLS) newXmlDocument.getImplementation();
			LSSerializer lsSerializer = 
					domImplementationLS.createLSSerializer();
			LSOutput lsOutput = domImplementationLS.createLSOutput();
			lsOutput.setEncoding("UTF-8");

			Writer stringWriter = new StringWriter();
			lsOutput.setCharacterStream(stringWriter);
			lsSerializer.write(newXmlDocument, lsOutput);     
			String result = stringWriter.toString();

			// be sure you remove BOM..
			String cleaned = removeUTF8BOM(result);
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("C:\\Users\\Baran.Topal\\Documents\\your2.xml"), StandardCharsets.UTF_8);
			writer.write(cleaned);

			writer.close();

			File file = new File("C:\\Users\\Baran.Topal\\Documents\\your2.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);

			// problem with unmarshalling from the file
			// TODO
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Employee employee = (Employee) jaxbUnmarshaller.unmarshal(file);
		
			System.out.println("baran" + employee.getAge());


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyJAXBRest(InputStream incomingData) {

		String result = "JAXB service is started successfully";
		return Response.status(200).entity(result).build();
	}
	public static final String UTF8_BOM = "\uFEFF";

	public static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}
}

