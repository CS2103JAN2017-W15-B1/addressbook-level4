package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxToolkit;

import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainGuiHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.TaskCardHandle;
import guitests.guihandles.TaskDetailsPanelHandle;
import guitests.guihandles.TaskListPanelHandle;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.todolist.TestApp;
import seedu.todolist.commons.core.EventsCenter;
import seedu.todolist.commons.events.BaseEvent;
import seedu.todolist.model.ToDoList;
import seedu.todolist.model.task.Task;
import seedu.todolist.testutil.TestUtil;
import seedu.todolist.testutil.TypicalTestTasks;

/**
 * A GUI Test class for ToDoList.
 */
public abstract class ToDoListGuiTest {

    /* The TestName Rule makes the current test name available inside test methods */
    @Rule
    public TestName name = new TestName();

    TestApp testApp;

    protected TypicalTestTasks td = new TypicalTestTasks();

    /*
     *   Handles to GUI elements present at the start up are created in advance
     *   for easy access from child classes.
     */
    protected MainGuiHandle mainGui;
    //protected MainMenuHandle mainMenu;
    protected TaskListPanelHandle taskListPanel;
    protected ResultDisplayHandle resultDisplay;
    protected CommandBoxHandle commandBox;
    protected TaskDetailsPanelHandle taskDetailsPanel;
    private Stage stage;

    @BeforeClass
    public static void setupSpec() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage((stage) -> {
            mainGui = new MainGuiHandle(new GuiRobot(), stage);
            //mainMenu = mainGui.getMainMenu();
            taskListPanel = mainGui.getTaskListPanel();
            resultDisplay = mainGui.getResultDisplay();
            commandBox = mainGui.getCommandBox();
            taskDetailsPanel = mainGui.getTaskDetailsPanel();
            this.stage = stage;
        });
        EventsCenter.clearSubscribers();
        testApp = (TestApp) FxToolkit.setupApplication(() -> new TestApp(this::getInitialData, getDataFileLocation()));
        FxToolkit.showStage();
        while (!stage.isShowing());
        mainGui.focusOnMainApp();
    }

    /**
     * Override this in child classes to set the initial local data.
     * Return null to use the data in the file specified in {@link #getDataFileLocation()}
     */
    protected ToDoList getInitialData() {
        ToDoList ab = new ToDoList();
        TypicalTestTasks.loadToDoListWithSampleData(ab);
        return ab;
    }

    /**
     * Override this in child classes to set the data file location.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    /**
     * Asserts the task shown in the card is same as the given task
     */
    public void assertMatching(Task task, TaskCardHandle card) {
        assertTrue(TestUtil.compareCardAndTask(card, task));
    }

    /**
     * Asserts the size of the person list is equal to the given number.
     */
    protected void assertListSize(int size) {
        int numberOfPeople = taskListPanel.getNumberOfTasks();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in the Result Display area is same as the given string.
     */
    protected void assertResultMessage(String expected) {
        assertEquals(expected, resultDisplay.getText());
    }

    public void raise(BaseEvent e) {
        //JUnit doesn't run its test cases on the UI thread. Platform.runLater is used to post event on the UI thread.
        Platform.runLater(() -> EventsCenter.getInstance().post(e));
    }
}
