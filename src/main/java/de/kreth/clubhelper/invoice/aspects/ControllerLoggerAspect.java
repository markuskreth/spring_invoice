package de.kreth.clubhelper.invoice.aspects;

import static de.kreth.clubhelper.invoice.aspects.AbstractLoggerAspect.LogLevel.DEBUG;
import static de.kreth.clubhelper.invoice.aspects.AbstractLoggerAspect.LogLevel.ERROR;
import static de.kreth.clubhelper.invoice.aspects.AbstractLoggerAspect.LogLevel.INFO;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ControllerLoggerAspect extends AbstractLoggerAspect {

    @Pointcut("execution (public * de.kreth.clubhelper.invoice.ui..*(..))")
    private void invocation() {
    }

    @Before("invocation()")
    public void logDao(JoinPoint joinPoint) throws Throwable {
	log(INFO, joinPoint);
    }

    @AfterThrowing(pointcut = "invocation()", throwing = "ex")
    public void logCall(JoinPoint joinPoint, Exception ex) throws Throwable {
	log(ERROR, joinPoint, ex);
    }

    @AfterReturning(pointcut = "invocation()", returning = "result")
    public void logCall(JoinPoint joinPoint, Object result) throws Throwable {
	log(DEBUG, joinPoint, result);
    }

}
