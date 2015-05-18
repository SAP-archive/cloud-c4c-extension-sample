package com.sap.hana.cloud.samples.cfcfbint.persistence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class holding information on a person.
 */
@Entity
@Table(name = "T_MARKETINGLEAD")
@NamedQuery(name = "AllLeads", query = "select p from MarketingLead p")
public class MarketingLead {
    @Id
    private Long leadid;   

	@Basic
    private String firstName;
    @Basic
    private String lastName;

    /**
     * Create a Person instance with unique id.
     */
   /* public MarketingLead() {
        this.leadid = UUID.randomUUID();
    }*/

    public Long getLeadid() {
		return leadid;
	}

	public void setLeadid(Long leadid) {
		this.leadid = leadid;
	}

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    @Override
    public int hashCode() {
        return leadid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MarketingLead)) {
            return false;
        }
        return getLeadid().equals(((MarketingLead) obj).getLeadid());
    }
}