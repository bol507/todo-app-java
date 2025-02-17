package org.eclipse.jakarta.api;

import org.eclipse.jakarta.service.PersistenceService;
import org.eclipse.jakarta.service.QueryService;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

  @Inject PersistenceService persistenceService;

  @Inject QueryService queryService;

  @POST
  @Path("login")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response login(
    @NotEmpty(message = "Email field must be set") @FormParam("email") String email, 
    @NotEmpty(message = "Password field must be set") @FormParam("password") String password
  ){
    
    
    return Response
    .ok()
    .build();
  }


}
