package com.example.NEWBUS.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private String phone;
    private String password;


}
