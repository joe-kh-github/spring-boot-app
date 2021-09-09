package com.springboot.app.config;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingHandler {

	private final static Logger logger = LoggerFactory.getLogger(LoggingHandler.class);

	/** Starting from all requests defined under the controller package */
	@Pointcut("execution(public * com.springboot.app.controllers..*.*(..))")
	public void webLog() {
	}

	/**
	 * Woven in before the cut point
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// Start printing request log
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// Print request related parameters
		logger.info("======================= Start ======================");
		// Print request url
		logger.info("URL            : {}", request.getRequestURL().toString());
		// Print Http method
		logger.info("HTTP Method    : {}", request.getMethod());
		// Print the full path and execution method of calling controller
		logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName());
		// Print requested IP
		logger.info("IP             : {}", request.getRemoteAddr());
		// Print request input parameter
		try {
			Object requestParam = joinPoint.getArgs();
			logger.info("parameter            : {}", new ObjectMapper().writeValueAsString(requestParam));
		} catch (Exception e) {
			logger.info("Parameter printing failed, exception: {}", e.getMessage());
		}
	}

	/**
	 * Weaving after the cut point
	 * 
	 * @throws Throwable
	 */
	@After("webLog()")
	public void doAfter() throws Throwable {
		logger.info("=======================  End  ======================");
	}

	/**
	 * surround
	 * 
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();		
		Object result = proceedingJoinPoint.proceed();
		// Print out parameters
		logger.info("Return value : {}", new ObjectMapper().writeValueAsString(result));
		// Execution time consuming
		logger.info("time consuming   : {} ms", System.currentTimeMillis() - startTime);
		return result;
	}

}