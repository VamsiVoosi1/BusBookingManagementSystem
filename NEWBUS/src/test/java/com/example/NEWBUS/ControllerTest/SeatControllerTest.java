package com.example.NEWBUS.ControllerTest;

import com.example.NEWBUS.Controller.SeatController;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Service.BusService;
import com.example.NEWBUS.Service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatControllerTest {

    @Mock
    private SeatService seatService;

    @Mock
    private BusService busService;

    @Mock
    private Model model;

    @InjectMocks
    private SeatController seatController;

    private List<Seat> testSeats;

    @BeforeEach
    void setUp() {
        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setSeatNumber("A1");

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("A2");

        testSeats = Arrays.asList(seat1, seat2);
    }

    @Test
    void testShowSeatForm() {
        String viewName = seatController.showSeatForm(1L, model);

        assertEquals("add-seat", viewName);
        verify(model, times(1)).addAttribute("busId", 1L);
    }

    @Test
    void testAddSeats() {
        String viewName = seatController.addSeats(1L, 10);

        assertEquals("redirect:/bus/list", viewName);
        verify(seatService, times(1)).addSeatsToBus(1L, 10);
    }

    @Test
    void testViewSeats() {
        when(seatService.getSeatsByBus(1L)).thenReturn(testSeats);

        String viewName = seatController.viewSeats(1L, model);

        assertEquals("seat-list", viewName);
        verify(model, times(1)).addAttribute("seats", testSeats);
        verify(model, times(1)).addAttribute("busId", 1L);
    }
}
