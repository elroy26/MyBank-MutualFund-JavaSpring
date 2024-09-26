package mybank.mutualfund.mutualfundmybank.dao.exceptions;

import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

public class FundException extends RuntimeException {
    public FundException(String message) {
        super(message);
    }

    public FundException(String message, SQLException sqlEx) {
    }

    public FundException(String message, DataAccessException e) {
    }
}
