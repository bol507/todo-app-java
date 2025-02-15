package org.eclipse.jakarta.api;

import org.eclipse.jakarta.entity.UserEntity;
import org.eclipse.jakarta.service.PersistenceService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
public class UserResource {

  @Inject PersistenceService persistenceService;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser (UserEntity user) {
    try {
      persistenceService.saveUser(user);
      return Response.status(Response.Status.CREATED).entity(user).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                      .entity("Error creating user: " + e.getMessage())
                      .build();
    }

  }

}
