package com.my.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.my.json.JSONToPojoConverter;
import com.my.vo.Address;
import com.my.vo.Department;
import com.my.vo.Employee;
import com.my.vo.Organization;
import com.my.vo.Person;

public class OrganizationBuilder {
	
	 Logger logger = Logger.getLogger(JSONToPojoConverter.class);
	 
	public static Organization loadOrganization(int orgSize, int empSize)
			throws Exception {
		final List <Organization> sisterOrganizations = new ArrayList <Organization>();		
		for(int i=0; i<orgSize; i++) {		
			final List <Employee> employees = new ArrayList<Employee>();		
			for(int e=0; e<empSize; e++) {			
				employees.add(loadEmployee());
			}			
			final Organization organization = ObjectLoader.loadObject(Organization.class);
			organization.setEmployees(employees);
			organization.setAddress(ObjectLoader.loadObject(Address.class));			
			sisterOrganizations.add(organization);
		}
		
		final Organization organization = ObjectLoader.loadObject(Organization.class);
		organization.setAddress(ObjectLoader.loadObject(Address.class));	
		List <Employee> employees = new ArrayList<Employee>();		
		for(int e=0; e<empSize; e++) {			
			employees.add(loadEmployee());
		}
		organization.setEmployees(employees);
		organization.setSisterOrganizations(sisterOrganizations);
		return organization;
	}


	private static Employee loadEmployee() throws Exception {
		final Person person = ObjectLoader.loadObject(Person.class);
		 person.setCommunicationAddress(ObjectLoader.loadObject(Address.class));
		 person.setPermanentAddress(ObjectLoader.loadObject(Address.class));		 
		 final Employee employee = ObjectLoader.loadObject(Employee.class);
		 employee.setPerson(person);
		 employee.setDepartment(ObjectLoader.loadObject(Department.class));
		return employee;
	}
	
	
	

}
