package mybank.mutualfund.mutualfundmybank.dao.services;

import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailed;
import mybank.mutualfund.mutualfundmybank.dao.exceptions.FundException;
import mybank.mutualfund.mutualfundmybank.dao.remotes.FundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    @Override
    public String callSaveFundAvailed(FundAvailed availed) throws SQLException, FundException {
        String sql = "{call insert_fund_availed(?, ?, ?, ?, ?, ?)}";
        LOGGER.info("Insert fund availed: {}", availed);
        try {
            jdbcTemplate.update(sql,
                    availed.getFundAvailableId(),
                    availed.getAccountId(),
                    availed.getAmtInvested(),
                    availed.getStartDate(),
                    availed.getUnits(),
                    availed.getFundStatus()
            );
            LOGGER.info("Save fund availed: {}", availed);
            return "You have successfully bought the fund"; // Optionally return the saved object
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage());
                // Check for specific error code from the trigger
            if (e.getMessage().contains("-20001")) {
                throw new FundException("User is under 18 years old. Cannot apply for funds.");
            } else if (e.getMessage().contains("-20002")) {
                throw new FundException("Birth date is missing. Please complete your profile.");
            } else if (e.getMessage().contains("-20003")) {
                throw new FundException("Account ID not found.");
            } else {
                throw new FundException("Database error occurred: " + e.getMessage());
            }

        }
    }
}
