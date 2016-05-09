import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("tasks", Task.all());
      model.put("template", "templates/tasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String description = request.queryParams("description");
      Task newTask = new Task(description);
      newTask.save();
      response.redirect("/tasks");
      return null;
    });

    post("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

    get("/tasks/:id", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params("id")));
      model.put("task", task);
      model.put("allCategories", Category.all());
      model.put("template", "templates/task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params("id")));
      model.put("category", category);
      model.put("allTasks", Task.all());
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_tasks", (request, response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Task task = Task.find(taskId);
      category.addTask(task);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    post("/add_categories", (request, response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Task task = Task.find(taskId);
      task.addCategory(category);
      response.redirect("/tasks/" + taskId);
      return null;
    });

    get("/tasks/:task_id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int taskId = Integer.parseInt(request.params("task_id"));
      Task currentTask = Task.find(taskId);
      model.put("task", currentTask);
      model.put("template", "templates/edit_task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks/:task_id/edit", (request, response) -> {
      int taskId = Integer.parseInt(request.params("task_id"));
      Task currentTask = Task.find(taskId);
      String description = request.queryParams("description");
      currentTask.update(description);
      response.redirect("/tasks/" + taskId);
      return null;
    });

    get("/categories/:category_id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int categoryId = Integer.parseInt(request.params("category_id"));
      Category currentCategory = Category.find(categoryId);
      model.put("category", currentCategory);
      model.put("template", "templates/edit_category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/:category_id/edit", (request, response) -> {
      int categoryId = Integer.parseInt(request.params("category_id"));
      Category currentCategory = Category.find(categoryId);
      String name = request.queryParams("name");
      currentCategory.update(name);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    post("/tasks/:task_id/delete", (request, response) -> {
      int taskId = Integer.parseInt(request.params("task_id"));
      Task currentTask = Task.find(taskId);
      currentTask.delete();
      response.redirect("/tasks");
      return null;
    });

    post("/categories/:category_id/delete", (request, response) -> {
      int categoryId = Integer.parseInt(request.params("category_id"));
      Category currentCategory = Category.find(categoryId);
      currentCategory.delete();
      response.redirect("/categories");
      return null;
    });

    post("/tasks/:task_id/complete", (request, response) -> {
      int taskId = Integer.parseInt(request.params("task_id"));
      Task currentTask = Task.find(taskId);
      currentTask.setComplete();
      response.redirect("/tasks/" + taskId);
      return null;
    });

    post("/tasks/:task_id/upDATE", (request, response) -> {
      int taskId = Integer.parseInt(request.params("task_id"));
      Task currentTask = Task.find(taskId);
      String date = request.queryParams("date");
      System.out.println(date);
      currentTask.setDueDate(date);
      response.redirect("/tasks/" + taskId);
      return null;
    });


  }
}
