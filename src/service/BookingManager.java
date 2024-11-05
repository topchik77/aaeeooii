package service;

import program.BookingDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookingManager implements Iterable<BookingDetails> {
    private static BookingManager instance;
    private List<BookingDetails> bookings;

    private BookingManager() { bookings = new ArrayList<>(); }

    public static BookingManager getInstance() {
        if (instance == null) instance = new BookingManager();
        return instance;
    }

    public void addBooking(BookingDetails booking) { bookings.add(booking); }
    public List<BookingDetails> getBookings() { return bookings; }
    public BookingDetails getBookingDetails(int bookingId) {
        for (BookingDetails booking : bookings) {
            if (booking.getBookingId() == bookingId) return booking;
        }
        return null;
    }

    public Iterator<BookingDetails> iterator() {
        return bookings.iterator();
    }
}
