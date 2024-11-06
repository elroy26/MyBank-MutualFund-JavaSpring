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

import java.sql.*;
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

    @Override
    public List<FundAvailed> callAllFundAvailed(Integer accountId) throws SQLException, FundException {
        List<FundAvailed> fundAvailedList = new ArrayList<>();
        String sql = "SELECT fa.FUND_NAME, fav.AMT_INVESTED, fav.UNITS, fa.NAV_VALUE, ft.FUND_TYPE_NAME, fav.fund_available_id " +
                "FROM FUND_AVAILABLE fa " +
                "JOIN FUND_TYPE ft ON ft.fund_type_id = fa.fund_type_id " +
                "JOIN FUND_AVAILED fav ON fa.FUND_AVAILABLE_ID = fav.FUND_AVAILABLE_ID " +
                "WHERE fav.ACCOUNT_ID = ? AND fav.FUND_STATUS = 'active'";

        try {
            // Execute the query and map the result set to FundAvailed objects
            fundAvailedList = jdbcTemplate.query(sql, new Object[]{accountId}, (rs, rowNum) -> {
                FundAvailed fundAvailed = new FundAvailed();
                fundAvailed.setFundAvailableId(rs.getInt("FUND_AVAILABLE_ID"));
                fundAvailed.setFundTypeName(rs.getString("FUND_TYPE_NAME"));
                fundAvailed.setFundName(rs.getString("FUND_NAME"));
                fundAvailed.setAmtInvested(rs.getDouble("AMT_INVESTED"));
                fundAvailed.setUnits(rs.getDouble("UNITS"));
                fundAvailed.setNavValue(rs.getFloat("NAV_VALUE"));
                return fundAvailed;
            });

            if (fundAvailedList.isEmpty()) {
                throw new FundException("ERR-FA -No availed funds found for the provided account ID.");
            }

            return fundAvailedList;

        } catch (DataAccessException e) {
            // Handle specific SQL error cases
            LOGGER.error("Database error occurred while fetching availed funds: {}", e.getMessage());
                // Generic database error
            throw new FundException("Database error occurred: " + e.getMessage());


        } catch (FundException e) {
            LOGGER.error("Error: {}", e.getMessage());
            throw e;  // Re-throw to maintain exception handling

        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred: {}", e.getMessage());
            throw new FundException("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public String callSaveUpdateFundAvailed(FundAvailed availed) {

        String sql = "UPDATE FUND_AVAILED " +
                "SET amt_invested = ?, units = ? " +
                "WHERE fund_available_id = ? AND account_id = ? AND FUND_STATUS='active'";

        try {

            jdbcTemplate.update(sql,
                    availed.getAmtInvested(),      // Updated investment amount
                    availed.getUnits(),            // Updated units (calculated)
                    availed.getFundAvailableId(),  // Fund available ID to identify the fund
                    availed.getAccountId()     // Fund availed ID to uniquely identify the record
            );

            return "Fund details updated successfully!!!";
        } catch (Exception e) {
            // Handle and log the exception, and return an error message
            return "Error updating fund availed: " + e.getMessage();
        }
    }

    @Override
    public String callSellFundAvailed(FundAvailed availed) {
        String sql = "UPDATE FUND_AVAILED " +
                "SET FUND_STATUS = ?, END_DATE = ? " +
                "WHERE FUND_AVAILABLE_ID = ? AND ACCOUNT_ID = ? AND FUND_STATUS='active'";
        try{
            jdbcTemplate.update(sql,
                    availed.getFundStatus(),
                    availed.getEndDate(),
                    availed.getFundAvailableId(),
                    availed.getAccountId());
            return "Your redemption request has been successfully submitted.";
        }catch (Exception e){
            return "Error selling the funds: " + e.getMessage();
        }
    }

    @Override
    public List<FundAvailable> callSearchFunds(String searchTerm) throws SQLException {
        List<FundAvailable> funds = new ArrayList<>();

        jdbcTemplate.execute((Connection conn) -> {
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                // Prepare and execute the stored procedure call
                cs = conn.prepareCall("{ call searchFundsKmp(?, ?) }"); // Call searchFundsKmp procedure
                cs.setString(1, searchTerm);  // Set input parameter
                cs.registerOutParameter(2, Types.REF_CURSOR);  // Register output parameter as CURSOR

                cs.execute();  // Execute the stored procedure

                // Retrieve the cursor and iterate over the result set
                rs = (ResultSet) cs.getObject(2);
                while (rs.next()) {
                    FundAvailable fund = new FundAvailable();
                    fund.setFundName(rs.getString("fund_name"));
                    fund.setFundTypeName(rs.getString("fund_type_name"));
                    funds.add(fund);
                }
            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }
            return null;
        });

        return funds;
    }


}
