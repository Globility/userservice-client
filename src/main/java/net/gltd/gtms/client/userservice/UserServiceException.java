package net.gltd.gtms.client.userservice;

public class UserServiceException extends Exception {

	private static final long serialVersionUID = -6013748880417664399L;

	public UserServiceException() {
		super();
	}

	public UserServiceException(String msg) {
		super(msg);
	}

	public UserServiceException(Exception e) {
		super(e);
	}

}
