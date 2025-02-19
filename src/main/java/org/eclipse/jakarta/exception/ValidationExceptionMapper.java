package org.eclipse.jakarta.exception;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    
    @Override
    public Response toResponse(ConstraintViolationException exception) {
      

      JsonObjectBuilder errorBuilder = Json.createObjectBuilder().add("error", "There were errors in the request");
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

      exception.getConstraintViolations().forEach(violation -> {
        String propertyName = violation.getPropertyPath().toString().split("\\.")[2];
        String errorMessage = violation.getMessage();
        
        objectBuilder.add(propertyName, errorMessage);
      });

      errorBuilder.add("validationErrors", objectBuilder);

      return Response.status(Response.Status.EXPECTATION_FAILED)
                   .entity(errorBuilder.build())
                   .type("application/json")
                   .build();
    }
}
