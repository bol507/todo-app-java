package org.eclipse.jakarta.entity;

//import java.util.ArrayList;
//import java.util.Collection;

import jakarta.persistence.Entity;
//import jakarta.persistence.OneToMany;

@Entity
public class UserEntity extends AbstractEntity {

  private String email;
  private String password;
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
