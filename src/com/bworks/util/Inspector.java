package com.bworks.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bworks.log.LoggerProvider;

/**
 * Inspect a class or object with reflection
 * @author Nikhil N K
 */
public class Inspector {

	/**
	 * Get all fields in class
	 * @param type
	 * @param out
	 * @return List of fields
	 */
	@SuppressWarnings("unchecked")
	public static <T, S> List<S> getFields(Class<T> type, Class<S> out) throws Exception {

		List<S> fields = null;
		List<Field> fieldsList = null;

		try {
			if(type != null && out != null) {
				fieldsList = Arrays.asList(type.getDeclaredFields());
				if(out == String.class) {
					fields = new ArrayList<S>();
					for(Field field : fieldsList) {
						fields.add((S) field.getName());
					}
				} else if(out == Field.class) {
					fields = new ArrayList<S>();
					fields = (List<S>) fieldsList;
				}
			}
		} catch(Exception e) {
			throw e;
		}

		return fields;
	}

	/**
	 * Get all fields and values in an object
	 * @param object
	 * @return field and value pair
	 */
	public static Map<String, Object> getFields(Object object, Class<?>... inclusives) 
			throws Exception {
		
		Field[] fields = null;
		Map<String, Object> properties = null;
		List<Class<?>> inclusivesList = null;

		try {
			inclusivesList = Arrays.asList(inclusives);
			fields = object.getClass().getDeclaredFields();
			properties = new LinkedHashMap<String, Object>();
			for(Field field : fields) {
				field.setAccessible(true);
				try {
					if(inclusivesList.size() == 0 || (inclusivesList.size() > 0 &&
							inclusivesList.contains(field.getType()))) {
						properties.put(field.getName(), field.get(object));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					LoggerProvider.getLogger().error(e);
				}
			}
		} catch(Exception e) {
			throw e;
		}

		return properties;
	}

	/**
	 * 
	 * @param object
	 * @param memberName
	 * @return value of a poperty
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValue(Object object, String memberName) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Object value = null;
		Field field = null;

		try {
			if(object != null && memberName != null) {
				field = object.getClass().getDeclaredField(memberName);
				field.setAccessible(true);
				value = field.get(object);
			}
		} catch(Exception e) {
			throw e;
		}

		return value;
	}

	/**
	 * 
	 * @param object
	 * @param memberName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValue(Object object, String memberName, Object value) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Field field = null;

		try {
			if(object != null && memberName != null && value != null) {
				field = object.getClass().getDeclaredField(memberName);
				field.setAccessible(true);
				Class<?> targetType = value.getClass();
				Class<?> sourceType = field.getType();
				if(sourceType.isPrimitive()) {
					if(sourceType.isAssignableFrom(int.class)) {
						sourceType = Integer.class;
					} else if(sourceType.isAssignableFrom(double.class)) {
						sourceType = Double.class;
					} else if(sourceType.isAssignableFrom(boolean.class)) {
						sourceType = Boolean.class;
					}
				}
				if(targetType.isAssignableFrom(sourceType)) {
					field.set(object, value);
				}
			}
		} catch(Exception e) {
			throw e;
		}
	}


	/**
	 * 
	 * @param object
	 * @param memberName
	 * @param value
	 * @return 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Class<?> getType(Class<?> classType, String memberName) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Field field = null;
		Class<?> type = null;

		try {
			if(classType != null && memberName != null) {
				field = classType.getDeclaredField(memberName);
				field.setAccessible(true);
				type = field.getType();
			}
		} catch(Exception e) {
			throw e;
		}
		
		return type;
	}
	
	/**
	 * 
	 * @param object
	 * @param memberName
	 * @return
	 * @throws Exception
	 */
	public static Annotation[] getAnnottations(Class<?> classType, String memberName) throws Exception {
		
		Field field = null;
		Annotation[] annotations = null;

		try {
			if(classType != null && memberName != null) {
				field = classType.getDeclaredField(memberName);
				field.setAccessible(true);
				annotations = field.getDeclaredAnnotations();
			}
		} catch(Exception e) {
			throw e;
		}

		return annotations;
		
	}
	

	/**
	 * 
	 * @param <T>
	 * @param object
	 * @param memberName
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public static <T extends Annotation> T getAnnottation(Class<?> classType, String memberName,
			Class<T> annotatedClass) throws Exception {

		Field field = null;
		T annotation = null;

		try {
			if(classType != null && memberName != null) {
				field = classType.getDeclaredField(memberName);
				field.setAccessible(true);
				annotation = field.getAnnotation(annotatedClass);
			}
		} catch(Exception e) {
			throw e;
		}

		return annotation;
	}
}