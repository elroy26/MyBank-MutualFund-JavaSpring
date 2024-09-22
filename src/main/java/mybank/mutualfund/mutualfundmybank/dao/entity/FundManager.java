package mybank.mutualfund.mutualfundmybank.dao.entity;

public class FundManager extends FundType {
    private Integer managerId;
    private String firstName;
    private String lastName;
    private Integer fundCompanyID;

    public FundManager() {
    }

    @Override
    public String toString() {
        return "FundManager{" +
                "managerId=" + managerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fundCompanyID=" + fundCompanyID +
                '}';
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getFundCompanyID() {
        return fundCompanyID;
    }

    public void setFundCompanyID(Integer fundCompanyID) {
        this.fundCompanyID = fundCompanyID;
    }

    public FundManager(Integer managerId, String firstName, String lastName, Integer fundCompanyID) {
        this.managerId = managerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fundCompanyID = fundCompanyID;
    }
}