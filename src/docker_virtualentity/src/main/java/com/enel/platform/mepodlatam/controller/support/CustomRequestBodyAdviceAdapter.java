package com.enel.platform.mepodlatam.controller.support;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.enel.platform.mepodlatam.util.JsonUtils;

@ControllerAdvice
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomRequestBodyAdviceAdapter.class);

	@Override
	public boolean supports(MethodParameter methodParameter, Type type,
			Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		String bodyString = JsonUtils.objToString(body);
		LOGGER.info("Incoming payload: {} ", bodyString);
		return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
	}
}