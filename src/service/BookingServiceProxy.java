package service;

import program.BookingDetails;
import service.BookingManager;
import service.BookingService;

public class BookingServiceProxy implements BookingService {
    private BookingManager manager = BookingManager.getInstance();

    public void bookRoom(BookingDetails booking) {
        manager.addBooking(booking);
        System.out.println("Room booked successfully!");
    }
}
