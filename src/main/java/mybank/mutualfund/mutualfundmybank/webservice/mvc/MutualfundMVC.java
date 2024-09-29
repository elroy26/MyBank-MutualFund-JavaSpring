package mybank.mutualfund.mutualfundmybank.webservice.mvc;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerAccount;
import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailed;
import mybank.mutualfund.mutualfundmybank.dao.exceptions.FundException;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import mybank.mutualfund.mutualfundmybank.dao.remotes.FundRepository;
import mybank.mutualfund.mutualfundmybank.dao.services.CustomerDbRepo;
import mybank.mutualfund.mutualfundmybank.webservice.security.controller.CustomerSignup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/ui")
public class MutualfundMVC {
    private String  name;
    private Integer customerId;
    private Integer accountId;
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private FundRepository fundRepository;

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
        accountId = customer.getAccountId();
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
        CustomerAccount customer1 = repository.findByCustomerId(customerId);
        accountId = customer1.getAccountId();
        return customer.getUsername();
    }

    @PostMapping("/")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "index";
    }

    @GetMapping("/apply/{id}")
    public String applyToFund(@PathVariable("id") Integer fundId, Model model) {
        // Fetch the fund details if needed
        try {
            List<FundAvailable> fund = fundRepository.callAllFundAvailable(); // Implement this method
            model.addAttribute("fund", fund);
            return "applyIMutualFund"; // Thymeleaf template for applying
        } catch (Exception e) {
            model.addAttribute("error", "Unable to apply to the fund: " + e.getMessage());
            return "redirect:/fund/fundAvailable?error";
        }
    }

    @PostMapping("/updateFund")
    public ResponseEntity<Object> saveUpdateFund(@RequestParam Float navValue,
                                                 @RequestParam Double amtInvested,
                                                 @RequestParam Double updatedAmt,
                                                 @RequestParam Integer fundAvailableId){
            FundAvailed availed = new FundAvailed();
            availed.setAccountId(accountId);
            availed.setFundAvailableId(fundAvailableId);
            Double totalAmt = amtInvested + updatedAmt;
            Double units=totalAmt/navValue;
            availed.setUnits(units);

            String fundAvailedMessage = fundRepository.callSaveUpdateFundAvailed(availed);
            return ResponseEntity.ok(fundAvailedMessage);

    }
    @PostMapping("/applyFund")
    public ResponseEntity<Object> saveAppliedFund(@RequestBody FundAvailed availed) {

        try {
            // Calculate the units
            Double units = availed.getAmtInvested() / availed.getNavValue();

            // Set necessary fields
            availed.setAmtInvested(availed.getAmtInvested());
            availed.setAccountId(accountId); // Ensure this is set from session or security context
            availed.setStartDate(availed.getStartDate());
            availed.setUnits(units);
            availed.setFundStatus("active"); // Assuming '0' is the status for 'pending' or similar

            // Call the repository to save the fund details
            String fundAvailedMessage = fundRepository.callSaveFundAvailed(availed);

            // Return success message with HTTP 200
            return ResponseEntity.ok(fundAvailedMessage);

        } catch (FundException e) {
            if(e.getMessage().contains("-20001")) {
                // Return a 400 Bad Request with an error message
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is under 18 years old. Cannot process the fund application.");
            }else if(e.getMessage().contains("-20002")) {
                // Return a 400 Bad Request with an error message
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Birth date is missing. Please fill profile details to buy funds.");
            }else {
                return ResponseEntity.badRequest().body("Error buying funds: " + e.getMessage());
            }
        } catch (DateTimeParseException e) {
            // Handle date parsing issues
            return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");

        } catch (SQLException e) {
            // Handle SQL-related exceptions
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
    @GetMapping("/portfolio")
    public String getAvailedFunds(Model model) {
        try {
            List<FundAvailed> availedFunds = fundRepository.callAllFundAvailed(accountId);
            model.addAttribute("availedFunds", availedFunds);  // Add the funds to the model

            // Return the portfolio view
            return "holdings";

        } catch (FundException e) {
            if (e.getMessage().contains("ERR-FA")) {
                model.addAttribute("error", "You haven't invested in any funds currently");
                return "holdings";
            } else {
                model.addAttribute("error", "Error buying funds: " + e.getMessage());
                return "holdings";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred.");
            return "holdings";
        }
    }

}
