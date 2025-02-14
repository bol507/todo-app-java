package org.eclipse.jakarta.service;

import java.util.Collection;
import java.util.List;

import org.eclipse.jakarta.entity.TodoEntity;
import org.eclipse.jakarta.entity.UserEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class QueryService {
  
  @PersistenceContext
  EntityManager entityManager;

  public UserEntity findUserByEmail(String email) {
    return entityManager.createNamedQuery(UserEntity.FIND_USER_BY_EMAIL, UserEntity.class)
      .setParameter("email", email).getSingleResult();
  }

  public List<UserEntity> findAllUsers() {
    return entityManager.createNamedQuery(UserEntity.ORDER_BY_FULL_NAME, UserEntity.class).getResultList();
  }

  public UserEntity findUserById(Long id) {
    return entityManager.createNamedQuery(UserEntity.FIND_USER_BY_ID, UserEntity.class)
      .setParameter("id", id).getSingleResult();
  }

  public Collection<UserEntity> findAllUsersByName(String name) {
    return entityManager.createNamedQuery(UserEntity.FIND_USER_BY_NAME, UserEntity.class)
      .setParameter("name", "%" + name + "%").getResultList();
  }
  
  public Collection<TodoEntity> findAllTodoByOwnerEmail(String email) {
    return entityManager.createNamedQuery(TodoEntity.FIND_ALL_TODO_BY_OWNER_EMAIL, TodoEntity.class)
      .setParameter("email", email).getResultList();
  }

}
