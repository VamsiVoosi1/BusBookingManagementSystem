package com.example.NEWBUS.Service;

import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BusService{
    @Autowired
    private BusRepository busRepository;


    public void addBus(Bus bus) {
        busRepository.save(bus);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    public List<Bus> searchBuses(String fromLocation, String toLocation, LocalDate travelDate) {
        return busRepository.findByFromLocationAndToLocationAndTravelDate(fromLocation, toLocation, travelDate);
    }
    public Bus getBusById(Long busId) {
        return busRepository.findById(busId).orElse(null);
    }


}
