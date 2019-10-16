package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSROOM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.Classroom;

import static java.util.Objects.requireNonNull;

/**
 * Makes a new classroom in the Teacher's Notebook.
 */
public class NewCommand extends Command {

    public static final String COMMAND_WORD = "new";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Makes a new classroom "
            + "Parameters: "
            + PREFIX_CLASSROOM + "CLASS "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASSROOM + "4E3 ";

    public static final String MESSAGE_SUCCESS = "New classroom added: %1$s";
    public static final String MESSAGE_DUPLICATE_CLASSROOM = "This classroom already exists in the Teacher's Notebook";

    private final Classroom toAdd;

    /**
     * Creates a NewCommand to add the specified {@code Classroom}
     */
    public NewCommand(Classroom classroom) {
        requireNonNull(classroom);
        toAdd = classroom;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasClassroom(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLASSROOM);
        }

        model.addClassroom(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NewCommand // instanceof handles nulls
                && toAdd.equals(((NewCommand) other).toAdd));
    }
}
