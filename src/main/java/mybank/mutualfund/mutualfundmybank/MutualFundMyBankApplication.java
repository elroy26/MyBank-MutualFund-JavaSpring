package mybank.mutualfund.mutualfundmybank;

import mybank.mutualfund.mutualfundmybank.dao.services.CustomerDbRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MutualFundMyBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutualFundMyBankApplication.class, args);
//        ConfigurableApplicationContext context=  SpringApplication.run(MutualFundMyBankApplication.class, args);
//
//        CustomerDbRepo repo = context.getBean(CustomerDbRepo.class);
//        String name="elroy";
//        System.out.println(repo.findByUserName(name));
    }

}
