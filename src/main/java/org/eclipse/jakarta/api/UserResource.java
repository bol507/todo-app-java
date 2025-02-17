package org.eclipse.jakarta.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jakarta.entity.UserEntity;
import org.eclipse.jakarta.entity.dto.UserUpdateDTO;
import org.eclipse.jakarta.service.PersistenceService;
import org.eclipse.jakarta.service.QueryService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

  @Inject PersistenceService persistenceService;

  @Inject QueryService queryService;



  @GET
  public Response listAllTodoUsers() {
    try{
      return Response.ok(queryService.findAllUsers()).build();
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Error creating user" + e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .type("application/json")
                    .build();
    }  
  }

  @POST  
  public Response createUser (@Valid UserEntity user) {
    try {
      persistenceService.saveUser(user);
      return Response.status(Response.Status.CREATED).entity(user).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                      .entity("Error creating user: " + e.getMessage())
                      .build();
    }

  }

  @PUT
  @Path("{id}")
  public Response updateUser(@PathParam("id") @NotNull Long id, @Valid UserUpdateDTO user){
    try{
      UserEntity updatedUser = persistenceService.updateUser(id, user);
      return Response.status(Response.Status.OK).entity(updatedUser).build();

    } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.NOT_FOUND)
                      .entity(e.getMessage()).build();
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Error updating user");

      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
      .entity(errorResponse)
      .type("application/json")
      .build();
     }

  }



   

}
