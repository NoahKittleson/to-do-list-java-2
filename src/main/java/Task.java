import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Task {
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private int id;

  public Task (String description) {
    this.description = description;
    this.completed = false;
    this.createdAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object otherTask) {
    if(!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) && 
      this.getId() == newTask.getId();
    }
  }



  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks (description) VALUES (:description)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .executeUpdate()
        .getKey();
    }
  }


  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public boolean isCompleted() {
  	return completed;
  }

  public LocalDateTime getCreatedAt() {
  	return createdAt;
  }

  public static List<Task> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, description FROM tasks";
     return con.createQuery(sql).executeAndFetch(Task.class);

    }
  }



}
