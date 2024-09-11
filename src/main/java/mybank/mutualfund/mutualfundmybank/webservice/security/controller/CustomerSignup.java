package mybank.mutualfund.mutualfundmybank.webservice.security.controller;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/profile")
public class CustomerSignup {

    @Autowired
    CustomerRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public RedirectView registerUser(@RequestParam String username,
                                     @RequestParam String email,
                                     @RequestParam String phoneNumber,
                                     @RequestParam String password,
                                     Model model) {

        try {
            // Create and populate the CustomerLogin object
            CustomerLogin customer = new CustomerLogin();
            customer.setUsername(username);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setPassword(passwordEncoder.encode(password)); // Encrypt the password
            customer.setCustomerStatus("active");
            customer.setAttempts(1);

            // Save the customer in the repository
            CustomerLogin savedCustomer = repository.signingUp(customer);

            // Check if the customer is successfully saved
            if (savedCustomer != null) {
                return new RedirectView("/ui/");
            } else {
                model.addAttribute("error", "Failed to save the user, please try again.");
                return new RedirectView("/ui/signup"); // Redirect to signup page if save failed
            }

        } catch (Exception e) {
            // Handle any exceptions that may occur during the save operation
            model.addAttribute("error", "An error occurred while processing your request.");
            return new RedirectView("/ui/signup"); // Redirect to signup page in case of errors
        }
    }
}
