package com.example.NEWBUS.Service;

import com.example.NEWBUS.Dto.AdminDto;
import com.example.NEWBUS.Entity.Admin;
import com.example.NEWBUS.Repository.AdminRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public AdminServiceImpl(AdminRepository adminRepository, ModelMapper modelMapper) {
        this.adminRepository = adminRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AdminDto saveAdmin(AdminDto adminDto) {
        Admin admin=modelMapper.map(adminDto,Admin.class);
        //Password Encryption Before saving
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        Admin savedAdmin=adminRepository.save(admin);
        return modelMapper.map(savedAdmin,AdminDto.class);
    }

    @Override
    public AdminDto findByEmail(String email) {
        Optional<Admin> optionalAdmin=adminRepository.findByEmail(email);
        return optionalAdmin.map(patient -> modelMapper.map(patient,AdminDto.class)).orElse(null);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        Optional<Admin> existingId=adminRepository.findById(adminId);
        return modelMapper.map(existingId,Admin.class);
    }

    public boolean authenticate(String email,String rawPassword){
        AdminDto adminDto=findByEmail(email);
        return (adminDto!=null && passwordEncoder.matches(rawPassword,adminDto.getPassword()));
    }

}
