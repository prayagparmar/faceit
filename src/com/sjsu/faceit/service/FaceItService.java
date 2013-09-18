package com.sjsu.faceit.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/service")
public interface FaceItService {
	
	@Path("/signup")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response signUpUser(String xmlString);
	
	@Path("/login/username/{username}/password/{password}")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response loginUser(@PathParam("username") String userName, @PathParam("password") String password, String loginXMLData);
	
	@Path("/deleteuser/username/{username}")
	@DELETE
	public Response deleteUser(@PathParam("username") String userName);
	
	@Path("/resetpassword/username/{username}")
	@GET
	public Response resetPassword(@PathParam("username") String userName);
	
	@Path("/resetpassword/username/{username}/securityanswer/{securityanswer}")
	@GET
	public Response resetPasswordAnswer(@PathParam("username") String userName, @PathParam("securityanswer") String securityanswer);
	
}
