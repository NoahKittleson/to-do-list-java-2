import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Category {
  private String name;
  private int id;

  public Category(String name) {
    this.name = name;
  }

  @Override 
  public boolean equals(Object otherCategory) {
    if(!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName()) &&
      this.getId() == newCategory.getId();
    }
  }

  public String getName(){
    return name;
  }

  public int getId() {
    return id;
  }

  public static List<Category> all() {
    try( Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, name FROM categories";
      return con.createQuery(sql)
        .executeAndFetch(Category.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true) 
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Category.class);
    }
  }

  public List<Task> getTasks() {
    try(Connection con =  DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks WHERE categoryId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Task.class);
    }
  }
}
