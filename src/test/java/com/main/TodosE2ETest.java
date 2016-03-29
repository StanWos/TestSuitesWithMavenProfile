package com.main;

import com.main.categories.Smoke;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import static pages.TodoMVC.*;


public class TodosE2ETest extends BaseTest {

    @Test
    @Category(Smoke.class) //Annotation for specifying test suite, in the brackets we specify the group
    public void testTaskLifeCycle() {

        givenAtAll();
        add("1");
        startEdit("1", "1 edited").pressEnter();
        assertTasks("1 edited");

        filterActive();
        assertTasks("1 edited");
        toggleAll();
        add("2");
        assertVisibleTasks("2");

        filterCompleted();
        assertVisibleTasks("1 edited");
        clearCompleted();
        assertNoVisibleTasks();

        filterAll();
        assertItemLeft(1);
        assertTasks("2");

        delete("2");
        assertNoTasks();

    }

}
