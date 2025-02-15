package org.eclipse.jakarta.service;

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
        .setParameter("id", id).setParameter("email", sessionService.getEmail()).getSingleResult(); 
    } catch (NonUniqueResultException |  NoResultException e) {
      return null;
    }
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
