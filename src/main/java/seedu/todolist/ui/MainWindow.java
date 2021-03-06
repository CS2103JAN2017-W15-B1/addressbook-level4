package seedu.todolist.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.todolist.commons.core.Config;
import seedu.todolist.commons.core.GuiSettings;
import seedu.todolist.commons.events.ui.ExitAppRequestEvent;
import seedu.todolist.commons.util.FxViewUtil;
import seedu.todolist.logic.Logic;
import seedu.todolist.logic.commands.ListCommand;
import seedu.todolist.logic.commands.exceptions.CommandException;
import seedu.todolist.model.UserPrefs;
import seedu.todolist.model.task.Task;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {
    //@@A01442420W

    private static final String ICON = "/images/todo_list_filled_32.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 600;
    private static final String LIST = "list";
    private static final String SELECTED = "selected";

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private TaskDetailsPanel taskDetailsPanel;
    private TaskListPanel taskListPanel;
    private Config config;
    private StatusBarFooter statusBarFooter;

    @FXML
    private AnchorPane taskDetailsPlaceholder;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private AnchorPane taskListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;

    @FXML
    private Button incompleteButton;

    @FXML
    private Button allButton;

    @FXML
    private Button completeButton;

    @FXML
    private Button overdueButton;

    @FXML
    private Button upcomingButton;


    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        //setAccelerators();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }
    **/

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    /**
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.

        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }
      **/

    void fillInnerParts() {
        taskDetailsPanel = new TaskDetailsPanel(taskDetailsPlaceholder);
        taskListPanel = new TaskListPanel(getTaskListPlaceholder(), logic.getFilteredTaskList());
        statusBarFooter = new StatusBarFooter(getStatusbarPlaceholder(), config.getToDoListFilePath());
        new ResultDisplay(getResultDisplayPlaceholder());
        new CommandBox(getCommandBoxPlaceholder(), logic);
        incompleteButton.getStyleClass().add(SELECTED);
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    private AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    //@@author A0144240W
    @FXML
    public void handleAllButton() throws CommandException {
        String command = LIST + " " + ListCommand.TYPE_ALL;
        logic.execute(command);
    }

    //@@author A0144240W
    @FXML
    public void handleCompletedButton() throws CommandException {
        String command = LIST + " " + ListCommand.TYPE_COMPLETE;
        logic.execute(command);
    }

    //@@author A0144240W
    @FXML
    public void handleIncompleteButton() throws CommandException {
        String command = LIST + " " + ListCommand.TYPE_INCOMPLETE;
        logic.execute(command);
    }

    //@@author A0144240W
    @FXML
    public void handleOverdueButton() throws CommandException {
        String command = LIST + " " + ListCommand.TYPE_OVERDUE;
        logic.execute(command);
    }

    //@@author A0144240W
    @FXML
    public void handleUpcomingButton() throws CommandException {
        String command = LIST + " " + ListCommand.TYPE_UPCOMING;
        logic.execute(command);
    }

    //@@author A0139633B
    public void updateSaveLocationInFooter(String newPath) {
        statusBarFooter.setSaveLocation(newPath);
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public TaskListPanel getTaskListPanel() {
        return this.taskListPanel;
    }

    void loadPersonPage(Task person) {
        taskDetailsPanel.loadPersonPage(person);
    }

    void releaseResources() {
        taskDetailsPanel.freeResources();
    }


    //@@author A0144240W
    public void changeButtonsBackToOriginalState() {
        incompleteButton.getStyleClass().remove(SELECTED);
        completeButton.getStyleClass().remove(SELECTED);
        upcomingButton.getStyleClass().remove(SELECTED);
        overdueButton.getStyleClass().remove(SELECTED);
        allButton.getStyleClass().remove(SELECTED);
    }

    //@@author A0144240W
    public void indicateButtonChange(String typeOfButton) {
        changeButtonsBackToOriginalState();
        taskListPanel.changeList(getTaskListPlaceholder(), logic.getFilteredTaskList());
        switch(typeOfButton) {
        case ListCommand.TYPE_UPCOMING:
            changeUpcomingButton();
            break;
        case ListCommand.TYPE_INCOMPLETE:
            changeIncompleteButton();
            break;
        case ListCommand.TYPE_COMPLETE:
            changeCompleteButton();
            break;
        case ListCommand.TYPE_OVERDUE:
            changeOverdueButton();
            break;
        default:
            changeAllButton();
        }
    }

    //@@author A0144240W
    /**
     * Changes the display of the upcoming button to be selected
     * and adds the sortedList to the task panel
     */
    private void changeUpcomingButton() {
        taskListPanel.changeList(getTaskListPlaceholder(), logic.getSortedTaskList());
        upcomingButton.getStyleClass().add(SELECTED);
    }

    //@@author A0144240W
    /**
     * Changes the display of the incomplete button to be selected
     */
    private void changeIncompleteButton() {
        incompleteButton.getStyleClass().add(SELECTED);
    }

    //@@author A0144240W
    /**
     * Changes the display of the complete button to be selected
     */
    private void changeCompleteButton() {
        completeButton.getStyleClass().add(SELECTED);
    }

    //@@author A0144240W
    /**
     * Changes the display of the overdue button to be selected
     */
    private void changeOverdueButton() {
        overdueButton.getStyleClass().add(SELECTED);
    }

    //@@author A0144240W
    /**
     * Changes the display of the all button to be selected
     */
    private void changeAllButton() {
        allButton.getStyleClass().add(SELECTED);
    }









}
