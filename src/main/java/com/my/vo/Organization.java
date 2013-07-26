package com.my.vo;

import java.util.ArrayList;
import java.util.List;

public class Organization {
    
    private String organizatioName;
    private Address address;
    private String phoneNumber;
    private String emailID;
    private String website;
    
    
    private List <Organization> sisterOrganizations = new ArrayList<Organization>();
    private List <Employee> employees = new ArrayList<Employee>();    

    public void setOrganizatioName(String organizatioName) {
        this.organizatioName = organizatioName;
    }

    public String getOrganizatioName() {
        return organizatioName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

	public List <Organization> getSisterOrganizations() {
		return sisterOrganizations;
	}

	public void setSisterOrganizations(List <Organization> sisterOrganizations) {
		this.sisterOrganizations = sisterOrganizations;
	}
}
