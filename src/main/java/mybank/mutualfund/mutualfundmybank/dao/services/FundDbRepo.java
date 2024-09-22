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
        String sql = "SELECT " +
                "FA.fund_Available_Id, " +
                "FA.fund_Name, " +
                "FA.fund_Type_Id, " +
                "FT.fund_Type_Name, " +
                "FA.expense_Ratio, " +
                "FA.nav_Value, " +
                "FA.manager_Id, " +
                "FM.first_Name, " +
                "FM.last_Name, " +
                "FA.exit_Load, " +
                "FA.AUM, " +
                "FA.min_Invest_Amt, " +
                "FA.fund_Company_ID, " +
                "FC.fund_Company_Name " +
                "FROM FUND_AVAILABLE FA " +
                "JOIN FUND_COMPANY FC ON FA.fund_Company_ID = FC.fund_Company_Id " +
                "JOIN FUND_MANAGER FM ON FA.manager_Id = FM.manager_Id " +
                "JOIN FUND_TYPE FT ON FA.fund_Type_Id = FT.fund_Type_Id";

        try {
            fundAvailableList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FundAvailable.class));
            LOGGER.info("Total fund available: {}", fundAvailableList.size());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        if (fundAvailableList.isEmpty()) {
            throw new FundException("There is no fund available");
        }

        return fundAvailableList;
    }
}
