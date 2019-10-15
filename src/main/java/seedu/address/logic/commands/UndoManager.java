package seedu.address.logic.commands;

import java.util.LinkedList;

public class UndoManager {
    private LinkedList<Command> commandStack = new LinkedList<Command>();
    private LinkedList<Command> redoStack = new LinkedList<Command>();

    public static final String MESSAGE_NOTHING_TO_UNDO = "There is nothing to undo!";
    public static final String MESSAGE_NOTHING_TO_REDO = "There is nothing to redo!";

    public void execute(Command command) {
        commandStack.addFirst(command);
        redoStack.clear();
    }

    public void undo() {
        if (commandStack.isEmpty()) {
            return;
        }
        Command command = commandStack.removeFirst();
        command.undo();
        redoStack.addFirst(command);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Command command = redoStack.removeFirst();
        command.redo();
        commandStack.addFirst(command);
    }
}
