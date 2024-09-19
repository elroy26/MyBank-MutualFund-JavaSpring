package mybank.mutualfund.mutualfundmybank.dao.remotes;

import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface FundRepository {
    List<FundAvailable> callAllFundAvailable() throws SQLException;
}
