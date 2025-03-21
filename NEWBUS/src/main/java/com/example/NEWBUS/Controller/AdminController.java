package com.example.NEWBUS.Controller;

import com.example.NEWBUS.Dto.AdminDto;
import com.example.NEWBUS.Service.AdminService;
import com.example.NEWBUS.Service.BusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroy session
        return "logout-admin"; // Redirect to confirmation page
    }
    @GetMapping("/about")
    public String displayAbout(){
        return "about";
    }
    @GetMapping("/services")
    public String displayServices(){
        return "services";
    }
    @GetMapping("/adminlogin")
    public String displayAdminLogin(){
        return "login-admin";
    }

    @GetMapping("/adminhome")
    public String displayAdminHome(){
        return "home-admin";
    }

    @GetMapping("/adminregistration")
    public String showRegistrationForm(Model model) {
        AdminDto adminDto=new AdminDto();
        model.addAttribute("admin", adminDto);
        return "register-admin"; // This should match your Thymeleaf template name
    }
    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute("admin") AdminDto adminDto, BindingResult result,Model model) {
        Optional<AdminDto> existingAdminOpt=Optional.ofNullable(adminService.findByEmail(adminDto.getEmail()));
        if(existingAdminOpt.isPresent()){
            result.rejectValue("email","email.exist","This Email is already Exist");
        }
        if(result.hasErrors()){
            model.addAttribute("admin",adminDto);
            return "register-admin";
        }
        adminService.saveAdmin(adminDto);
        return "redirect:/admin/adminlogin"; // Redirect to a success page
    }
    @PostMapping("/loginWithEmailAndPassword")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        if (adminService.authenticate(email, password)) {
            session.setAttribute("loggedInAdmin", email);
            return "redirect:/admin/adminhome"; // Redirect to homepage after login
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login-admin"; // Show login page again with an error message
        }
    }

}
