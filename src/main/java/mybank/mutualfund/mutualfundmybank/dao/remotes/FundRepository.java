package mybank.mutualfund.mutualfundmybank.dao.remotes;

import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailed;
import mybank.mutualfund.mutualfundmybank.dao.exceptions.FundException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface FundRepository {
    List<FundAvailable> callAllFundAvailable() throws SQLException;
    String callSaveFundAvailed(FundAvailed availed) throws SQLException, FundException;
    List<FundAvailed> callAllFundAvailed(Integer accountId) throws SQLException, FundException;

    String callSaveUpdateFundAvailed(FundAvailed availed);
    String callSellFundAvailed(FundAvailed availed);
    List<FundAvailable> callSearchFunds(String searchTerm) throws SQLException;
}
