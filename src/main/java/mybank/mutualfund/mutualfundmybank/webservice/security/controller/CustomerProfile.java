package mybank.mutualfund.mutualfundmybank.webservice.security.controller;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerAccount;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;

@RestController
@RequestMapping("/account")
public class CustomerProfile {

    @Autowired
    CustomerRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(CustomerProfile.class);


    @PostMapping("/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam(required = false) String middleName,
                                @RequestParam String lastName,
                                @RequestParam String address,
                                @RequestParam String birthDate,
                                @RequestParam String aadhaarNumber,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String phoneNumber,
                                @RequestParam String email,
                                Model model) {

        // Retrieve the customer account from the repository
        CustomerAccount existingAccount = new CustomerAccount();

        // Update the account fields with new values
        existingAccount.setFirstName(firstName);
        existingAccount.setMiddleName(middleName);
        existingAccount.setLastName(lastName);
        existingAccount.setAddress(address);
        existingAccount.setAadhaarNumber(aadhaarNumber);

        // Convert String to Date using GregorianCalendar
        try {
            // Define the expected date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(birthDate); // Convert String to Date

            // Create a GregorianCalendar object
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(parsedDate); // Set the Date

            // Convert the Date to java.sql.Date
            java.sql.Date sqlBirthDate = new java.sql.Date(gregorianCalendar.getTimeInMillis());

            // Set the birth date in the account object
            existingAccount.setBirthDate(sqlBirthDate);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return "Invalid birth date format. Please use 'yyyy-MM-dd'.";
        }


        // Update login details
        existingAccount.setUsername(username);
        existingAccount.setPassword(password);
        existingAccount.setPhoneNumber(phoneNumber);
        existingAccount.setEmail(email);

        // Save the updated account to the repository
        String account = repository.updateAccount(existingAccount);

        // Check if the update was successful by verifying the object is not null
        if (account == null) {
            return "Error: Account update failed. Please try again.";
        }
        model.addAttribute("account", account);
        return account;
    }

    @GetMapping("/check-password")
    public Boolean checkPassword(@RequestParam String username, @RequestParam String password) {
        // Retrieve the customer account based on the username
        CustomerAccount account = repository.findByUserName(username);

        // Check if account exists
        if (account == null) {
            logger.error("Account not found for username: " + username);
            return false;
        }

        return passwordEncoder.matches(password, account.getPassword());
    }

}
