package com.example.NEWBUS.Controller;

import com.example.NEWBUS.Dto.PassengerDto;
import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Repository.PassengerRepository;
import com.example.NEWBUS.Service.BookingService;
import com.example.NEWBUS.Service.PassengerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/passenger")
public class PassengerController {

    private final PassengerService passengerService;
    @Autowired
    private BookingService bookingService;
    private PassengerRepository passengerRepository;

    public PassengerController(PassengerService passengerService,PassengerRepository passengerRepository) {
        this.passengerService = passengerService;
        this.passengerRepository=passengerRepository;
    }
    @GetMapping("/home")
    public String displayIndex(){
        return "index";
    }
    @GetMapping("/dashboard")
    public String displayDashboard(){
        return "home-passenger";
    }
    @GetMapping("/logins")
    public String displayLogins(){
        return "loginhome";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroy session
        return "logout-passenger"; // Redirect to confirmation page
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login-passenger";
    }

    @GetMapping("/passengerlogin")
    public String showPassengerHome(HttpSession session, Model model) {
        Passenger passenger = (Passenger) session.getAttribute("loggedInPassenger");
        System.out.println("Passenger in session: " + (passenger != null ? passenger.getId() : "No passenger found!"));


        if (passenger == null) {
            System.out.println("No passenger found in session. Redirecting to login.");
            return "redirect:/passenger/login";
        }
        System.out.println("Passenger in session: " + passenger.getId() + " | " + passenger.getEmail());
        model.addAttribute("passenger", passenger);
        return "home-passenger";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("passenger", new Passenger());
        return "register-passenger";
    }

    @PostMapping("/save")
    public String savePassenger(@ModelAttribute("passenger") PassengerDto passengerDto, BindingResult result, Model model) {
        Optional<PassengerDto> existingPassengerOpt = Optional.ofNullable(passengerService.findByEmail(passengerDto.getEmail()));
        if (existingPassengerOpt.isPresent()) {
            result.rejectValue("email", "email.exists", "This Email is already registered");
        }
        if (result.hasErrors()) {
            model.addAttribute("passenger", passengerDto);
            return "register-passenger";
        }
        passengerService.savePassenger(passengerDto);
        return "redirect:/passenger/login";
    }
    @PostMapping("/loginWithEmailAndPassword")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {

        if (passengerService.authenticate(email, password)) {
            Optional<Passenger> optionalPassenger = passengerRepository.findByEmail(email);
            if (optionalPassenger.isEmpty()) {
                System.out.println(" Error: No passenger found for email: " + email);
                model.addAttribute("error", "No passenger found with this email.");
                return "login-passenger";
            }

            Passenger passenger = optionalPassenger.get();

            session.setAttribute("loggedInPassenger", passenger);
            session.setAttribute("loggedInPassengerId", passenger.getId());
            session.setAttribute("loggedInUser", email);

            System.out.println(" Logged-in Passenger Stored in Session: " + passenger.getName());
            return "redirect:/passenger/passengerlogin";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login-passenger";
        }
    }
    @GetMapping("/history")
    public String bookingHistory(HttpSession session, Model model) {
        Passenger passenger = (Passenger) session.getAttribute("loggedInPassenger");

        if (passenger == null) {
            System.out.println(" No passenger found in session. Redirecting to login.");
            return "redirect:/passenger/login"; // Redirect to login if not logged in
        }

        List<Booking> bookings = bookingService.findByPassengerId(passenger.getId());
        model.addAttribute("bookings", bookings);

        return "booking-history";
    }
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        Passenger passenger = (Passenger) session.getAttribute("loggedInPassenger");

        if (passenger == null) {
            System.out.println(" No passenger found in session. Redirecting to login.");
            return "redirect:/passenger/login";
        }
        System.out.println(" Passenger found in session: " + passenger.getName()); // Debugging
        model.addAttribute("passenger", passenger);
        return "profile-passenger";
    }






}