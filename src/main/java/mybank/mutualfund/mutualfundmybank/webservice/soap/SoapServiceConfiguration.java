package mybank.mutualfund.mutualfundmybank.webservice.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.soap.server.endpoint.interceptor.SoapEnvelopeLoggingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


@EnableWs
@Configuration
public class SoapServiceConfiguration {
    @Bean
    public SoapEnvelopeLoggingInterceptor loggingInterceptor() {
        return new SoapEnvelopeLoggingInterceptor();
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> servletRegistrationBean(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/fundRepo/*");
    }

    @Bean(name = "fundSearch")
    public DefaultWsdl11Definition convertToWsdl(XsdSchema fundSchema) {
        DefaultWsdl11Definition wsdlDefinition = new DefaultWsdl11Definition();
        wsdlDefinition.setPortTypeName("FundSearchPort");
        wsdlDefinition.setLocationUri("/fundRepo");
        wsdlDefinition.setTargetNamespace("http://fund.services");
        wsdlDefinition.setSchema(fundSchema);
        return wsdlDefinition;
    }

    @Bean
    public XsdSchema fundSchema() {
        return new SimpleXsdSchema(new ClassPathResource("fund.xsd"));
    }

    // Add the JAXB marshaller configuration
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("services.fund"); // Make sure this matches the package where your generated classes are
        return marshaller;
    }

}
