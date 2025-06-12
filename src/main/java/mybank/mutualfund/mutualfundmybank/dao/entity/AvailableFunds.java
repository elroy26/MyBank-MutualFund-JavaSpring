package mybank.mutualfund.mutualfundmybank.dao.entity;

import java.util.Date;

public class AvailableFunds {
    private Integer schemeCode;
    private String schemeName;
    private Date startDate;
    private String schemeCategory;
    private String fundHouse;
    private Double aumOverseas;
    private Double aumDomestic;
    private String riskInvolved;
    private Integer minInvestAmt;
    private Double startNav;

    public AvailableFunds(Integer schemeCode, String schemeName, Date startDate, String schemeCategory, String fundHouse, Double aumOverseas, Double aumDomestic, String riskInvolved, Integer minInvestAmt, Double startNav) {
        this.schemeCode = schemeCode;
        this.schemeName = schemeName;
        this.startDate = startDate;
        this.schemeCategory = schemeCategory;
        this.fundHouse = fundHouse;
        this.aumOverseas = aumOverseas;
        this.aumDomestic = aumDomestic;
        this.riskInvolved = riskInvolved;
        this.minInvestAmt = minInvestAmt;
        this.startNav = startNav;
    }
    public AvailableFunds() {
    }
    @Override
    public String toString() {
        return "AvailableFunds{" +
                "schemeCode=" + schemeCode +
                ", schemeName='" + schemeName + '\'' +
                ", startDate=" + startDate +
                ", schemeCategory='" + schemeCategory + '\'' +
                ", fundHouse='" + fundHouse + '\'' +
                ", aumOverseas=" + aumOverseas +
                ", aumDomestic=" + aumDomestic +
                ", riskInvolved='" + riskInvolved + '\'' +
                ", minInvestAmt=" + minInvestAmt +
                ", startNav=" + startNav +
                '}';
    }

    public Integer getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(Integer schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSchemeCategory() {
        return schemeCategory;
    }

    public void setSchemeCategory(String schemeCategory) {
        this.schemeCategory = schemeCategory;
    }

    public String getFundHouse() {
        return fundHouse;
    }

    public void setFundHouse(String fundHouse) {
        this.fundHouse = fundHouse;
    }

    public Double getAumOverseas() {
        return aumOverseas;
    }

    public void setAumOverseas(Double aumOverseas) {
        this.aumOverseas = aumOverseas;
    }

    public Double getAumDomestic() {
        return aumDomestic;
    }

    public void setAumDomestic(Double aumDomestic) {
        this.aumDomestic = aumDomestic;
    }

    public String getRiskInvolved() {
        return riskInvolved;
    }

    public void setRiskInvolved(String riskInvolved) {
        this.riskInvolved = riskInvolved;
    }

    public Integer getMinInvestAmt() {
        return minInvestAmt;
    }

    public void setMinInvestAmt(Integer minInvestAmt) {
        this.minInvestAmt = minInvestAmt;
    }

    public Double getStartNav() {
        return startNav;
    }

    public void setStartNav(Double startNav) {
        this.startNav = startNav;
    }
}
