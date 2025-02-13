package org.eclipse.jakarta.entity;

import jakarta.persistence.Column;

//import java.util.ArrayList;
//import java.util.Collection;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
//import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class UserEntity extends AbstractEntity {

  @Column(length = 100)
  @NotEmpty(message="An email must be set")
  @Email(message="Email must be in the format user@somedomain.com")
  private String email;

  @NotBlank(message="Password cannot be empty")
  @Size(min=8, max=100, message="Password must be a min of 8 and max of 100 characters")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\$%&#!]).{8,100}$", message="Password must have at least one upper case, " + "one lower case, and must contain at least one of $%&#!")
  private String password;

  @NotEmpty(message="Name must be set")
  @Size(min=2, max=100, message="Name must be a min of 2 and max 100 characters ")
  private String fullName;

  //@OneToMany
  //private final Collection<TodoEntity> todos = new ArrayList<>();

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
}
