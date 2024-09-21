package mybank.mutualfund.mutualfundmybank.dao.entity;

public class FundAvailable {
    private Integer fundAvailableId;
    private String fundName;
    private Integer fundTypeId;
    private Float expenseRatio;
    private Float navValue;
    private Integer managerId;
    private Float exitLoad;
    private Double AUM;
    private Double minInvestAmt;
    private Integer fundCompanyID;

    public FundAvailable() {
    }

    public FundAvailable(Integer fundAvailableId, String fundName, Integer fundTypeId, Float expenseRatio, Float navValue, Integer managerId, Float exitLoad, Double AUM, Double minInvestAmt, Integer fundCompanyID) {
        this.fundAvailableId = fundAvailableId;
        this.fundName = fundName;
        this.fundTypeId = fundTypeId;
        this.expenseRatio = expenseRatio;
        this.navValue = navValue;
        this.managerId = managerId;
        this.exitLoad = exitLoad;
        this.AUM = AUM;
        this.minInvestAmt = minInvestAmt;
        this.fundCompanyID = fundCompanyID;
    }

    public Integer getFundAvailableId() {
        return fundAvailableId;
    }

    public void setFundAvailableId(Integer fundAvailableId) {
        this.fundAvailableId = fundAvailableId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public Integer getFundTypeId() {
        return fundTypeId;
    }

    public void setFundTypeId(Integer fundTypeId) {
        this.fundTypeId = fundTypeId;
    }

    public Float getExpenseRatio() {
        return expenseRatio;
    }

    public void setExpenseRatio(Float expenseRatio) {
        this.expenseRatio = expenseRatio;
    }

    public Float getNavValue() {
        return navValue;
    }

    public void setNavValue(Float navValue) {
        this.navValue = navValue;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Float getExitLoad() {
        return exitLoad;
    }

    public void setExitLoad(Float exitLoad) {
        this.exitLoad = exitLoad;
    }

    public Double getAUM() {
        return AUM;
    }

    public void setAUM(Double AUM) {
        this.AUM = AUM;
    }

    public Double getMinInvestAmt() {
        return minInvestAmt;
    }

    public void setMinInvestAmt(Double minInvestAmt) {
        this.minInvestAmt = minInvestAmt;
    }

    public Integer getFundCompanyID() {
        return fundCompanyID;
    }

    public void setFundCompanyID(Integer fundCompanyID) {
        this.fundCompanyID = fundCompanyID;
    }

    @Override
    public String toString() {
        return "FundAvailable{" +
                "fundAvailableId=" + fundAvailableId +
                ", fundName='" + fundName + '\'' +
                ", fundTypeId=" + fundTypeId +
                ", expenseRatio=" + expenseRatio +
                ", navValue=" + navValue +
                ", managerId=" + managerId +
                ", exitLoad=" + exitLoad +
                ", assetUnderManagement=" + AUM +
                ", minInvestAmt=" + minInvestAmt +
                ", fundCompanyID=" + fundCompanyID +
                '}';
    }
}
