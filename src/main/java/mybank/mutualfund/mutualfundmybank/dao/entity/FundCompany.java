package mybank.mutualfund.mutualfundmybank.dao.entity;

public class FundCompany {
    private Integer fundTypeId;
    private String fundTypeName;

    public FundCompany(Integer fundTypeId, String fundTypeName) {
        this.fundTypeId = fundTypeId;
        this.fundTypeName = fundTypeName;
    }

    public FundCompany() {
    }

    public Integer getFundTypeId() {
        return fundTypeId;
    }

    public void setFundTypeId(Integer fundTypeId) {
        this.fundTypeId = fundTypeId;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    @Override
    public String toString() {
        return "FundCompany{" +
                "fundTypeId=" + fundTypeId +
                ", fundTypeName='" + fundTypeName + '\'' +
                '}';
    }
}
