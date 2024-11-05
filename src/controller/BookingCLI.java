package controller;

import command.AddBookingCommand;
import command.BookingInvoker;
import program.*;
import service.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BookingCLI {
    private Scanner scanner = new Scanner(System.in);
    private BookingManager bookingManager = BookingManager.getInstance();
    private BookingService bookingService = new BookingServiceProxy();
    private PaymentService paymentService = new PaymentAdapter();
    private BookingInvoker invoker = new BookingInvoker();

    public void start() {
        System.out.println("Welcome to the Booking System!");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Book a Room");
            System.out.println("2. Show Bookings");
            System.out.println("3. Make a Payment");
            System.out.println("4. Exit");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> bookRoom();
                case "2" -> showBookings();
                case "3" -> makePayment();
                case "4" -> {
                    System.out.println("Exiting the Booking System");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again");
            }
        }
    }

    private void bookRoom() {
        System.out.println("\n--- Booking a Room ---");
        Room room = chooseRoomType();
        if (room == null) return; // Invalid choice; exit to main menu

        System.out.print("Enter the number of guests: ");
        int guests;
        try {
            guests = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for guests. Please enter a number");
            return;
        }

        // Validate guest count and provide room recommendations
        if (guests > room.getMaxGuests()) {
            if (guests == 1) {
                System.out.println("The selected room is too large for 1 guest. We recommend a Single Room");
            } else if (guests == 2) {
                System.out.println("The selected room is too small or too large for 2 guests. We recommend a Double Room");
            } else if (guests >= 3 && guests <= 6) {
                System.out.println("For groups of 3 to 6 guests, we recommend a Family Room");
            } else {
                System.out.println("We currently do not have rooms that can accommodate more than 6 guests");
            }
            return;
        }

        // Collect client information
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your surname: ");
        String surname = scanner.nextLine().trim();

        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine().trim();

        // Date validation
        LocalDate checkInDate = getDate("check-in");
        if (checkInDate == null || !checkInDate.isAfter(LocalDate.now())) {
            System.out.println("Please enter a future date for check-in");
            return;
        }

        LocalDate checkOutDate = getDate("check-out");
        if (checkOutDate == null || !checkOutDate.isAfter(checkInDate)) {
            System.out.println("Please enter a check-out date after the check-in date");
            return;
        }

        System.out.print("Would you like to add breakfast (yes/no)? ");
        boolean breakfastIncluded = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("Would you like to add parking (yes/no)? ");
        boolean parkingIncluded = scanner.nextLine().equalsIgnoreCase("yes");

        long numberOfNights = checkInDate.until(checkOutDate).getDays();
        double totalPrice = numberOfNights * room.getPrice();

        // Create the booking details
        BookingDetails bookingDetails = new BookingDetails(room.getType(), checkInDate, checkOutDate, guests, breakfastIncluded, parkingIncluded, totalPrice);
        invoker.executeCommand(new AddBookingCommand(bookingManager, bookingDetails));

        System.out.println("\n--- Booking Summary ---");
        System.out.printf("Thank you %s %s! We will remind you closer to the date of your booking\n", surname, firstName);
    }


    private Room chooseRoomType() {
        System.out.println("Choose room type:");
        System.out.println("1. Single Room - A cozy room for one guest Price: $100/night");
        System.out.println("2. Double Room - A spacious room for two guests Price: $139/night");
        System.out.println("3. Family Room - Ideal for families of 3 to 6 guests Price: $200/night");

        try {
            int roomChoice = Integer.parseInt(scanner.nextLine());
            return switch (roomChoice) {
                case 1 -> new SingleRoom();
                case 2 -> new DoubleRoom();
                case 3 -> new FamilyRoom();
                default -> {
                    System.out.println("Invalid choice. Returning to main menu");
                    yield null;
                }
            };
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number");
            return null;
        }
    }

    private LocalDate getDate(String type) {
        try {
            System.out.printf("Enter %s year (YYYY): ", type);
            int year = Integer.parseInt(scanner.nextLine());

            System.out.printf("Enter %s month (MM): ", type);
            int month = Integer.parseInt(scanner.nextLine());

            System.out.printf("Enter %s day (DD): ", type);
            int day = Integer.parseInt(scanner.nextLine());

            return LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid date entered Please enter a valid date");
            return null;
        }
    }

    private void showBookings() {
        System.out.println("Current bookings:");
        List<BookingDetails> bookings = bookingManager.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings found");
            return;
        }

        for (BookingDetails booking : bookings) {
            System.out.println(booking);
        }
    }

    private void makePayment() {
        showBookings();

        System.out.print("Enter the booking ID to make a payment (or type 'exit' to cancel): ");
        String bookingChoice = scanner.nextLine();

        if (bookingChoice.equalsIgnoreCase("exit")) {
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingChoice);
            BookingDetails bookingDetails = bookingManager.getBookingDetails(bookingId);

            if (bookingDetails == null) {
                System.out.println("Invalid booking ID. Please try again");
                return;
            }

            System.out.println("\n--- Make a Payment ---");
            double totalPrice = bookingDetails.getTotalPrice();
            System.out.printf("Your total booking price is: $%.2f\n", totalPrice);
            System.out.print("Enter payment account: ");
            String account = scanner.nextLine();

            paymentService.processPayment(account, totalPrice);
            System.out.println("Payment processed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid booking ID");
        }
    }
}
