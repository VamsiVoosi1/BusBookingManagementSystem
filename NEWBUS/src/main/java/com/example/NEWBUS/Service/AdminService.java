package com.example.NEWBUS.Service;

import com.example.NEWBUS.Dto.AdminDto;
import com.example.NEWBUS.Entity.Admin;
import org.springframework.stereotype.Service;

public interface AdminService {
    AdminDto saveAdmin(AdminDto adminDto);
    AdminDto findByEmail(String email);
    Admin getAdminById(Long adminId);
    boolean authenticate(String email,String rawPassword);
}
