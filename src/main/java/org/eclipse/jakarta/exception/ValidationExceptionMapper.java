package org.eclipse.jakarta.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
      Map<String, String> validationErrors = new HashMap<>();
      exception.getConstraintViolations().forEach(violation -> {
        String propertyName = violation.getPropertyPath().toString();
            if (propertyName.contains(".")) {
                propertyName = propertyName.substring(propertyName.lastIndexOf('.') + 1);
            }
        String errorMessage = violation.getMessage();
        validationErrors.put(propertyName, errorMessage);
      });

      return Response.status(Response.Status.BAD_REQUEST)
                   .entity(validationErrors)
                   .type("application/json")
                   .build();
    }
}
