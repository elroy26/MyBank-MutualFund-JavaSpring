package mybank.mutualfund.mutualfundmybank.webservice.security.loginhandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.exceptions.CustomerException;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomerFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    CustomerRepository service;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        try {
            CustomerLogin customer = service.findByUserName(username);
            if (customer != null) {
                if (!customer.getCustomerStatus().equals("inactive")) {
                    if (customer.getAttempts() < customer.getMaxAttempts()) {
                        customer.setAttempts(customer.getAttempts() + 1);
                        service.updateAttempts(customer);
                        exception = new LockedException((4 - customer.getAttempts()) + " " + "Attempts left.");
                        String err = customer.getAttempts() + " " + exception.getMessage();
                        logger.warn(err);
                        super.setDefaultFailureUrl("/ui/?error=" + exception.getMessage());
                    } else {
                        service.updateStatus(customer);
                        exception = new LockedException("Max Attempts reached, account is suspended. Contact admin !!");
                        super.setDefaultFailureUrl("/ui/?error=" + exception.getMessage());
                    }
                } else {
                    exception = new LockedException("Account suspended contact admin to activate.");
                    super.setDefaultFailureUrl("/ui/?error=" + exception.getMessage());
                }
            }

        } catch (CustomerException e) {
            exception = new LockedException("User not found with this username.");
            super.setDefaultFailureUrl("/ui/?error=" + exception.getMessage());
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
