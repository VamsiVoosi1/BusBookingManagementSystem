package com.example.NEWBUS.Service;

import com.example.NEWBUS.Dto.PassengerDto;
import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Repository.BookingRepository;
import com.example.NEWBUS.Repository.PassengerRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    public void registerPassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }


    public Passenger loginPassenger(String email, String password) {
        return passengerRepository.findByEmailAndPassword(email, password);
    }

    public PassengerDto findByEmail(String email) {
        Optional<Passenger> optionalPassenger = passengerRepository.findByEmail(email);
        return optionalPassenger.map(passenger -> modelMapper.map(passenger, PassengerDto.class)).orElse(null);
    }
    public PassengerDto savePassenger(PassengerDto passengerDto) {
        Passenger passenger = modelMapper.map(passengerDto, Passenger.class);
        passenger.setPassword(passwordEncoder.encode(passengerDto.getPassword())); // Encrypt password
        Passenger savedPassenger = passengerRepository.save(passenger);
        return modelMapper.map(savedPassenger, PassengerDto.class);
    }
    public boolean authenticate(String email, String rawPassword) {
        PassengerDto passengerDto = findByEmail(email);
        return (passengerDto != null && passwordEncoder.matches(rawPassword, passengerDto.getPassword()));
    }



}
