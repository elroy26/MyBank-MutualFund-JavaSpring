package mybank.mutualfund.mutualfundmybank.dao.entity;

public class FundManager {
    private Integer managerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer fundCompanyID;
    private Integer fundAvailableId;

    public FundManager() {
    }

    public FundManager(Integer managerId, String firstName, String middleName, String lastName, Integer fundCompanyID, Integer fundAvailableId) {
        this.managerId = managerId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.fundCompanyID = fundCompanyID;
        this.fundAvailableId = fundAvailableId;
    }

    @Override
    public String toString() {
        return "FundManager{" +
                "managerId=" + managerId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fundCompanyID=" + fundCompanyID +
                ", fundAvailableId=" + fundAvailableId +
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public Integer getFundAvailableId() {
        return fundAvailableId;
    }

    public void setFundAvailableId(Integer fundAvailableId) {
        this.fundAvailableId = fundAvailableId;
    }
}
