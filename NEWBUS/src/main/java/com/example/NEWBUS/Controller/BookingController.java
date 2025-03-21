package com.example.NEWBUS.Controller;

import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Service.BookingService;
import com.example.NEWBUS.Service.BusService;
import com.example.NEWBUS.Service.EmailService;
import com.example.NEWBUS.Service.PDFGeneratorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BusService busService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private  PDFGeneratorService pdfGeneratorService;
    @Autowired
    private EmailService emailService;


    @PostMapping("/checkout")
    public String checkout(@RequestParam("busId") Long busId,
                           @RequestParam("selectedSeats") String selectedSeatsStr,
                           @RequestParam("passengerNames") List<String> passengerNames,
                           @RequestParam("passengerAges") List<Integer> passengerAges,
                           @RequestParam("passengerGenders") List<String> passengerGenders,
                           HttpSession session,
                           Model model) {

        Long passengerId = (Long) session.getAttribute("loggedInPassengerId");
        if (passengerId == null) {
            System.out.println("Error: Passenger not found in session!");
            return "redirect:/passenger/passengerlogin"; // Redirect to login page
        }

        String passengerEmail = (String) session.getAttribute("loggedInUser");
        System.out.println("Passenger ID from session: " + passengerId);
        System.out.println("Passenger Email from session: " + passengerEmail);

        List<String> seatNumbers = Arrays.stream(selectedSeatsStr.split(","))
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        if (seatNumbers.size() != passengerNames.size()) {
            throw new RuntimeException("Mismatch between seat selection and passenger details.");
        }

        Bus bookedBus = busService.getBusById(busId);
        if (bookedBus == null) {
            throw new RuntimeException("Bus not found!");
        }

        model.addAttribute("bus", bookedBus);
        model.addAttribute("seatNumbers", seatNumbers);
        model.addAttribute("passengerList", passengerNames);
        model.addAttribute("passengerAges", passengerAges);
        model.addAttribute("passengerGenders", passengerGenders);
        model.addAttribute("totalPrice", bookedBus.getPricePerSeat() * seatNumbers.size());
        model.addAttribute("passengerEmail", passengerEmail);

        return "checkout";
    }
    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam("busId") Long busId,
                                 @RequestParam("seatNumbers") String seatNumbersStr,
                                 @RequestParam("passengerNames") List<String> passengerNames,
                                 @RequestParam("passengerAges") List<Integer> passengerAges,
                                 @RequestParam("passengerGenders") List<String> passengerGenders,
                                 @RequestParam("totalPrice") double totalPrice,
                                 HttpSession session,
                                 Model model) {

        Long passengerId = (Long) session.getAttribute("loggedInPassengerId");
        if (passengerId == null) {
            return "redirect:/passenger/passengerlogin";
        }

        Bus bookedBus = busService.getBusById(busId);
        if (bookedBus == null) {
            throw new RuntimeException("Bus not found!");
        }

        List<String> seatNumbers = Arrays.asList(seatNumbersStr.split(","));

        System.out.println("🚀 Booking Confirmed! Passenger Data:");
        for (int i = 0; i < seatNumbers.size(); i++) {
            System.out.println("   - Seat: " + seatNumbers.get(i) +
                    " | Name: " + (i < passengerNames.size() ? passengerNames.get(i) : "MISSING") +
                    " | Age: " + (i < passengerAges.size() ? passengerAges.get(i) : "MISSING") +
                    " | Gender: " + (i < passengerGenders.size() ? passengerGenders.get(i) : "MISSING"));
        }

        Booking booking = bookingService.saveBooking(passengerId, bookedBus, seatNumbers, passengerNames, passengerAges, passengerGenders, totalPrice);
        System.out.println("Booking Confirmed! Booking ID: " + booking.getId());

        model.addAttribute("bookingId", booking.getId());
        model.addAttribute("bus", bookedBus);
        model.addAttribute("seatNumbers", seatNumbers);
        model.addAttribute("passengerList", passengerNames);
        model.addAttribute("passengerAges", passengerAges);
        model.addAttribute("passengerGenders", passengerGenders);
        model.addAttribute("totalPrice", totalPrice);

        return "booking-confirmation"; // Create this page next!
    }
    @PostMapping("/generate-ticket")
    public ResponseEntity<byte[]> generateTicket(@RequestParam("bookingId") Long bookingId) {
        System.out.println("Received Booking ID: " + bookingId);

        Booking booking = bookingService.getBookingWithPassengers(bookingId);
        if (booking == null) {
            throw new RuntimeException(" Error: Booking not found for ID: " + bookingId);
        }

        System.out.println("Booking ID: " + booking.getId());
        System.out.println(" Bus: " + booking.getBus().getTravelsName());
        System.out.println(" Passengers: ");
        for (Passenger passenger : booking.getPassengers()) {
            System.out.println("   - Name: " + passenger.getName() + " | Seat: " + passenger.getSeatNumber());
        }
        byte[] pdfBytes = pdfGeneratorService.generateBookingTicket(booking);

        emailService.sendBookingConfirmation(booking.getEmail(), pdfBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Ticket_" + booking.getId() + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    @GetMapping("/download-ticket/{id}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        byte[] pdfBytes = pdfGeneratorService.generateBookingTicket(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Ticket_" + booking.getId() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }


}
