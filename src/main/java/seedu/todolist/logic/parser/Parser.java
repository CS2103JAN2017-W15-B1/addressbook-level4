package seedu.todolist.logic.parser;

import static seedu.todolist.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.todolist.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.todolist.logic.commands.AddCommand;
import seedu.todolist.logic.commands.ChangeStoragePathCommand;
import seedu.todolist.logic.commands.ClearCommand;
import seedu.todolist.logic.commands.Command;
import seedu.todolist.logic.commands.CompleteCommand;
import seedu.todolist.logic.commands.DeleteCommand;
import seedu.todolist.logic.commands.DescribeCommand;
import seedu.todolist.logic.commands.EditCommand;
import seedu.todolist.logic.commands.ExitCommand;
import seedu.todolist.logic.commands.ExportSaveCommand;
import seedu.todolist.logic.commands.FindCommand;
import seedu.todolist.logic.commands.HelpCommand;
import seedu.todolist.logic.commands.IncorrectCommand;
import seedu.todolist.logic.commands.ListCommand;
import seedu.todolist.logic.commands.RedoCommand;
import seedu.todolist.logic.commands.SelectCommand;
import seedu.todolist.logic.commands.SyncCommand;
import seedu.todolist.logic.commands.UndoCommand;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DescribeCommand.COMMAND_WORD:
            return new DescribeCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case CompleteCommand.COMMAND_WORD:
            return new CompleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case ChangeStoragePathCommand.COMMAND_WORD:
            return new ChangeStoragePathCommandParser().parse(arguments);

        case ExportSaveCommand.COMMAND_WORD:
            return new ExportSaveCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case SyncCommand.COMMAND_WORD:
            return new SyncCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND, commandWord);
        }
    }

}
