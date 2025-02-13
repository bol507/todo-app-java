package org.eclipse.jakarta.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TodoEntity extends AbstractEntity {

  private String task;
  private LocalDate dueDate;
  private LocalDate dateCreated;
  private boolean isCompleted;
  private boolean isArchived;
  private boolean isRemaind;

  @ManyToOne
  @JoinColumn(name = "TodoUser_id")
  private UserEntity todoOwner;

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

  public boolean getIsRemaind() {
    return isRemaind;
  }

  public void setIsRemaind(boolean isRemaind) {
    this.isRemaind = isRemaind;
  }

}
