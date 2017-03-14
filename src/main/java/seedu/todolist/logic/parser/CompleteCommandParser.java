package seedu.todolist.logic.parser;

import static seedu.todolist.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import seedu.todolist.commons.exceptions.IllegalValueException;
import seedu.todolist.logic.commands.Command;
import seedu.todolist.logic.commands.CompleteCommand;
import seedu.todolist.logic.commands.IncorrectCommand;

/**
 * Parses input arguments and creates a new CompleteCommand object
 */
public class CompleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the CompleteCommand
     * and returns a CompleteCommand object for execution.
     */
    public Command parse(String args) throws IllegalValueException {

        Optional<Integer> index = ParserUtil.parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));
        }

        return new CompleteCommand(index.get());
    }
}
