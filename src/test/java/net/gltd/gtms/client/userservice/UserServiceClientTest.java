package net.gltd.gtms.client.userservice;

import junit.framework.Assert;
import net.gltd.gtms.client.userservice.users.User;
import net.gltd.gtms.client.userservice.users.Users;
import net.gltd.util.log.GtmsLog;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserServiceClientTest {

	protected Logger logger = Logger.getLogger("net.gltd.gtms");

	public static final String DOMAIN = "example.com";
	public static final int PORT = 9090;
	public static final boolean SSL = false;
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "password";

	private UserServiceClient client;

	@Before
	public void initialize() throws Exception {
		logger = GtmsLog.initializeConsoleLogger("net.gltd.gtms", GtmsLog.DEFAULT_DEBUG_CONVERSION_PATTERN, "DEBUG");
		client = new UserServiceClient(DOMAIN, PORT, SSL, USERNAME, PASSWORD);
	}

	@After
	public void shutdown() throws Exception {
		logger.debug("SHUTDOWN");
		if (client != null) {
			client.destroy();
		}
		LogManager.shutdown();
	}

	@Test
	public void testListAllUsers() throws Exception {
		Users users = this.client.getUsers();
		Assert.assertTrue(users.size() > 0);
		for (User u : users) {
			Assert.assertTrue(u.getUsername() != null && !"".equals(u.getUsername()));
			logger.debug(u);
		}
	}
}
