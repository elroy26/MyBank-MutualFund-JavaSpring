package mybank.mutualfund.mutualfundmybank.dao.entity;

public class FundCompany extends FundManager {
    private Integer fundCompanyId;
    private String fundCompanyName;
    private Integer managerId;

    public FundCompany() {
    }

    public FundCompany(Integer fundCompanyId, String fundCompanyName, Integer managerId) {
        this.fundCompanyId = fundCompanyId;
        this.fundCompanyName = fundCompanyName;
        this.managerId = managerId;
    }

    public FundCompany(Integer managerId, String firstName, String lastName, Integer fundCompanyID, Integer fundCompanyId, String fundCompanyName, Integer managerId1) {
        super(managerId, firstName, lastName, fundCompanyID);
        this.fundCompanyId = fundCompanyId;
        this.fundCompanyName = fundCompanyName;
        this.managerId = managerId1;
    }

    public Integer getFundCompanyId() {
        return fundCompanyId;
    }

    public void setFundCompanyId(Integer fundCompanyId) {
        this.fundCompanyId = fundCompanyId;
    }

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    @Override
    public Integer getManagerId() {
        return managerId;
    }

    @Override
    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }
}
