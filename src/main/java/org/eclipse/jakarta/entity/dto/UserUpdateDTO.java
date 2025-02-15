package org.eclipse.jakarta.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateDTO {
  @NotBlank(message="Full name cannot be empty")
  private String fullName;

  @Email(message="Invalid email format")
  private String email;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
      this.fullName = fullName;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

}
