package mybank.mutualfund.mutualfundmybank.dao.remotes;

import mybank.mutualfund.mutualfundmybank.dao.entity.CustomerLogin;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository {
    CustomerLogin signingUp(CustomerLogin customerLogin);
    CustomerLogin findByUserName(String username);
    void updateAttempts(CustomerLogin customer);
    void updateStatus(CustomerLogin customer);
}
