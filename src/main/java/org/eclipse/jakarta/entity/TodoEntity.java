package org.eclipse.jakarta.entity;

import java.time.LocalDate;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="todo")
@NamedQuery(name=TodoEntity.FIND_ALL_TODO_BY_OWNER_EMAIL, query = "SELECT todo FROM TodoEntity todo WHERE todo.todoOwner.email = :email")
@NamedQuery(name=TodoEntity.FIND_TODO_BY_ID, query = "SELECT todo FROM TodoEntity todo WHERE todo.id = :id and todo.todoOwner.email = :email")
@NamedQuery(name=TodoEntity.FIND_TODO_BY_STATE, query = "SELECT todo FROM TodoEntity todo WHERE todo.isCompleted = :state and todo.todoOwner.email = :email")
@NamedQuery(name=TodoEntity.FIND_TODO_BY_DUE_DATE, query = "SELECT todo FROM TodoEntity todo WHERE todo.dueDate = :dueDate and todo.todoOwner.email = :email")
public class TodoEntity extends AbstractEntity {

  public static final String FIND_ALL_TODO_BY_OWNER_EMAIL = "TodoEntity.findAllTodoByOwnerEmail";
  public static final String FIND_TODO_BY_ID = "TodoEntity.findTodoById";
  public static final String FIND_TODO_BY_STATE = "TodoEntity.findTodoByState";
  public static final String FIND_TODO_BY_DUE_DATE = "TodoEntity.findTodoByDueDate";

  @NotEmpty(message = "A todo task must be set")
  @Size(min=3, max = 150, message="The minimum character length should be 3 and max 150.")
  private String task;
  
  @NotNull(message="Due date must be set")
  @FutureOrPresent(message="Due date must be in the present or future.")
  @JsonbDateFormat(value="yyyy-MM-dd")
  private LocalDate dueDate;

  

  private LocalDate dateCreated;
  private boolean isCompleted;
  private boolean isArchived;
  private boolean isRemainder;

  @ManyToOne
  @JoinColumn(name = "todo_owner")
  private UserEntity todoOwner;

  @PrePersist
  public void init() {
    setDateCreated(LocalDate.now());
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public LocalDate getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDate dateCreated) {
    this.dateCreated = dateCreated;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public boolean getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public boolean getIsArchived() {
    return isArchived;
  }

  public void setIsArchived(boolean isArchived) {
    this.isArchived = isArchived;
  }

  public boolean getIsRemainder() {
    return isRemainder;
  }

  public void setIsRemainder(boolean isRemainder) {
    this.isRemainder = isRemainder;
  }

  public UserEntity getTodoOwner() {
    return todoOwner;
  }

  public void setTodoOwner(UserEntity todoOwner) {
    this.todoOwner = todoOwner;
  }
}
