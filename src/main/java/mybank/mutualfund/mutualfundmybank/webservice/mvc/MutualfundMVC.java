package mybank.mutualfund.mutualfundmybank.webservice.mvc;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerAccount;
import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailed;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import mybank.mutualfund.mutualfundmybank.dao.remotes.FundRepository;
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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @PostMapping("/applyFund")
    public String saveAppliedFund(Model model,
                                  @RequestParam Integer fundAvailableId,
                                  @RequestParam String startDate,
                                  @RequestParam Double amtInvested,
                                  @RequestParam Double navValue
    ) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(startDate); // Convert String to Date

            // Create a GregorianCalendar object
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(parsedDate); // Set the Date

            // Convert the Date to java.sql.Date
            java.sql.Date sqlStartDate = new java.sql.Date(gregorianCalendar.getTimeInMillis());

            FundAvailed availed = new FundAvailed();
            Double units=amtInvested/navValue;

            availed.setFundAvailableId(fundAvailableId);
            availed.setAccountId(accountId);
            availed.setStartDate(sqlStartDate);
            availed.setAmtInvested(amtInvested);
            availed.setUnits(units);
            availed.setFundStatus(0);

            String fundAvailed = fundRepository.callSaveFundAvailed(availed);
            model.addAttribute("fundAvailed", fundAvailed);  // Add the funds to the model
        } catch (SQLException e) {
            model.addAttribute("error", "Error buying funds: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return "fundAvailed";
    }

}
