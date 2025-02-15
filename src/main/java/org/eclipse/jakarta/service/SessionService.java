package org.eclipse.jakarta.service;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;

@SessionScoped
public class SessionService implements Serializable {
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
