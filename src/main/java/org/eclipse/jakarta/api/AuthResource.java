package org.eclipse.jakarta.api;

import org.eclipse.jakarta.config.SecurityFilter;
import org.eclipse.jakarta.service.PersistenceService;
import org.eclipse.jakarta.service.SecurityService;
import org.eclipse.jakarta.service.SessionService;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

  @Inject PersistenceService persistenceService;

  @Inject
  private SecurityService securityService;

  @Inject
  private SessionService sessionService;

  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response login(
    @NotEmpty(message = "Email field must be set") @FormParam("email") String email, 
    @NotEmpty(message = "Password field must be set") @FormParam("password") String password
  ){
    if(!securityService.authenticateUser(email, password)){
      throw new SecurityException("Email or password is invalid");
    }
    String token = securityService.generateToken(email);
    sessionService.setEmail(email);

    return Response
    .ok()
    .header(AUTHORIZATION, SecurityFilter.BEARER + token)
    .build();
  }

  


}
