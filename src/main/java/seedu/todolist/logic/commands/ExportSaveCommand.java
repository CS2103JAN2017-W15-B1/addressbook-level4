package seedu.todolist.logic.commands;

import java.io.IOException;

import seedu.todolist.commons.core.Config;
import seedu.todolist.commons.util.ConfigUtil;
import seedu.todolist.logic.commands.exceptions.CommandException;

//@@author A0139633B
/*
 * Exports the save file of the location
 */
public class ExportSaveCommand extends Command {

    public static final String COMMAND_WORD = "exportsave";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the saved file for the app. "
            + "Parameters: PATH_TO_FILE\n"
            + "Example: " + COMMAND_WORD
            + " some_folder";

    public static final String MESSAGE_SUCCESS = "Save file exported to: %1$s";
    public static final String MESSAGE_FAILURE = "Error saving to path: %1$s";

    private String commandText;
    private final String path;

    //takes in an relative path
    public ExportSaveCommand(String path) {
        this.path = path + "/todolist.xml";
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert config != null;
        try {
            config.setToDoListFilePath(this.path);
            ConfigUtil.saveConfig(config, Config.DEFAULT_CONFIG_FILE);
            storage.changeToDoListFilePath(this.path);
            storage.saveToDoList(model.getToDoList(), this.path);
            return new CommandResult(String.format(MESSAGE_SUCCESS, this.path));
        } catch (IOException e) {
            e.printStackTrace(); //is this necessary?
            return new CommandResult(String.format(MESSAGE_FAILURE, this.path));
        }
    }

    @Override
    public boolean isMutating() {
        return false;
    }

    @Override
    public String getCommandText() {
        return commandText;
    }
}
