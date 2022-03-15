package com.enel.platform.mepodlatam.batch.filters.application.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.annotation.FilterColumnMap;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.exception.FilterColumnMappingException;
import com.enel.platform.mepodlatam.exception.ApplicationException;

public class FilterColumnMappingConfig {

	private FilterColumnMappingConfig() {

	}

	private static final String CUSTOM_FILTER_COLUMN_MAPPING_PACKAGE = "com.enel.platform.mepodlatam.batch.filters.application.mapping";
	private static final String GET_FILTER_COLUMN_MAPPING_NAME_METHOD = "map";

	private static Map<String, String> mapColumns = new HashMap<>();	
	public static final String TENANT_ALIAS = "tenant";
	public static final String INCREMENTAL = "incremental#";

	public static void removeAllColumnMapping() {
		mapColumns.clear();
	}
	
	public static void mappingAll(Map<String, String> map) {
		mapColumns.putAll(map);
	}
	
	public static String getColumNameByDBAndAlias(String dbName, String fieldName) {
		if (mapColumns.isEmpty()) {
			addMapping();
		}
		String columnName = mapColumns.get(dbName.concat(fieldName));
		if (columnName == null) {
			throw new FilterColumnMappingException(
					String.format("Filter field %s no mapped with any db column", fieldName));
		}

		return columnName;
	}

	private static void addMapping() {
		Set<Class<?>> mappingClasses = getMappingClasses(FilterColumnMap.class);
		Iterator<Class<?>> mappingClassesIterator = mappingClasses.iterator();
		while (mappingClassesIterator.hasNext()) {
			FilterColumnMapping mapping = createMappingInstance(mappingClassesIterator.next());
			Map<String, String> mapColumnsForTenant = buildMapping(mapping);
			if (!mapColumnsForTenant.isEmpty()) {
				mappingAll(mapColumnsForTenant);
			}
		}
	}

	private static Set<Class<?>> getMappingClasses(Class<? extends Annotation> annotation) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(CUSTOM_FILTER_COLUMN_MAPPING_PACKAGE)).setScanners(Scanners.TypesAnnotated));
		return reflections.getTypesAnnotatedWith(annotation);				
	}

	private static FilterColumnMapping createMappingInstance(Class<?> clazz) {
		Constructor<?> mappingConstructor = null;
		FilterColumnMapping mapping = null;
		try {
			mappingConstructor = clazz.getDeclaredConstructor();
			mapping = (FilterColumnMapping) mappingConstructor.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new ApplicationException(e);
		}
		return mapping;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> buildMapping(FilterColumnMapping mapping) {

		Method map = null;
		Object mappingColumns = null;
		try {
			map = FilterColumnMapping.class.getDeclaredMethod(GET_FILTER_COLUMN_MAPPING_NAME_METHOD);
			mappingColumns = map.invoke(mapping);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ApplicationException(e);
		}
		if (mappingColumns == null) {
			return new HashMap<>();
		} else {
			return (Map<String, String>) mappingColumns;
		}

	}
}
