package mybank.mutualfund.mutualfundmybank.webservice.mvc;


import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import mybank.mutualfund.mutualfundmybank.dao.services.CustomerDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/ui")
public class MutualfundMVC {
    @Autowired
    private CustomerRepository customerDbRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String phoneNumber,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup"; // Return to the signup page with an error message
        }

        CustomerLogin customer = new CustomerLogin();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber); // Ensure this field exists in your entity
        customer.setPassword(passwordEncoder.encode(password)); // Encrypt the password
        customer.setCustomerStatus("active");
        customer.setAttempts(1);

        customerDbRepo.signingUp(customer);

        return "redirect:/ui/"; // Redirect to login page or another page upon successful registration
    }

    @PostMapping("/")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "index";
    }
}
