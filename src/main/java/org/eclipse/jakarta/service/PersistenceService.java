package org.eclipse.jakarta.service;

import org.eclipse.jakarta.entity.TodoEntity;
import org.eclipse.jakarta.entity.UserEntity;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataSourceDefinition(name = "todo-app-java", className = "org.sqlite.SQLiteDataSource", url = "jdbc:sqlite:sqlite.db")
@Stateless
public class PersistenceService {

  @Inject 
  private SessionService sessionService;

  @Inject
  private QueryService queryService;

  @PersistenceContext
  EntityManager entityManager;

  public UserEntity saveUser(UserEntity  user){
    if(user.getId() == null){
      entityManager.persist(user);
    }else{
      entityManager.merge(user);
    }
    return user;
  }

  public TodoEntity saveTodo(TodoEntity todo){
    
    String email = sessionService.getEmail();
    UserEntity user = queryService.findUserByEmail(email);

    todo.setTodoOwner(null);

    if(todo.getId() == null && user != null){
      todo.setTodoOwner(user);
      entityManager.persist(todo);
    }else{
      entityManager.merge(todo);
    }
    return todo;
  }

}
