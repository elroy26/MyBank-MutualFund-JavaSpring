package mybank.mutualfund.mutualfundmybank.webservice.security.controller;

import mybank.mutualfund.mutualfundmybank.dao.entity.AvailableFunds;
import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerAccount;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailed;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import mybank.mutualfund.mutualfundmybank.dao.remotes.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/fund")
public class FundController {

    @Autowired
    private FundRepository fundRepository;


    @GetMapping("/fundAvailable")
    public String getAllFundAvailable(Model model) {
        try {
            List<FundAvailable> fundAvailable = fundRepository.callAllFundAvailable();
            model.addAttribute("fundAvailable", fundAvailable);  // Add the funds to the model
        } catch (SQLException e) {
            model.addAttribute("error", "Error fetching funds: " + e.getMessage());
        }
        return "fundAvailable";
    }

}
