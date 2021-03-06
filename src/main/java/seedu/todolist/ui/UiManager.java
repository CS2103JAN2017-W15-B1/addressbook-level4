package seedu.todolist.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seedu.todolist.MainApp;
import seedu.todolist.commons.core.ComponentManager;
import seedu.todolist.commons.core.Config;
import seedu.todolist.commons.core.LogsCenter;
import seedu.todolist.commons.events.model.ToDoListChangedEvent;
import seedu.todolist.commons.events.model.ViewListChangedEvent;
import seedu.todolist.commons.events.storage.DataSavingExceptionEvent;
import seedu.todolist.commons.events.ui.JumpToListRequestEvent;
import seedu.todolist.commons.events.ui.ShowHelpRequestEvent;
import seedu.todolist.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.todolist.commons.util.StringUtil;
import seedu.todolist.logic.Logic;
import seedu.todolist.model.UserPrefs;

/**
 * The manager of the UI component.
 */
public class UiManager extends ComponentManager implements Ui {
    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/todo_list_filled_32.png";
    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";
    private static final String ADD = "add";
    private static final String DELETE = "delete";
    private static final String COMPLETE = "complete";

    private Logic logic;
    private Config config;
    private UserPrefs prefs;
    private MainWindow mainWindow;

    public UiManager(Logic logic, Config config, UserPrefs prefs) {
        super();
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");
        primaryStage.setTitle(config.getAppTitle());

        //Set the application icon.
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));

        try {
            mainWindow = new MainWindow(primaryStage, config, prefs, logic);
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    @Override
    public void stop() {
        prefs.updateLastUsedGuiSetting(mainWindow.getCurrentGuiSetting());
        mainWindow.hide();
    }

    private void showFileOperationAlertAndWait(String description, String details, Throwable cause) {
        final String content = details + ":\n" + cause.toString();
        showAlertDialogAndWait(AlertType.ERROR, "File Op Error", description, content);
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    //==================== Event Handling Code ===============================================================

    @Subscribe
    public void handleDataSavingExceptionEvent(DataSavingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showFileOperationAlertAndWait("Could not save data", "Could not save data to file", event.exception);
    }

    @Subscribe
    public void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.handleHelp();
    }

    @Subscribe
    public void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.getTaskListPanel().scrollTo(event.targetIndex);
    }

    @Subscribe
    public void handleTaskPanelSelectionChangedEvent(TaskPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.releaseResources();
        mainWindow.loadPersonPage(event.getNewSelection());
    }

    //@@author A0144240W
    @Subscribe
    public void handleViewListChangedEvent(ViewListChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        int firstSpaceIndex = event.toString().indexOf(" ");
        String typeOfList = event.toString().substring(0, firstSpaceIndex);
        mainWindow.indicateButtonChange(typeOfList);
    }

    //@@author A0144240W
    @Subscribe
    public void handleToDoListEventChanged(ToDoListChangedEvent event) {
        if (event.typeOfCommand.equals(ADD)) {
            mainWindow.getTaskListPanel().scrollTo(event.index);
        } else if (event.typeOfCommand.equals(DELETE) || event.typeOfCommand.equals(COMPLETE)) {
            mainWindow.releaseResources();
        }
    }

    //@@author A0139633B
    @Subscribe
    public void handleSaveLocationChangedEvent(ToDoListChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.updateSaveLocationInFooter(config.getToDoListFilePath());
    }

}
