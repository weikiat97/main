package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.memento.Memento;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    private Memento memento;

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Undo the previous command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command undo.
     */
    public abstract CommandResult undo(Model model) throws CommandException;

    /**
     * Redo the undone command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on
     * @return feedback message of the oepration result for display
     * @throws CommandException If an error occurs during command redo.
     */
    public abstract CommandResult redo(Model model) throws CommandException;

}
