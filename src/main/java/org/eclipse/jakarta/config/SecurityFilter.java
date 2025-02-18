package org.eclipse.jakarta.config;

import java.io.IOException;
import java.security.Key;

import org.eclipse.jakarta.security.CustomSecurityContext;
import org.eclipse.jakarta.service.SecurityService;
import org.eclipse.jakarta.service.SessionService;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Provider
@SecureAuth
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
  public static final String BEARER = "Bearer ";

  @Inject
  private SecurityService securityService;

  @Inject
  private SessionService sessionService;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String authString = requestContext.getHeaderString(AUTHORIZATION);
    if (authString == null || authString.isEmpty()  || !authString.startsWith("Bearer ")) {

      JsonObject jsonObject = Json.createObjectBuilder()
        .add("error", "Authorization header is missing or invalid")
        .build();

      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
        .entity(jsonObject.toString())
        .build());

      return;
    }

    String token = authString.substring(BEARER.length()).trim();
    try{
      Key key = securityService.generateKey(sessionService.getEmail());
      DecodedJWT jwt = securityService.validateTokenAndDecodeToken(token, key);
      if (jwt == null) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            .entity("Invalid token or token has expired")
            .build());
        return;
      }
      String userEmail = jwt.getClaim("email").asString();
      SecurityContext originalSecurityContext = requestContext.getSecurityContext();
      SecurityContext securityContext = new CustomSecurityContext(userEmail, originalSecurityContext);
      requestContext.setSecurityContext(securityContext);

    }catch (Exception e){
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
  }
}
