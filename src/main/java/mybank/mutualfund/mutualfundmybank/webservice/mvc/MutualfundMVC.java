package mybank.mutualfund.mutualfundmybank.webservice.mvc;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class MutualfundMVC {
    @GetMapping("/")
    public String landing() {
        return "index";
    }

    @GetMapping("/dash")
    public String dash(){
        return "dashboard";
    }

    @PostMapping("/")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "index";
    }
}
