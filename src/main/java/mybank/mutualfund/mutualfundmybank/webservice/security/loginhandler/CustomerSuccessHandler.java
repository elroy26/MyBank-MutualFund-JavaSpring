package mybank.mutualfund.mutualfundmybank.webservice.security.loginhandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomerSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    CustomerRepository service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomerLogin customer = (CustomerLogin) authentication.getPrincipal();
        service.isAccountExists(customer.getUsername());
        if (!customer.getCustomerStatus().equals("inactive")) {
            if(customer.getAttempts()>1)
            {
                customer.setAttempts(1);
                logger.warn("updating the attempts.");
                service.updateAttempts(customer);
            }
            super.setDefaultTargetUrl("/ui/dash");

        } else {
            logger.warn("Max Attempts reached, account is suspended. Contact admin !!");
            super.setDefaultTargetUrl("/ui/?error="+"Max Attempts reached, account is suspended. Contact admin !!");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
