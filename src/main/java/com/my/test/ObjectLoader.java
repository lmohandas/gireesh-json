package com.my.test;

import java.lang.reflect.Method;

import org.apache.commons.lang3.RandomStringUtils;

public class ObjectLoader {

	public static <T> T loadObject(final Class<T> clazz) 
			throws Exception {
		final T pojo = clazz.newInstance();
		final Method m[] = clazz.getMethods();       
        for (int i = 0; i < m.length; i++)  {    
        	final Method method = m[i];        	
        	if (method.getName().startsWith("set")){
        		String value = RandomStringUtils.randomAlphanumeric(20);
        		try { method.invoke(pojo, value); } catch(Exception e) {}        		
        	}        
        }       
        return pojo;
    }
}
