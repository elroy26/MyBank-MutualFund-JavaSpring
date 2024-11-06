package mybank.mutualfund.mutualfundmybank.webservice.soap;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.xml.bind.JAXBElement;
import mybank.mutualfund.mutualfundmybank.dao.entity.FundAvailable;
import mybank.mutualfund.mutualfundmybank.dao.remotes.FundRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import services.fund.CallSearchFundsRequest;
import services.fund.CallSearchFundsResponse;
import services.fund.ServiceStatus;


import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Endpoint
public class FundSearchEndpoint {
    //http://localhost:8082/fundRepo/fundSearch.wsdl
    // wsimport -keep http://localhost:8082/fundRepo/fundSearch.wsdl
    private static final String NAMESPACE_URI = "http://fund.services";

    @Autowired
    private FundRepository fundRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "callSearchFundsRequest")
    @ResponsePayload
    public JAXBElement<CallSearchFundsResponse> searchFunds(@RequestPayload Source source) {
        CallSearchFundsResponse response = new CallSearchFundsResponse();
        ServiceStatus status = new ServiceStatus();
        List<services.fund.FundAvailable> actualFund = new ArrayList<>();

        try {
            // Convert Source to XML string
            String xmlRequest = sourceToString(source);

            // Parse XML request with JDOM
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xmlRequest));
            Element rootElement = document.getRootElement();

            // Extract searchTerm from the XML
            String searchTerm = rootElement.getChildText("searchTerm", rootElement.getNamespace());
            System.out.println("Parsed searchTerm: " + searchTerm);

            // Proceed with business logic using the searchTerm
            List<FundAvailable> fundsDao = fundRepository.callSearchFunds(searchTerm);

            for (FundAvailable fund : fundsDao) {
                services.fund.FundAvailable fundAvailable = new services.fund.FundAvailable();
                BeanUtils.copyProperties(fund, fundAvailable);
                actualFund.add(fundAvailable);
            }

            response.getFund().addAll(actualFund);
            status.setStatus(200);
            status.setMessage("Funds retrieved successfully");

        } catch (Exception e) {
            status.setStatus(500);
            status.setMessage("Error retrieving funds: " + e.getMessage());
            e.printStackTrace();
        }

        response.setServiceStatus(status);
        return new JAXBElement<>(new QName(NAMESPACE_URI, "callSearchFundsResponse"), CallSearchFundsResponse.class, response);
    }

    // Helper method to convert Source to XML String
    private String sourceToString(Source source) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
