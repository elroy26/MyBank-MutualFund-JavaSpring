package mybank.mutualfund.mutualfundmybank.dao.entity;

import java.util.Date;

public class FundAvailed extends FundAvailable {
    private Integer fundAvailedId;
    private Integer fundAvailableId;
    private Integer accountId;
    private Double amtInvested;
    private Date startDate;
    private Date endDate;
    private Integer units;
    private Integer fundStatus;

    public FundAvailed() {
    }

    @Override
    public String toString() {
        return "FundAvailed{" +
                "fundAvailedId=" + fundAvailedId +
                ", fundAvailableId=" + fundAvailableId +
                ", accountId=" + accountId +
                ", amtInvested=" + amtInvested +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", units=" + units +
                ", fundStatus=" + fundStatus +
                '}';
    }

    public Integer getFundAvailedId() {
        return fundAvailedId;
    }

    public void setFundAvailedId(Integer fundAvailedId) {
        this.fundAvailedId = fundAvailedId;
    }

    public Integer getFundAvailableId() {
        return fundAvailableId;
    }

    public void setFundAvailableId(Integer fundAvailableId) {
        this.fundAvailableId = fundAvailableId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Double getAmtInvested() {
        return amtInvested;
    }

    public void setAmtInvested(Double amtInvested) {
        this.amtInvested = amtInvested;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Integer getFundStatus() {
        return fundStatus;
    }

    public void setFundStatus(Integer fundStatus) {
        this.fundStatus = fundStatus;
    }

    public FundAvailed(Integer fundAvailedId, Integer fundAvailableId, Integer accountId, Double amtInvested, Date startDate, Date endDate, Integer units, Integer fundStatus) {
        this.fundAvailedId = fundAvailedId;
        this.fundAvailableId = fundAvailableId;
        this.accountId = accountId;
        this.amtInvested = amtInvested;
        this.startDate = startDate;
        this.endDate = endDate;
        this.units = units;
        this.fundStatus = fundStatus;
    }
}
