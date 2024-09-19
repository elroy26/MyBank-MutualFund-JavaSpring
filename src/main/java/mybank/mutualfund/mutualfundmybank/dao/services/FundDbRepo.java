package mybank.mutualfund.mutualfundmybank.dao.services;

import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.exceptions.FundException;
import mybank.mutualfund.mutualfundmybank.dao.remotes.FundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FundDbRepo implements FundRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    Logger LOGGER = LoggerFactory.getLogger(FundDbRepo.class);


    @Override
    public List<FundAvailable> callAllFundAvailable() throws SQLException {
        List<FundAvailable> fundAvailableList = null;
        try {
            fundAvailableList=jdbcTemplate.query("SELECT * FROM FUND_AVAILABLE", new BeanPropertyRowMapper<>(FundAvailable.class));
            LOGGER.info("Total fund available: "+fundAvailableList.size());
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
        if (fundAvailableList.isEmpty()) {
            throw new FundException("there is no fund available");
        }
        return fundAvailableList;
    }
}
