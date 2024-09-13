package mybank.mutualfund.mutualfundmybank.dao.services;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import mybank.mutualfund.mutualfundmybank.dao.exceptions.CustomerException;
import mybank.mutualfund.mutualfundmybank.dao.remotes.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerDbRepo implements CustomerRepository, UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean existsByUsername(String username) {
        try {
            String sql = "SELECT COUNT(*) FROM CUSTOMER_LOGIN WHERE username = ?";
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
            return count != null && count > 0;  // If count is greater than 0, username exists
        } catch (Exception e) {
            throw new CustomerException(e.getMessage());
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM CUSTOMER_LOGIN WHERE email = ?";
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
            return count != null && count > 0;  // If count is greater than 0, username exists
        } catch (Exception e) {
            throw new CustomerException(e.getMessage());
        }
    }

    @Override
    public Boolean existsByPhoneNumber(String phoneNumber) {
        try {
            String sql = "SELECT COUNT(*) FROM CUSTOMER_LOGIN WHERE  phone_number= ?";
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{phoneNumber}, Integer.class);
            return count != null && count > 0;  // If count is greater than 0, username exists
        } catch (Exception e) {
            throw new CustomerException(e.getMessage());
        }
    }

    @Override
    public CustomerLogin signingUp(CustomerLogin customerLogin) {
        try{
            String sql = "INSERT INTO CUSTOMER_LOGIN (customer_id, username, password, phone_number, email, customer_status, attempts) VALUES (LOGIN_ID_SEQ.nextval, ?, ?, ?, ?, ?, ?) ";
            int rowCount = jdbcTemplate.update(sql,
                    customerLogin.getUsername(),
                    customerLogin.getPassword(),
                    customerLogin.getPhoneNumber(),
                    customerLogin.getEmail(),
                    customerLogin.getCustomerStatus(),
                    customerLogin.getAttempts());
            if (rowCount == 1) {
                return customerLogin;
            } else {
                // Handle failure to insert customer
                throw new CustomerException("Unable to create account"+new Exception().getMessage());
            }
        } catch (Exception e) {
            throw new CustomerException(e.getMessage());
        }
    }

    @Override
    public CustomerLogin findByUserName(String username) {
        try {
            String sql = "SELECT * FROM CUSTOMER_LOGIN WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(CustomerLogin.class));

        }catch (Exception e){
            throw new CustomerException(e.getMessage());
        }
    }



    @Override
    public void updateAttempts(CustomerLogin customer) {
        jdbcTemplate.update("update CUSTOMER_LOGIN SET ATTEMPTS=? WHERE USERNAME=?",
                customer.getAttempts(), customer.getUsername());

    }

    @Override
    public void updateStatus(CustomerLogin customer) {
        String status= "inactive";
        jdbcTemplate.update("update CUSTOMER_LOGIN set CUSTOMER_STATUS=? where USERNAME=?",
                status, customer.getUsername());

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerLogin customerLogin = findByUserName(username);
        if (customerLogin == null) {
            throw new UsernameNotFoundException(username);
        }
        return customerLogin;
    }
}
