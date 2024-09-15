package mybank.mutualfund.mutualfundmybank.dao.entity;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CustomerAccount extends CustomerLogin {
    private Integer accountId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private Date birthDate;
    private String aadhaarNumber;
    private Integer customerId;

    public CustomerAccount() {
    }

    public CustomerAccount(Integer customerId, String username, String password, String email, String phoneNumber, String customerStatus, Integer attempts,
                           Integer accountId, String firstName, String middleName, String lastName, String address, Date birthDate, String aadhaarNumber) {
        super(customerId, username, password, email, phoneNumber, customerStatus, attempts);
        this.accountId = accountId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
        this.aadhaarNumber = aadhaarNumber;
    }

    public CustomerAccount(Integer accountId, String firstName, String middleName, String lastName, String address, Date birthDate, String aadhaarNumber, Integer customerId) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
        this.aadhaarNumber = aadhaarNumber;
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerAccount{" +
                "accountId=" + accountId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                ", aadhaarNumber='" + aadhaarNumber + '\'' +
                ", customerId=" + customerId +
                '}';
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
