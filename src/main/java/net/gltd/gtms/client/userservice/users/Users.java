package net.gltd.gtms.client.userservice.users;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "users")
public class Users extends HashSet<User> {

	private static final long serialVersionUID = 2334485039832928546L;

	@XmlElement(name = "user")
	public Set<User> getUsers() {
		return this;
	}

	public User getUserByUsername(String username) {
		for (User u : this) {
			if (username.equals(u.getUsername())) {
				return u;
			}
		}
		return null;
	}
}
