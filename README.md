# Userservice REST Client (Java)

##Description

This application uses [Jersey](https://jersey.java.net/) and [JAXB](https://jaxb.java.net/) to provide an implementation of the [Userservice REST API](https://www.igniterealtime.org/projects/openfire/plugins/userservice/readme.html) in Java.

##Components

###Userservice REST Client (Java) Library

The OpenlinkJava library itself is comprised of one main file: `userservice.jar`.

If managing your own dependencies simply use `userservice.jar` otherwise use `userservice-jar-with-dependencies.jar`.

More information about Babbler can be found in the [Userservice REST API documentation](https://www.igniterealtime.org/projects/openfire/plugins/userservice/readme.html). But the short summary is to simply drop the files into your application and use `UserServiceClient.java` to invoke your User Service commands.

###Userservice Java Test Examples

The Userservice Java Test Example consists of the file `UserServiceClientTest.java`. Configure the required service parameters and then run the scenarios to trigger the user service commands.

##Getting started
If you haven’t already done so, install Maven. 

Run `mvn clean install` in your console to install the dependencies and build the package.

###Layout

The items required to use the application can all be found in the `src` folder. It follows the typical Maven layout.

###Application Arguments

The applications arguments are:
* `domain`: The XMPP/Userservice domain.
* `port`: Service port (9090-HTTP / 9091-HTTPS).
* `ssl`: SSL Enabled/Disabled.
* `username`: Username.
* `password`: Password.


##Application Guide

Whilst the `UserServiceClientTest` file is only a guide it follows the standard structure for building client applications.

###Instantiate
	UserServiceClient client = new UserServiceClient(DOMAIN, PORT, SSL, USERNAME, PASSWORD);
	
###Execute
	client.getUsers();
	
###Disconnect
    client.destroy();

##License

`This file is subject to the terms and conditions defined 3in file `LICENSE`, which is part of this source code package.`
