package org.eclipse.jakarta.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.eclipse.jakarta.entity.TodoEntity;
import org.eclipse.jakarta.entity.UserEntity;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class QueryService {
  
  @PersistenceContext
  EntityManager entityManager;

  @Inject
  private SessionService sessionService;


  public UserEntity findUserByEmail(String email) {
    
    try {
      return entityManager.createNamedQuery(UserEntity.FIND_USER_BY_EMAIL, UserEntity.class)
        .setParameter("email", email).getSingleResult();
    } catch (NonUniqueResultException |  NoResultException e) {
      return null;
    }
    
  }

  public List<UserEntity> findAllUsers() {
    return entityManager.createNamedQuery(UserEntity.ORDER_BY_FULL_NAME, UserEntity.class).getResultList();
  }

  public UserEntity findUserById(Long id) {
    try {
      return entityManager.createNamedQuery(UserEntity.FIND_USER_BY_ID, UserEntity.class)
        .setParameter("id", id).getSingleResult(); 
    } catch (NonUniqueResultException |  NoResultException e) {
      return null;
    }
  }

  public Collection<UserEntity> findAllUsersByName(String name) {
    return entityManager.createNamedQuery(UserEntity.FIND_USER_BY_NAME, UserEntity.class)
      .setParameter("name", "%" + name + "%").getResultList();
  }

  public Collection<TodoEntity> findAllTodos() {
    return entityManager.createNamedQuery(TodoEntity.FIND_ALL_TODO_BY_OWNER_EMAIL, TodoEntity.class)
      .setParameter("email", sessionService.getEmail()).getResultList();
  }

  public TodoEntity findTodoById(Long id) {
    
    List<TodoEntity> result = entityManager.createNamedQuery(TodoEntity.FIND_TODO_BY_ID, TodoEntity.class)
    .setParameter("id", id)
    .setParameter("email", sessionService.getEmail())
    .getResultList();
    
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
    
  }

  public void todoAsCompleted(Long id) {
    TodoEntity todo = findTodoById(id);
    if(todo == null) {
      throw new IllegalArgumentException("Todo not found with ID: " + id);
    }
    todo.setIsCompleted(true);
    entityManager.merge(todo);
  }

  public List<TodoEntity> getTodoByState(boolean state) {
    return entityManager.createNamedQuery(TodoEntity.FIND_TODO_BY_STATE, TodoEntity.class)
      .setParameter("state", state)
      .setParameter("email", sessionService.getEmail())
      .getResultList();
  }

  public List<TodoEntity> getCompleted() {
    return getTodoByState(true);
  }

  public List<TodoEntity> getUncompleted() {
    return getTodoByState(false);
  }

  public List<TodoEntity> getByDueDate(LocalDate dueDate) {
    return entityManager.createNamedQuery(TodoEntity.FIND_TODO_BY_DUE_DATE, TodoEntity.class)
      .setParameter("dueDate", dueDate)
      .setParameter("email", sessionService.getEmail())
      .getResultList();
  }

  public void todoAsArchived(Long id) {
    TodoEntity todo = findTodoById(id);
    if(todo == null) {
      throw new IllegalArgumentException("Todo not found with ID: " + id);
    }
    todo.setIsArchived(true);
    entityManager.merge(todo);
  }

}
 