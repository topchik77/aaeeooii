package command;

import command.Command;
import program.BookingDetails;
import service.BookingManager;

public class AddBookingCommand implements Command {
    private BookingManager bookingManager;
    private BookingDetails bookingDetails;

    public AddBookingCommand(BookingManager bookingManager, BookingDetails bookingDetails) {
        this.bookingManager = bookingManager;
        this.bookingDetails = bookingDetails;
    }

    public void execute() {
        bookingManager.addBooking(bookingDetails);
    }
}
