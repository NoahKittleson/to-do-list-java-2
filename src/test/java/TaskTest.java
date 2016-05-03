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
      String sql = "DELETE FROM tasks *";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Task_instantiatesCorrectly_true() {
    Task testTask = new Task("Do the dishes");
    assertEquals(true, testTask instanceof Task);
  }

  @Test
  public void Task_instantiatesWithDescription_String() {
    Task testTask= new Task("Mow the lawn");
    assertEquals("Mow the lawn", testTask.getDescription());
  }

  @Test 
  public void all_InstantiatesEmpty() {
    assertEquals(Task.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Task firstTask = new Task("Trim the hedge");
    Task secondTask = new Task("Trim the hedge");
    assertTrue(firstTask.equals(secondTask));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    Task testTask = new Task("Do the laundry");
    testTask.save();
    assertTrue(Task.all().get(0).equals(testTask));
  }

  @Test
  public void save_assignsIdToObject() {
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    Task savedTask = Task.all().get(0);
    assertEquals(testTask.getId(), savedTask.getId());
}

  // @Test 
  // public void Task_isCompleted_isFalseAfterInstantiation_false() {
  //   Task testTask= new Task("Mow the lawn");
  //   assertEquals(false, myTask.isCompleted());
  // }

  // @Test 
  // public void getCreatedAt_instantiatesWithCurrentTime_today() {
  //   Task myTask = new Task("Mow the lawn");
  //   assertEquals(LocalDateTime.now().getDayOfWeek(), myTask.getCreatedAt().getDayOfWeek());
  // }

  // @Test 
  // public void all_returnsAllInstancesOfTask_true() {
  //   Task firstTask = new Task("Mow the lawn");
  //   Task secondTask = new Task("Buy groceries");
  //   assertTrue(Task.all().contains(firstTask));
  //   assertTrue(Task.all().contains(secondTask));
  // }

  // @Test
  // public void clear_emptiedAllTasksFromArrayList_0() {
  //   Task myTask = new Task("Mow the lawn");
  //   Task.clear();
  //   assertEquals(Task.all().size(), 0);
  // }
}
