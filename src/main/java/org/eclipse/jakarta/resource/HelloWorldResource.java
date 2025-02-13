package org.eclipse.jakarta.resource;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("hello")
public class HelloWorldResource {

  @Path("{name}")
  @GET
  public JsonObject greet(@PathParam("name") String name) {
    return Json.createObjectBuilder().add( "name",name)
      .add("greeting", "Hello, " + name)
      .add("message","Welcome to the world of RESTful services").build();
  }

}
