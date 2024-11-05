package command;

import command.Command;

public class BookingInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand(Command command) {
        this.command = command;
        command.execute();
    }
}
