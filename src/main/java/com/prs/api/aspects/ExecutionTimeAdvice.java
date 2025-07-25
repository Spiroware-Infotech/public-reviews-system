package com.prs.api.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnExpression("${aspect.enabled:true}")
public class ExecutionTimeAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeAdvice.class);
	
	@Around("@annotation(com.prs.api.aspects.TrackExecutionTime)")
	public Object executionTime(ProceedingJoinPoint point) throws Throwable {
		//log.info("Class Name: " + point.getSignature().getDeclaringTypeName() + " <===> Method Name: " + point.getSignature().getName());
		long startTime = System.currentTimeMillis();
		Object object = point.proceed();
		long endtime = System.currentTimeMillis();
		logger.info("\n Class Name: " + point.getSignature().getDeclaringTypeName() + "\n Method Name: "
				+ point.getSignature().getName() + ".\n Time taken for Execution is : " + (endtime - startTime) + " ms");
		return object;
	}
}