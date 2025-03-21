package com.example.NEWBUS.Config;

import com.example.NEWBUS.Service.SeatService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public SeatService seatService() {
        return new SeatService();
    }
}
