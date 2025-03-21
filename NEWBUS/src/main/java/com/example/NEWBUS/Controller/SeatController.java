package com.example.NEWBUS.Controller;

import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Service.BusService;
import com.example.NEWBUS.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    private SeatService seatService;
    @Autowired
    private BusService busService;

    @GetMapping("/add")
    public String showSeatForm(@RequestParam("busId") Long busId, Model model) {
        model.addAttribute("busId", busId);
        return "add-seat";
    }

    @PostMapping("/add")
    public String addSeats(@RequestParam("busId") Long busId, @RequestParam("seatCount") int seatCount) {
        seatService.addSeatsToBus(busId, seatCount);
        return "redirect:/bus/list";
    }
    @GetMapping("/list")
    public String viewSeats(@RequestParam("busId") Long busId, Model model) {
        List<Seat> seats = seatService.getSeatsByBus(busId);
        model.addAttribute("seats", seats);
        model.addAttribute("busId", busId);
        return "seat-list"; // Calls seat-list.html
    }
}
