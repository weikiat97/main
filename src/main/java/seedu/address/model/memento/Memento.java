package seedu.address.model.memento;

import seedu.address.logic.commands.Command;

public class Memento {

    private Command command;

    public Memento (Command command) {
        this.command = command;
    }

    public Command getState() {
        return command;
    }

    public void setState(Command command) {
        this.command = command;
    }
}
