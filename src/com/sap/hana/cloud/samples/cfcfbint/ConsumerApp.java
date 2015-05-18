package com.sap.hana.cloud.samples.cfcfbint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



import com.sap.hana.cloud.samples.cfcfbint.persistence.MarketingLead;
import com.sap.hana.cloud.samples.cfcfbint.persistence.MarketingLeadBean;



/**
 * Servlet implementation class ConsumerApp
 * This application receives facebook call back events and creates marketing lead in 
 */
@WebServlet("/FBCallBackApp/")
public class ConsumerApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String USERNAME = null;
	
	private static String VERIFY_TOKEN = null;
	private static String APP_ACCESS_TOKEN = null;
	private static String PAGE_ACCESS_TOKEN = null;
	private static String PASSWORD = null;
	private static String SERVICE_URL = null;

	ServletContext context;
	ArrayList<String> arrl = new ArrayList<String>();
	Properties prop = new Properties();

	static Logger LOGGER = LoggerFactory.getLogger(ConsumerApp.class);
	
	@EJB
    private MarketingLeadBean marketingLeadBean;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConsumerApp() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		context = getServletContext();
		try {
			String path = getServletContext()
					.getInitParameter("PropertiesFile");
			final InputStream is = getServletContext()
					.getResourceAsStream(path);
			try {
				prop.load(is);
				VERIFY_TOKEN = prop.getProperty("VERIFY_TOKEN");
				APP_ACCESS_TOKEN = prop.getProperty("APP_ACCESS_TOKEN");
				PAGE_ACCESS_TOKEN = prop.getProperty("PAGE_ACCESS_TOKEN");
				USERNAME = prop.getProperty("USERNAME");
				PASSWORD = prop.getProperty("PASSWORD");
				SERVICE_URL = prop.getProperty("SERVICE_URL");

				// To check if properties are loaded correctly
				LOGGER.debug(SERVICE_URL);
			} finally {
				is.close();
			}
		} catch (Exception asd) {
			LOGGER.error(asd.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _mode = request.getParameter("hub.mode");
		String _token = request.getParameter("hub.verify_token");

		if (_mode.equalsIgnoreCase("subscribe") && _token.equals(VERIFY_TOKEN)) {
			LOGGER.error("Facebook Call back received");
			response.setContentType("text/plain");
			response.getWriter().print(request.getParameter("hub.challenge"));

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOGGER.info("Callback received from facebook");
		
		try {
			JsonReader jsonReader = Json.createReader(request.getInputStream());
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();

			LOGGER.info("Call back Data  : " + jsonObject.toString());

			JsonArray jsonArray = jsonObject.getJsonArray("entry");

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject changeobject = jsonArray.getJsonObject(i);
				JsonArray jsonArrayChange = changeobject
						.getJsonArray("changes");
				for (int j = 0; j < jsonArrayChange.size(); j++) {
					JsonObject innerJsonObject = jsonArrayChange
							.getJsonObject(j);

					JsonObject valObject = innerJsonObject
							.getJsonObject("value");
					if (valObject.getJsonString("item").getString()
							.equals("like")) {

						String parent_id = valObject.getJsonString("parent_id")
								.getString().split("_")[1];

						String urlString = "https://graph.facebook.com/v2.0/"
								+ parent_id + "/likes?access_token="
								+ PAGE_ACCESS_TOKEN;
						LOGGER.debug(urlString);

						jsonReader = Json
								.createReader(callFBGraphAPI(urlString));
						JsonObject jsonappscopedidObject = jsonReader
								.readObject();
						LOGGER.debug("User ID Data from Facebook : "
								+ jsonappscopedidObject.toString());
						jsonReader.close();

						JsonArray jsonArrayappscopedid = jsonappscopedidObject
								.getJsonArray("data");

						for (int k = 0; k < jsonArrayappscopedid.size(); k++) {
							JsonObject JsonObjectData = jsonArrayappscopedid
									.getJsonObject(k);

							String user_app_scoped_id = JsonObjectData
									.getJsonString("id").getString();
							if (!arrl.contains(parent_id + "_"
									+ user_app_scoped_id)) {
								urlString = "https://graph.facebook.com/v2.0/"
										+ user_app_scoped_id + "?access_token="
										+ APP_ACCESS_TOKEN;
								LOGGER.debug(urlString);
								jsonReader = Json
										.createReader(callFBGraphAPI(urlString));
								JsonObject jsonuserdetailObject = jsonReader
										.readObject();
								LOGGER.debug("User details Data from Facebook : "
										+ jsonuserdetailObject.toString());
								jsonReader.close();

								String usrmail = jsonuserdetailObject
										.getJsonString("email").getString();
								String usrfname = jsonuserdetailObject
										.getJsonString("first_name")
										.getString();
								String usrlname = jsonuserdetailObject
										.getJsonString("last_name").getString();
								String usrgender = jsonuserdetailObject
										.getJsonString("gender").getString();
								String marketingleadid = CreateMarketingLead(
										usrfname, usrlname, usrmail, usrgender,
										parent_id + "_" + user_app_scoped_id);
								LOGGER.error("Marketing Lead ID : "
										+ marketingleadid);
								//Persist into HCP database
								persistLead(usrfname, usrlname, usrmail, usrgender,
										parent_id + "_" + user_app_scoped_id,
										marketingleadid);

								
							}
						}

					}
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
		}
	}
	
	public InputStream callFBGraphAPI(String urlString) throws Exception {
		LOGGER.debug("Inside callFBGraphAPI");
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		return conn.getInputStream();

	}
	
	private String CreateMarketingLead(String usrfname, String usrlname,
			String usrmail, String usrgender, String user_app_scoped_id) {
		LOGGER.debug("Inside CreateMarketingLead");

		SOAPConnectionFactory soapConnectionFactory = null;
		SOAPConnection soapConnection = null;
		try {
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();

			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			String soapaction = "http://sap.com/xi/A1S/Global/ManageMarketingLeadIn/MaintainBundleRequest";
			String ns = "http://sap.com/xi/SAPGlobal20/Global";
			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("glob", ns);

			// SOAP Body
			SOAPBody soapBody = envelope.getBody();
			// MarketingLeadBundleMaintainRequest_sync
			SOAPElement marketingLeadBundleMaintainRequestsync = soapBody
					.addChildElement("MarketingLeadBundleMaintainRequest_sync",
							"glob");
			SOAPElement MarketingLead = marketingLeadBundleMaintainRequestsync
					.addChildElement("MarketingLead");
			MarketingLead.setAttribute("actionCode", "01");
			MarketingLead.setAttribute("itemListCompleteTransmissionIndicator",
					"true");
			SOAPElement Name = MarketingLead.addChildElement("Name");
			Name.addTextNode(user_app_scoped_id);
			SOAPElement OriginTypeCode = MarketingLead
					.addChildElement("OriginTypeCode");
			OriginTypeCode.addTextNode("003");
			SOAPElement CompanyName = MarketingLead
					.addChildElement("CompanyName");
			CompanyName.addTextNode("Facebook");
			SOAPElement PersonFirstName = MarketingLead
					.addChildElement("PersonFirstName");
			PersonFirstName.addTextNode(usrfname);
			SOAPElement PersonLastName = MarketingLead
					.addChildElement("PersonLastName");
			PersonLastName.addTextNode(usrlname);
			SOAPElement PersonGenderCode = MarketingLead
					.addChildElement("PersonGenderCode");
			if (usrgender.equals("male"))
				PersonGenderCode.addTextNode("1");
			else if (usrgender.equals("female"))
				PersonGenderCode.addTextNode("2");
			else
				PersonGenderCode.addTextNode("0");

			SOAPElement Address = MarketingLead.addChildElement("Address");
			SOAPElement EmailURI = Address.addChildElement("EmailURI");
			EmailURI.addTextNode(usrmail);
			// End SOAP Body

			// Set SOAP headers
			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction", soapaction);
			String authorization = USERNAME + ":" + PASSWORD;
			byte[] encoded = Base64.encodeBase64(authorization.getBytes());
			headers.addHeader("Authorization", "Basic " + new String(encoded));
			soapMessage.saveChanges();
			LOGGER.debug("Calling End Point : "+SERVICE_URL);
			SOAPMessage soapResponse = soapConnection.call(soapMessage,
					SERVICE_URL);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			// loading the XML document
			DocumentBuilderFactory builderfactory = DocumentBuilderFactory
					.newInstance();
			builderfactory.setNamespaceAware(true);
			DocumentBuilder builder = builderfactory.newDocumentBuilder();
			// Document xmlDocument = null;

			Document xmlDocument = builder.parse(new InputSource(
					new StringReader(out.toString())));
			xmlDocument.getDocumentElement().normalize();

			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//ID";
			arrl.add(user_app_scoped_id);
			return (xPath.compile(expression).evaluate(xmlDocument));

		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}

	}
	
	private void persistLead(String usrfname, String usrlname, String usrmail,
			String usrgender, String string, String marketingleadid) {
		// TODO Auto-generated method stub
		LOGGER.info("Inside persistLead");
		Long leadid = Long.parseLong(marketingleadid);
		if (usrfname != null && usrlname != null && !usrfname.trim().isEmpty()
				&& !usrlname.trim().isEmpty()) {
			MarketingLead marketingLead = new MarketingLead();
			marketingLead.setLeadid(leadid);
        	marketingLead.setFirstName(usrfname.trim());
        	marketingLead.setLastName(usrlname.trim());
        	marketingLeadBean.addMarketingLead(marketingLead);

			LOGGER.info("Added the data to the database: " + usrfname);
		}

	}

}
