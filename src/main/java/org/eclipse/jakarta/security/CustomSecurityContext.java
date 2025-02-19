package org.eclipse.jakarta.security;

import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

public class CustomSecurityContext implements SecurityContext {
    private final String userEmail; 
    private final SecurityContext originalSecurityContext;


    public CustomSecurityContext(String userEmail, SecurityContext originalSecurityContext) {
        this.userEmail = userEmail;
        this.originalSecurityContext = originalSecurityContext;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> userEmail; 
    }

    @Override
    public boolean isUserInRole(String role) {
        return originalSecurityContext.isUserInRole(role);
    }

    @Override
    public boolean isSecure() {
        return originalSecurityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return originalSecurityContext.getAuthenticationScheme();
    }
}
