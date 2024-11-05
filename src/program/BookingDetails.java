package program;

import java.time.LocalDate;

public class BookingDetails {
    private static int counter = 0;
    private int bookingId;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private boolean breakfastIncluded;
    private boolean parkingIncluded;
    private double totalPrice;

    public BookingDetails(String roomType, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, boolean breakfastIncluded, boolean parkingIncluded, double totalPrice) {
        this.bookingId = ++counter;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.breakfastIncluded = breakfastIncluded;
        this.parkingIncluded = parkingIncluded;
        this.totalPrice = totalPrice;
    }

    public int getBookingId() { return bookingId; }
    public String getRoomType() { return roomType; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public boolean isBreakfastIncluded() { return breakfastIncluded; }
    public boolean isParkingIncluded() { return parkingIncluded; }
    public double getTotalPrice() { return totalPrice; }

    public String toString() {
        return String.format("Booking ID: %d, Room Type: %s, Check-in: %s, Check-out: %s, Guests: %d, Breakfast: %s, Parking: %s, Total Price: $%.2f",
                bookingId, roomType, checkInDate, checkOutDate, numberOfGuests,
                breakfastIncluded ? "Included" : "Not Included",
                parkingIncluded ? "Included" : "Not Included",
                totalPrice);
    }
}
