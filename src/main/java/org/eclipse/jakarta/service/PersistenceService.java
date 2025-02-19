package org.eclipse.jakarta.service;

import java.util.Map;



import org.eclipse.jakarta.entity.TodoEntity;
import org.eclipse.jakarta.entity.UserEntity;
import org.eclipse.jakarta.entity.dto.UserUpdateDTO;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@DataSourceDefinition(name = "todo-app-java", className = "org.sqlite.SQLiteDataSource", url = "jdbc:sqlite:sqlite.db")
@Stateless
public class PersistenceService {

  /*@Inject
  private SessionService sessionService;
  */
  @Inject
  private QueryService queryService;

  @Inject
  private SecurityService securityService;

  @Context
  private SecurityContext securityContext;

  @PersistenceContext
  EntityManager entityManager;

  private String userEmail;

  @PostConstruct
  public void init() {
    userEmail = securityContext.getUserPrincipal().getName();
  }

  public UserEntity saveUser(UserEntity user) {
    try{
      UserEntity existingUser = queryService.findUserByEmail(user.getEmail());

      if (existingUser != null) {
        throw new IllegalArgumentException("Unauthorized: User already exists");
      }
      Map<String, String> credentialMap = securityService.hashPassword(user.getPassword());
      if(user.getId() == null) {
        user.setPassword(credentialMap.get("hashedPassword"));
        user.setSalt(credentialMap.get("salt"));
        entityManager.persist(user);
      }
      credentialMap.clear();
      
      return user;
    } catch (IllegalArgumentException e) {
      throw e; 
    } catch (PersistenceException e) {
        throw new RuntimeException("Database error: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
    }
  }

  public TodoEntity saveTodo(TodoEntity todo) {

    
    UserEntity user = queryService.findUserByEmail(userEmail);

    if (todo.getId() == null && user != null) {
      todo.setTodoOwner(user);
      entityManager.persist(todo);
    } else {
      entityManager.merge(todo);
    }
    return todo;
  }

  public UserEntity updateUser(Long id, UserUpdateDTO user) {
    try {
      UserEntity existingUser = queryService.findUserById(id);
      if (existingUser == null) {
        throw new IllegalArgumentException("User not found with ID: " + id);
      }

      if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
        UserEntity existingEmail = queryService.findUserByEmail(user.getEmail());
        if (existingEmail != null) {
          throw new IllegalArgumentException("Email already in use: " + user.getEmail());
        }
      }

      existingUser.setFullName(user.getFullName());
      existingUser.setEmail(user.getEmail());
      
      UserEntity savedUser = entityManager.merge(existingUser);

      return savedUser;
    } catch (Exception e) {
      throw new RuntimeException("Error updating user ", e);
    }
  }
}
