package mybank.mutualfund.mutualfundmybank.webservice.mvc;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerAccount;
import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import mybank.mutualfund.mutualfundmybank.dao.services.CustomerDbRepo;
import mybank.mutualfund.mutualfundmybank.webservice.security.controller.CustomerSignup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ui")
public class MutualfundMVC {
    private String  name;
    private Integer customerId;
    @Autowired
    private CustomerRepository repository;

    @GetMapping("/")
    public String landing() {
        return "index";
    }

    @GetMapping("/dash")
    public String dash(){
        return "dashboard";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/account")
    public String profile(Model model) {
        CustomerAccount customer = repository.findByCustomerId(customerId);
        System.out.println(customer);
        model.addAttribute("customer", customer);
        return "profile";
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exists = repository.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean exists = repository.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/check-phonenumber")
    public ResponseEntity<Map<String, Boolean>> checkPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        boolean exists = repository.existsByPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name")
    @ResponseBody
    public String customerName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        name = authentication.getName();
        CustomerAccount customer = repository.findByUserName(name);
        customerId = customer.getCustomerId();
        return customer.getUsername();
    }

    @PostMapping("/")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "index";
    }
}
