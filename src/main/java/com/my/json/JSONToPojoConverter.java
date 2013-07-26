package com.my.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONToPojoConverter {

	 Logger logger = Logger.getLogger(JSONToPojoConverter.class);

	    public <P> P convertToPojo(final JSONObject jsonObject, final Class<P> pojoClass) throws JSONException {

	        final P pojo = getPojoFromClass(pojoClass);
	        if (pojo == null) {
	            return pojo;
	        }

	        final List<MethodPair> setterMethods = getSetterMethods(pojoClass);
	        for (final MethodPair setter : setterMethods) {
	            if (jsonObject.has(setter.getFieldName())) {
	                callSetterWithValue(pojo, setter.getMethod(), jsonObject, setter.getFieldName());
	            }
	        }
	        return pojo;
	    }

	    <T> boolean callSetterWithValue(final T pojo,
	                                    final Method method,
	                                    final JSONObject jsonObject,
	                                    final String fieldName) throws JSONException {

	        Object value = null;
	        boolean isCallSuccess = false;
	        final Class<?> expectedType = getExpectedParameterType(method);
	        if (expectedType != null && !jsonObject.has(fieldName)) {
	            return isCallSuccess;
	        }
	        if (expectedType.equals(List.class) || expectedType.isAssignableFrom(ArrayList.class)) {
	            value = getArrayListValue(method, jsonObject, fieldName);
	        } else if (expectedType.equals(Map.class) || expectedType.isAssignableFrom(HashMap.class)) {
	            value = getMapValue(method, jsonObject, fieldName);
	        } else if (expectedType.equals(String.class)) {
	            value = jsonObject.getString(fieldName);
	        } else if (expectedType.equals(Date.class)) {
	            value = new DateTime(jsonObject.getString(fieldName)).toDate();
	        } else if (expectedType.equals(Long.class) || expectedType.equals(Long.TYPE)) {
	            value = jsonObject.getLong(fieldName);
	        } else if (expectedType.equals(Integer.class) || expectedType.equals(Integer.TYPE)) {
	            value = jsonObject.getInt(fieldName);
	        } else if (expectedType.equals(Boolean.class) || expectedType.equals(Boolean.TYPE)) {
	            value = jsonObject.getBoolean(fieldName);
	        } else if (expectedType.equals(Float.class) || expectedType.equals(Float.TYPE)) {
	            final String stringFloat = jsonObject.getString(fieldName);
	            value = new Float(stringFloat);
	        }

	        if (value != null) {
	            setValueToPojo(method, pojo, value);
	            isCallSuccess = true;
	        }
	        return isCallSuccess;

	    }

	    private Map<String, Object> getMapValue(final Method method, final JSONObject jsonObject, final String fieldName) {

	        final ParameterizedType genericListType = (ParameterizedType) method.getGenericParameterTypes()[0];
	        final Type[] types = genericListType.getActualTypeArguments();
	        final Class<?> valueClass = (Class<?>) types[1];

	        final Map<String, Object> map = new HashMap<String, Object>();
	        JSONObject jsonMap;
	        try {
	            jsonMap = jsonObject.getJSONObject(fieldName);

	            final Iterator keys = jsonMap.keys();
	            while (keys.hasNext()) {
	                final String keyName = (String) keys.next();
	                map.put(keyName, convertToPojo(new JSONObject(jsonMap.getString(keyName)), valueClass));
	            }
	        } catch (final JSONException e) {
	            logger.debug("Json parsing exception", e);
	        }
	        return map;
	    }

	    <T> void setValueToPojo(final Method method, final T pojo, final Object value) {

	        try {
	            method.invoke(pojo, value);
	        } catch (final IllegalArgumentException e) {
	            logger.debug(e);
	        } catch (final IllegalAccessException e) {
	            logger.debug(e);
	        } catch (final InvocationTargetException e) {
	            logger.debug(e);
	        }

	    }

	    List<Object> getArrayListValue(final Method method, final JSONObject jsonObject, final String fieldName) {

	        final ParameterizedType genericListType = (ParameterizedType) method.getGenericParameterTypes()[0];
	        final Type type = genericListType.getActualTypeArguments()[0];
	        Class<?> listElementClass;
	        if (type instanceof ParameterizedType) {
	            listElementClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
	        } else {
	            listElementClass = (Class<?>) type;
	        }

	        final List<Object> list = new ArrayList();
	        JSONArray jsonArray = null;
	        try {
	            jsonArray = jsonObject.getJSONArray(fieldName);

	            for (int i = 0; i < jsonArray.length(); i++) {

	                if (jsonArray.get(i) instanceof JSONObject) {
	                    list.add(convertToPojo(jsonArray.getJSONObject(i), listElementClass));
	                } else {
	                    list.add(jsonArray.get(i));
	                }

	            }
	        } catch (final JSONException e) {
	            logger.debug("Json Excepton", e);
	        }

	        return list;
	    }

	    Class<?> getExpectedParameterType(final Method method) {

	        final Class<?>[] expectedTypeArray = method.getParameterTypes();
	        Class<?> expectedType = null;
	        if (expectedTypeArray != null && expectedTypeArray.length > 0) {
	            expectedType = expectedTypeArray[0];
	        }
	        return expectedType;

	    }

	    List<MethodPair> getSetterMethods(final Class<?> pojoClass) {

	        final List<MethodPair> methods = new ArrayList<MethodPair>();
	        final Method[] methodarray = pojoClass.getMethods();
	        for (final Method element : methodarray) {
	            if (element.getName().startsWith("set")) {
	                methods.add(new MethodPair(element, element.getName().substring(3).toLowerCase()));
	            }
	        }
	        return methods;
	    }

	    public <P> P getPojoFromClass(final Class<P> pojoClass) {

	        P pojo = null;
	        try {
	            pojo = pojoClass.newInstance();
	        } catch (final InstantiationException e) {
	            logger.debug("Instantiation of POJO from the give class failed", e);
	        } catch (final IllegalAccessException e) {
	            logger.debug("Instantiation of POJO from the give class failed", e);
	        }
	        return pojo;
	    }

	    public class MethodPair {
	        Method method;

	        String fieldName;

	        public Method getMethod() {
	            return method;
	        }

	        public void setMethod(final Method method) {
	            this.method = method;
	        }

	        public MethodPair(final Method method, final String fieldName) {
	            this.method = method;
	            this.fieldName = fieldName;
	        }

	        public String getFieldName() {
	            return fieldName;
	        }

	        public void setFieldName(final String fieldName) {
	            this.fieldName = fieldName;
	        }

	    }
}
