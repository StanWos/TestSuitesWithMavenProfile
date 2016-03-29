package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.WebDriverRunner.url;

/**
 * Created by stan on 25.03.16.
 */
public class TodoMVC {

    public static ElementsCollection tasks = $$("#todo-list li");
    public static SelenideElement newTodo = $("#new-todo");

    public static void add(String... taskTexts) {
        for (String text : taskTexts) {
            newTodo.setValue(text).pressEnter();
        }
    }

    public static SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    public static void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    public static void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    public static void toggleAll() {
        $("#toggle-all").click();
    }

    public static void clearCompleted() {
        $("#clear-completed").click();
    }

    public static void filterAll() {
        $(By.linkText("All")).click();
    }

    public static void filterActive() {
        $(By.linkText("Active")).click();
    }

    public static void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    public static void assertTasks(String... tasksTexts) {
        tasks.shouldHave(exactTexts(tasksTexts));
    }

    public static void assertNoTasks() {
        tasks.shouldHave(empty);
    }

    public static void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    public static void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    public static void assertItemLeft(int numberOfTasks) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(numberOfTasks)));
    }

    public static void ensureCurrentUrl() {
        if (!url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    } // instead of using @Before for opening url


    //  ******* Implementing Precondition-helpers *******

    public static enum TaskType {

        ACTIVE("false"), COMPLETED("true");

        public String flag;

        TaskType(String flag) {
            this.flag = flag;
        }

        public String getFlag() {
            return flag;
        }
    }

    public static class Task {
        String taskText;
        TaskType taskType;

        public Task(String taskText, TaskType taskType) {
            this.taskText = taskText;
            this.taskType = taskType;
        }
    }

    public static Task aTask(String taskText, TaskType taskType) {
        return new Task(taskText, taskType);
    }

    public static void givenAtActive(Task... tasks) {
        givenAtAll(tasks);
        filterActive();
    }

    public static void givenAtCompleted(Task... tasks) {
        givenAtAll(tasks);
        filterCompleted();
    }

    public static void givenAtAll(TaskType mainType, String... taskTexts) {
        Task[] tasks = new Task[taskTexts.length];
        for (int i = 0; i < taskTexts.length; i++) {
            tasks[i] = aTask(taskTexts[i], mainType);
        }
        givenAtAll(tasks);
    }

    public static void givenAtActive(TaskType mainType, String... taskTexts) {
        givenAtAll(mainType, taskTexts);
        filterActive();
    }

    public static void givenAtCompleted(TaskType mainType, String... taskTexts) {
        givenAtAll(mainType, taskTexts);
        filterCompleted();
    }

    public static void givenAtAll(Task... tasks) {
        ensureCurrentUrl();

        String JavaS = "localStorage.setItem('todos-troopjs', '[";

        for (Task task : tasks) {
            JavaS += "{\"completed\":" + task.taskType.getFlag() + ", \"title\":\"" + task.taskText + "\"},";
        }
        if (tasks.length > 0) {
            JavaS = JavaS.substring(0, (JavaS.length() - 1));
        }
        JavaS += "]');";
        executeJavaScript(JavaS);
        refresh();
    }
}
