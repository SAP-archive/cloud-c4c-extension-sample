package com.sap.hana.cloud.samples.cfcfbint.odata;
import java.util.Properties;  
import javax.persistence.EntityManagerFactory;  
import javax.persistence.Persistence;  
import org.odata4j.producer.ODataProducer;  
import org.odata4j.producer.ODataProducerFactory;  
import org.odata4j.producer.jpa.JPAProducer;  
public class LeadProducerFactory implements ODataProducerFactory {  
    private String persistenceUnitName = "MarketingLead";  
    private String namespace = "http://sap.com/MarketingLead";  
    private int maxResults = 50;  
    @Override  
    public ODataProducer create(final Properties arg0) {  
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(  
                persistenceUnitName);  
        JPAProducer producer = new JPAProducer(  
                emf,  
                namespace,  
                maxResults);  
        return producer;  
    }  
}  
