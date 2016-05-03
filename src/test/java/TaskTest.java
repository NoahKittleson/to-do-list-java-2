import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

public class TaskTest {


  @Before 
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTasksQuery = "DELETE FROM tasks *;";
      String deleteCategoriesQuery = "DELETE FROM categories *;";
      con.createQuery(deleteTasksQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
    }
  }

  @Test
  public void Task_instantiatesCorrectly_true() {
    Task testTask = new Task("Do the dishes", 1);
    assertEquals(true, testTask instanceof Task);
  }

  @Test
  public void Task_instantiatesWithDescription_String() {
    Task testTask= new Task("Mow the lawn", 1);
    assertEquals("Mow the lawn", testTask.getDescription());
  }

  @Test 
  public void all_InstantiatesEmpty() {
    assertEquals(Task.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Task firstTask = new Task("Trim the hedge", 1);
    Task secondTask = new Task("Trim the hedge", 1);
    assertTrue(firstTask.equals(secondTask));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    Task testTask = new Task("Do the laundry", 1);
    testTask.save();
    assertTrue(Task.all().get(0).equals(testTask));
  }

  @Test
  public void save_assignsIdToObject() {
    Task testTask = new Task("Mow the lawn", 1);
    testTask.save();
    Task savedTask = Task.all().get(0);
    assertEquals(testTask.getId(), savedTask.getId());
  }

  @Test
  public void find_findsTaskInDatabase_true() {
    Task myTask = new Task("Mow the lawn", 1);
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertTrue(myTask.equals(savedTask));
  }

  @Test 
  public void Task_isCompleted_isFalseAfterInstantiation_false() {
    Task testTask= new Task("Mow the lawn", 1);
    assertEquals(false, testTask.isCompleted());
  }

  @Test 
  public void getCreatedAt_instantiatesWithCurrentTime_today() {
    Task myTask = new Task("Mow the lawn", 1);
    assertEquals(LocalDateTime.now().getDayOfWeek(), myTask.getCreatedAt().getDayOfWeek());
  }

  @Test
  public void save_savesCategoryIdIntoDB_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn", myCategory.getId());
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertEquals(savedTask.getCategoryId(), myCategory.getId());
  }


}
