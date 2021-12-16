package edu.upf.taln.welcome.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;

@Provider
public class WelcomeExceptionMapper implements ExceptionMapper<WelcomeException>{

	@Override
	public Response toResponse(WelcomeException exception) {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
}
