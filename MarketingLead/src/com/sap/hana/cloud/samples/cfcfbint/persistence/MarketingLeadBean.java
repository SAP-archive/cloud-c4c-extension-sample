package com.sap.hana.cloud.samples.cfcfbint.persistence;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean encapsulating all operations for a marketingLead.
 */
@Stateless
@LocalBean
public class MarketingLeadBean {
    @PersistenceContext
    private EntityManager em;

    /**
     * Get all marketingLeads from the table.
     */
    public List<MarketingLead> getAllMarketingLeads() {
        return em.createNamedQuery("AllLeads", MarketingLead.class).getResultList();
    }

    /**
     * Add a marketingLead to the table.
     */
    public void addMarketingLead(MarketingLead marketingLead) {
        em.persist(marketingLead);
        em.flush();
    }
}
