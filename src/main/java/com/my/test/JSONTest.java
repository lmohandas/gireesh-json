package com.my.test;

import org.json.JSONObject;

import com.my.json.JSONToPojoConverter;
import com.my.vo.Address;
import com.my.vo.Organization;

public class JSONTest {

public static void main(String[] args) throws Exception {
		
		int empSize = 3;
		int orgSize = 2;
		
		final Organization organization = OrganizationBuilder.loadOrganization(orgSize, empSize);
		testJSON(organization);
		
		testJSON(ObjectLoader.loadObject(Address.class));
	}
	
	private static void testJSON(Object object) throws Exception {
		
		final String jsonObject = new JSONObject(object).toString();
		System.out.println(jsonObject);
		
		final JSONToPojoConverter jsonToPojoConverter = new JSONToPojoConverter();
		final Object object1 = jsonToPojoConverter.convertToPojo(new JSONObject(jsonObject), object.getClass());
		
		final String jsonObjectNew = new JSONObject(object1).toString();
		System.out.println(jsonObjectNew);		
	}
}
