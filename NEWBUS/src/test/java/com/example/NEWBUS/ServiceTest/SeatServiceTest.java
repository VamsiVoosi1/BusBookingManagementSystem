package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Repository.BusRepository;
import com.example.NEWBUS.Repository.SeatRepository;
import com.example.NEWBUS.Service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private SeatService seatService;

    private Bus bus;
    private Seat seat1;
    private Seat seat2;

    @BeforeEach
    void setUp() {
        bus = new Bus();
        bus.setId(1L);

        seat1 = new Seat();
        seat1.setId(1L);
        seat1.setBus(bus);
        seat1.setSeatNumber("S1");
        seat1.setBooked(false);

        seat2 = new Seat();
        seat2.setId(2L);
        seat2.setBus(bus);
        seat2.setSeatNumber("S2");
        seat2.setBooked(false);
    }

    @Test
    void testAddSeat() {
        when(seatRepository.save(seat1)).thenReturn(seat1);

        Seat savedSeat = seatService.addSeat(seat1);

        assertNotNull(savedSeat);
        assertEquals("S1", savedSeat.getSeatNumber());
        verify(seatRepository, times(1)).save(seat1);
    }

    @Test
    void testAddSeatsToBus_Success() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.findHighestSeatNumber(1L)).thenReturn(2);

        seatService.addSeatsToBus(1L, 3);

        verify(seatRepository, times(3)).save(any(Seat.class));
    }

    @Test
    void testAddSeatsToBus_BusNotFound() {
        when(busRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                seatService.addSeatsToBus(2L, 3));

        assertEquals("Bus not found", exception.getMessage());
    }

    @Test
    void testGetSeatsByBus_Found() {
        when(seatRepository.findByBusId(1L)).thenReturn(Arrays.asList(seat1, seat2));

        List<Seat> seats = seatService.getSeatsByBus(1L);

        assertNotNull(seats);
        assertEquals(2, seats.size());
        verify(seatRepository, times(1)).findByBusId(1L);
    }

    @Test
    void testGetSeatsByBusId_Found() {
        when(seatRepository.findByBusId(1L)).thenReturn(Arrays.asList(seat1, seat2));

        List<Seat> seats = seatService.getSeatsByBusId(1L);

        assertNotNull(seats);
        assertEquals(2, seats.size());
        verify(seatRepository, times(1)).findByBusId(1L);
    }
}
