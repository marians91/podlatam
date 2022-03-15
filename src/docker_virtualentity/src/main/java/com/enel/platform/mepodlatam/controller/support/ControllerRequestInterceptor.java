package com.enel.platform.mepodlatam.controller.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.enel.platform.mepodlatam.util.PodDateUtils;

public class ControllerRequestInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerRequestInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ContentCachingRequestWrapper requestCacheWrapper = new ContentCachingRequestWrapper(request);
		Long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);

		String startLog = String.format(
				"Start Time: %s \n".concat("Invoked resource: %s \n").concat("Method: %s \n").concat(
						"Query String: %s"),
				PodDateUtils.getCurrentTime(), requestCacheWrapper.getRequestURI(),
				requestCacheWrapper.getMethod(), requestCacheWrapper.getQueryString());
		LOGGER.info(startLog);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
			ModelAndView modelAndView) throws Exception {
		LOGGER.info("End Time: {} ", PodDateUtils.getCurrentTime());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o,
			Exception e) throws Exception {
		Long endTime = System.currentTimeMillis();
		Long startTime = (Long) request.getAttribute("startTime");
		LOGGER.info("Total time to process request: {}{}", (endTime - startTime), "ms");

	}

}
