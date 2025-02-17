package org.eclipse.jakarta.api;

import java.time.LocalDate;

import org.eclipse.jakarta.entity.TodoEntity;
import org.eclipse.jakarta.service.PersistenceService;
import org.eclipse.jakarta.service.QueryService;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/todo")
@Consumes("application/json")
@Produces("application/json")
public class TodoResource {

  @Inject PersistenceService persistenceService;

  @Inject QueryService queryService;

  @GET
  public Response getAll(){
    return Response.ok(queryService.findAllTodos()).build(); 
  }

  @GET
  @Path("/{id}")
  public Response getById(@PathParam("id") Long id){
    TodoEntity todo = queryService.findTodoById(id);
    return Response.ok(todo).build();
  }

  @GET
  public Response getByDueDate(@NotNull @QueryParam ("due-date") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date){
    LocalDate dueDate = LocalDate.parse(date);
    return Response.ok(queryService.getByDueDate(dueDate)).build(); 
  } 

  @GET
  @Path("completed")
  public Response getCompleted(){
    return Response.ok(queryService.getCompleted()).build(); 
  }

  @GET
  @Path("uncompleted")
  public Response getUncompleted(){
    return Response.ok(queryService.getUncompleted()).build(); 
  }

  @POST 
  public Response create(TodoEntity todo){
    persistenceService.saveTodo(todo);
    return Response.ok(todo).build();
  }

  
  @PATCH
  @Path("completed")
  public Response TodoAsCompleted(@NotNull @QueryParam ("id") Long id){
    queryService.todoAsCompleted(id);
    return Response.ok().build();
  }

  @PATCH
  @Path("archived")
  public Response TodoAsArchived(@NotNull @QueryParam ("id") Long id){
    queryService.todoAsArchived(id);
    return Response.ok().build();
  }
}
