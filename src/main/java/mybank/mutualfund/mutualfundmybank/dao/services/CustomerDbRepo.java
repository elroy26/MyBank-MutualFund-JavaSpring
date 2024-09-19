package mybank.mutualfund.mutualfundmybank.dao.services;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerAccount;
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

    private Integer customerId;
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
            CustomerAccount account=findByUserName(customerLogin.getUsername());
            String insertCustomerAccountQuery = "INSERT INTO CUSTOMER_ACCOUNT (ACCOUNT_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ADDRESS, BIRTH_DATE, AADHAAR_NUMBER, CUSTOMER_ID) VALUES (ACCOUNT_ID_SEQ.NEXTVAL, NULL, NULL, NULL, NULL, NULL, NULL, ?)";
            int rowCount1=jdbcTemplate.update(insertCustomerAccountQuery, account.getCustomerId());

            if (rowCount == 1 && rowCount1 == 1) {
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
    public CustomerAccount findByCustomerId(Integer customerId) {
            try {
                String sql = "SELECT cl.customer_id, cl.username, cl.password, cl.phone_number, cl.email, " +
                        "cl.customer_status, cl.attempts, ca.first_name, ca.middle_name, ca.last_name, " +
                        "ca.address, ca.birth_date, ca.aadhaar_number, ca.account_id " +
                        "FROM CUSTOMER_LOGIN cl " +
                        "JOIN CUSTOMER_ACCOUNT ca ON cl.customer_id = ca.customer_id " +
                        "WHERE cl.customer_id = ?";
                return jdbcTemplate.queryForObject(sql, new Object[]{customerId}, (rs, rowNum) -> {
                    CustomerAccount account = new CustomerAccount();
                    account.setAccountId(rs.getInt("account_id"));
                    account.setCustomerId(rs.getInt("customer_id"));
                    account.setUsername(rs.getString("username"));
                    account.setPassword(rs.getString("password"));
                    account.setPhoneNumber(rs.getString("phone_number"));
                    account.setEmail(rs.getString("email"));
                    account.setCustomerStatus(rs.getString("customer_status"));
                    account.setAttempts(rs.getInt("attempts"));
                    account.setFirstName(rs.getString("first_name"));
                    account.setMiddleName(rs.getString("middle_name"));
                    account.setLastName(rs.getString("last_name"));
                    account.setAddress(rs.getString("address"));
                    account.setBirthDate(rs.getDate("birth_date"));
                    account.setAadhaarNumber(rs.getString("aadhaar_number"));
                    return account;
                });
            } catch (Exception e) {
                throw new CustomerException(e.getMessage());
            }
    }

    @Override
    public CustomerAccount findByUserName(String username) {
        try {
            String sql = "SELECT * FROM CUSTOMER_LOGIN WHERE username = ?";
            CustomerAccount login=jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(CustomerAccount.class));
            customerId=login.getCustomerId();
            return login;

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
    public String updateAccount(CustomerAccount customerAccount) {
        try {
                // Update the CUSTOMER_ACCOUNT table
                String updateCustomerAccountQuery = "UPDATE CUSTOMER_ACCOUNT SET FIRST_NAME = ?, MIDDLE_NAME = ?, LAST_NAME = ?, ADDRESS = ?, BIRTH_DATE = ?, AADHAAR_NUMBER = ? WHERE CUSTOMER_ID = ?";
                jdbcTemplate.update(updateCustomerAccountQuery,
                        customerAccount.getFirstName(),
                        customerAccount.getMiddleName(),
                        customerAccount.getLastName(),
                        customerAccount.getAddress(),
                        customerAccount.getBirthDate(),
                        customerAccount.getAadhaarNumber(),
                        customerId);
                String updateCustomerLoginQuery = "UPDATE CUSTOMER_LOGIN SET USERNAME = ?, PHONE_NUMBER = ?, EMAIL = ? WHERE CUSTOMER_ID = ?";
                jdbcTemplate.update(updateCustomerLoginQuery,
                        customerAccount.getUsername(),
                        customerAccount.getPhoneNumber(),
                        customerAccount.getEmail(),
                        customerId);
                return "Your account has been updated successfully";


        } catch (Exception ex) {
            System.err.println("An error occurred while updating the account: " + ex.getMessage());
            throw new CustomerException(ex.getMessage());

        }
    }


    @Override
    public Boolean isAccountExists(String username) {
        try {
            // Try to find the customer login by username
            CustomerLogin customerLogin = findByUserName(username);


            // Check if the customerLogin object is null
            if (customerLogin == null || customerLogin.getCustomerId() == null) {
                throw new CustomerException("Customer with username: " + username + " not found.");
            }

            // If customerLogin is not null, proceed with setting the customerId and returning true
            customerLogin.setCustomerId(customerLogin.getCustomerId());
            return true;

        } catch (CustomerException ex) {
            // Handle exception if customer is not found
            System.err.println(ex.getMessage()); // You can log this instead of printing
            return false;
        } catch (Exception ex) {
            // Catch any other exceptions that might occur
            System.err.println("An error occurred while checking if account exists: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updatePassword(String password) {
        try {
            // Update the CUSTOMER_LOGIN table for the password
            String updatePasswordQuery = "UPDATE CUSTOMER_LOGIN SET PASSWORD = ? WHERE CUSTOMER_ID = ?";
            jdbcTemplate.update(updatePasswordQuery, password, customerId);
            return true;
        } catch (Exception ex) {
            System.err.println("An error occurred while updating the password: " + ex.getMessage());
            throw new CustomerException(ex.getMessage());
        }
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
