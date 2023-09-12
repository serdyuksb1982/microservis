package ru.serdyuk.micro.planner.utils.aop;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Log
public class LoggingAspect {
    //аспект будет выполняться для всех методов из пакета контроллеров
    @Around("execution(* ru.serdyuk.micro.planner.todo.contorller..*(..)))")
    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        //получить информацию о том, какой класс и метод выполняется
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("-------- Executing " + className + "." + methodName + " ---------- ");

        StopWatch stopWatch = new StopWatch();
        // засечка времени
        // выполняем сам метод
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();
        log.info("----------Executing time of -> <<" + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms.>>");
        return result;
    }
}
