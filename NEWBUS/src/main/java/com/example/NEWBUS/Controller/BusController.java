package com.example.NEWBUS.Controller;

import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Service.BusService;
import com.example.NEWBUS.Service.SeatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/bus")
public class BusController {
@Autowired
    private BusService busService;
@Autowired
    private SeatService seatService;
    @GetMapping("/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "add-bus";
    }

    @PostMapping("/add")
    public String addBus(@ModelAttribute Bus bus) {
        busService.addBus(bus);
        return "redirect:/bus/list";
    }
    @PostMapping("/save")
    public String saveBus(@ModelAttribute Bus bus) {
        busService.saveBus(bus);
        return "redirect:/bus/search";
    }
    @GetMapping("/list")
    public String showBusList(Model model) {
        List<Bus> buses = busService.getAllBuses();
        model.addAttribute("buses", buses);
        return "bus-list";
    }
    @GetMapping("/search")
    public String searchBuses(@RequestParam(required = false) String fromLocation,
                              @RequestParam(required = false) String toLocation,
                              @RequestParam(required = false) LocalDate travelDate,
                              Model model) {
        List<Bus> buses = null; // Initially no buses displayed

        // Only search if all parameters are provided
        if (fromLocation != null && toLocation != null && travelDate != null) {
            buses = busService.searchBuses(fromLocation, toLocation, travelDate);
        }

        model.addAttribute("buses", buses);
        return "search-bus"; // Calls bus-search.html
    }
    @GetMapping("/book-seats")
    public String bookSeats(@RequestParam("busId") Long busId, HttpSession session, Model model) {
        String passengerEmail = (String) session.getAttribute("loggedInUser");


        if (passengerEmail == null) {
            System.out.println(" Error: No passenger logged in!");
            return "redirect:/passenger/login"; // Redirect to login if session is empty
        }

        System.out.println("Passenger Email from session: " + passengerEmail);

        Bus bus = busService.getBusById(busId);
        if (bus == null) {
            throw new RuntimeException("Bus not found!");
        }

        List<Seat> seats = seatService.getSeatsByBusId(busId);

        model.addAttribute("bus", bus);
        model.addAttribute("seats", seats);
        model.addAttribute("passengerEmail", passengerEmail);

        return "seat-selection";
    }
}