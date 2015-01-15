package net.gltd.gtms.client.userservice;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.gltd.gtms.client.userservice.jaxb.XmlTest;
import net.gltd.gtms.client.userservice.users.User;
import net.gltd.gtms.client.userservice.users.Users;
import net.gltd.util.log.GtmsLog;
import net.gltd.util.string.StringUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UsersTest extends XmlTest {

	protected Logger logger = Logger.getLogger("net.gltd.gtms");

	public UsersTest() throws JAXBException, XMLStreamException {
		super(Users.class, User.class);
	}

	@Before
	public void initialize() throws Exception {
		logger = GtmsLog.initializeConsoleLogger("net.gltd.gtms", GtmsLog.DEFAULT_DEBUG_CONVERSION_PATTERN, "DEBUG");
	}

	public void shutdown() throws Exception {
		LogManager.shutdown();
	}

	@Test
	public void testUsersMarshal() throws XMLStreamException, JAXBException {
		Users users = new Users();
		User u1 = new User();
		u1.setEmail("leon@example.com");
		u1.setName("Leon Roy");
		u1.setUsername("leonroy");
		users.add(u1);

		User u2 = new User();
		u2.setEmail("brian.broker@exmaple.com");
		u2.setName("Brian Broker");
		u2.setUsername("brian.broker");
		users.add(u2);

		User u3 = new User();
		u3.setEmail("betty.bidder@example.com");
		u3.setName("Betty Bidder");
		u3.setUsername("betty.bidder");
		users.add(u3);

		String xml = marshal(users);

		logger.debug("XML: " + xml);

		Assert.assertTrue(xml.contains("<users><user>"));
		Assert.assertTrue(xml.contains("</user></users>"));
		Assert.assertTrue(xml.contains("<username>leonroy</username>"));
		Assert.assertTrue(xml.contains("<email>leon@example.com</email>"));
		Assert.assertTrue(xml.contains("<name>Leon Roy</name>"));

	}

	@Test
	public void testUsersUnmarshal() throws FileNotFoundException, XMLStreamException, JAXBException, IOException {
		String xmlIn = StringUtil.readFileAsString("users-all.xml");

		Users users = unmarshal(xmlIn, Users.class);

		String xmlOut = marshal(users);

		Assert.assertNotNull(xmlOut);
		logger.debug("XML: " + xmlIn);
		logger.debug("XML: " + xmlOut);

		Assert.assertEquals(3, users.size());

		User user;

		user = users.getUserByUsername("leon");
		Assert.assertNotNull(user);
		Assert.assertEquals("leon", user.getUsername());
		Assert.assertEquals("Leon Leon", user.getName());
		Assert.assertEquals("leon@gltd.net", user.getEmail());

		user = users.getUserByUsername("admin");
		Assert.assertNotNull(user);
		Assert.assertEquals("admin", user.getUsername());
		Assert.assertEquals("Administrator", user.getName());
		Assert.assertEquals("admin@example.com", user.getEmail());

		user = users.getUserByUsername("betty.bidder");
		Assert.assertNotNull(user);
		Assert.assertEquals("betty.bidder", user.getUsername());
		Assert.assertEquals("Betty Bidder", user.getName());
	}

}
