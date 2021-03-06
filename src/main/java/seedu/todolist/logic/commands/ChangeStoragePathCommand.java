package seedu.todolist.logic.commands;

import java.io.IOException;

import seedu.todolist.commons.core.Config;
import seedu.todolist.commons.util.ConfigUtil;
//import seedu.todolist.commons.util.FileUtil;
import seedu.todolist.logic.commands.exceptions.CommandException;

//@@author A0139633B
/*
 * Changes the save location of save file
 */
public class ChangeStoragePathCommand extends Command {

    public static final String COMMAND_WORD = "changestorage";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes where the to-do list is saved. "
            + "Parameters: PATH_TO_FILE\n"
            + "Example: " + COMMAND_WORD
            + " different_folder";

    public static final String MESSAGE_SUCCESS = "Storage path changed to: %1$s";
    public static final String MESSAGE_FAILURE = "Error saving to path: %1$s";

    private String commandText;
    private final String path;

    //takes in an relative or absolute path
    public ChangeStoragePathCommand(String path) {
        this.path = path + "/todolist.xml";
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert config != null;
        try {
            //TODO check if the file can be written to the path first (with the storage)
            config.setToDoListFilePath(this.path);
            ConfigUtil.saveConfig(config, Config.DEFAULT_CONFIG_FILE);
            storage.setStoragePath(this.path);
            storage.saveToDoList(model.getToDoList(), this.path);
            model.changeStoragePath(this.path);
            return new CommandResult(String.format(MESSAGE_SUCCESS, this.path));
        } catch (IOException e) {
            e.printStackTrace();
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
