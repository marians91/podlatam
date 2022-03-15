package com.enel.platform.mepodlatam.batch.filters.application;

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

import com.enel.platform.mepodlatam.batch.filters.application.annotation.WhereConditionFilter;
import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;
import com.enel.platform.mepodlatam.exception.ApplicationException;

public class FilterFactory {

	private FilterFactory() {
	}

	private static final String CUSTOM_FILTER_PACKAGE = "com.enel.platform.mepodlatam.batch.filters.application";
	private static final String GET_FILTER_NAME_METHOD = "getFilterName";

	private static final Map<String, Filter> registeredFilters = new HashMap<>();

	public static void registerFilter(String name, Filter filter) {
		registeredFilters.put(name, filter);
	}
	
	public static void removeAllFilters() {
		registeredFilters.clear();
	}


	public static Filter getFilter(String name) {
		if (registeredFilters.isEmpty()) {
			registerFilter(WhereConditionFilter.class);
		}
		Filter filter = registeredFilters.get(name);
		if (filter == null) {
			throw new ApplicationFilterException("Filter is not allowed");
		}
		return filter;
	}

	private static Set<Class<?>> getFilterClasses(Class<? extends Annotation> annotation) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(CUSTOM_FILTER_PACKAGE)).setScanners(Scanners.TypesAnnotated));
		return reflections.getTypesAnnotatedWith(annotation);
	}

	private static void registerFilter(Class<? extends Annotation> annotation) {
		Set<Class<?>> filterClasses = getFilterClasses(annotation);
		Iterator<Class<?>> filterClassesIterator = filterClasses.iterator();
		while (filterClassesIterator.hasNext()) {
			Filter filter = createFilterInstance(filterClassesIterator.next());
			String filterName = resolveFilterName(filter);
			registerFilter(filterName, filter);
		}
	}

	private static Filter createFilterInstance(Class<?> clazz) {
		Constructor<?> filterConstructor = null;
		Filter filter = null;
		try {
			filterConstructor = clazz.getDeclaredConstructor();
			filter = (Filter) filterConstructor.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new ApplicationException(e);
		}
		return filter;
	}

	private static String resolveFilterName(Filter filter) {

		Method getFilterName = null;
		Object filterName = null;
		try {
			getFilterName = Filter.class.getDeclaredMethod(GET_FILTER_NAME_METHOD);
			filterName = getFilterName.invoke(filter);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ApplicationException(e);
		}
		if (filterName == null) {
			return null;
		} else {
			return String.valueOf(filterName);
		}

	}
}
