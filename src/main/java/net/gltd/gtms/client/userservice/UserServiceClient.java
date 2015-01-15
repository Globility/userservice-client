package net.gltd.gtms.client.userservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.gltd.gtms.client.userservice.jaxb.XmppUtils;
import net.gltd.gtms.client.userservice.users.User;
import net.gltd.gtms.client.userservice.users.Users;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class UserServiceClient {
	private static final Logger logger = Logger.getLogger(UserServiceClient.class);

	private final String username;
	private final String password;

	private final String domain;
	private final int port;

	private Client client;

	private boolean ssl = false;
	private Unmarshaller unmarshaller;

	private Marshaller marshaller;

	public UserServiceClient(String domain, int port, boolean ssl, String username, String password) throws JAXBException, XMLStreamException {
		this.username = username;
		this.password = password;
		this.domain = domain;
		this.port = port;
		this.ssl = ssl;

		client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(this.username, this.password));

		logger.debug("Client Initialized with: " + getBaseUrl() + " for user: " + username);
		this.initializeJaxb(Users.class, User.class);
	}

	public String getBaseUrl() {
		String url = "";
		if (ssl) {
			url += "https://";
		} else {
			url += "http://";
		}
		url += this.domain + ":" + this.port + "/" + "plugins/userService/";
		return url;
	}

	public Users getUsers() throws UserServiceException {
		Users users = new Users();
		try {
			String url = getBaseUrl() + "users";

			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept("application/xml").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new UserServiceException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
			users = unmarshal(output, Users.class);
		} catch (Exception e) {
			throw new UserServiceException(e);
		}
		return users;
	}

	public void destroy() {
		logger.debug("DESTROY");
		if (this.client != null) {
			client.destroy();
		}
	}

	public void initializeJaxb(Class<?>... context) throws JAXBException, XMLStreamException {
		JAXBContext jaxbContext = JAXBContext.newInstance(context);
		unmarshaller = jaxbContext.createUnmarshaller();
		marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	}

	private static XMLEventReader getStream(String stanza) throws XMLStreamException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		printStream.print(stanza);

		InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

		XMLEventReader xmlEventReader = XMLInputFactory.newFactory().createXMLEventReader(inputStream);
		xmlEventReader.nextEvent();
		return xmlEventReader;
	}

	protected <T> T unmarshal(String xml, Class<T> type) throws XMLStreamException, JAXBException {
		XMLEventReader xmlEventReader = getStream(xml);
		return unmarshaller.unmarshal(xmlEventReader, type).getValue();
	}

	protected String marshal(Object object) throws XMLStreamException, JAXBException {
		Writer writer = new StringWriter();

		XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(writer);

		XMLStreamWriter prefixFreeWriter = XmppUtils.createXmppStreamWriter(xmlStreamWriter, true);
		marshaller.marshal(object, prefixFreeWriter);
		return writer.toString();
	}

}
