package mybank.mutualfund.mutualfundmybank;

import mybank.mutualfund.mutualfundmybank.dao.services.CustomerDbRepo;
import mybank.mutualfund.mutualfundmybank.dao.services.FundDbRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class MutualFundMyBankApplication {

    public static void main(String[] args) throws SQLException {
//        SpringApplication.run(MutualFundMyBankApplication.class, args);
        ConfigurableApplicationContext context=  SpringApplication.run(MutualFundMyBankApplication.class, args);

        FundDbRepo repo = context.getBean(FundDbRepo.class);
        System.out.println(repo.callAllFundAvailable());
    }

}
