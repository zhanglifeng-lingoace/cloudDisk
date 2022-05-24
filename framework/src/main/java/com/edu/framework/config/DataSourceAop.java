package com.edu.framework.config;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class DataSourceAop {

    @Autowired
    DynamicDataSourceProperties dynamicDataSourceProperties;

    @Pointcut("execution(* com..*Mapper.*(..))")
    public void checkArgs() {
    }


    @Before("checkArgs()")
    public void process(JoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class cla = joinPoint.getTarget().getClass();
        if (cla.isAnnotationPresent(DS.class)) {
            //使用注解切换数据库
            return;
        }

        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        Method method = cla.getMethod(methodName, parameterTypes);
        if (method.isAnnotationPresent(DS.class)) {
            //使用注解切换数据库
            return;
        }

        if (methodName.startsWith("get")
                || methodName.startsWith("count")
                || methodName.startsWith("find")
                || methodName.startsWith("list")
                || methodName.startsWith("select")
                || methodName.startsWith("check")
                || methodName.startsWith("page")) {

            Map<String, DataSourceProperty> map = dynamicDataSourceProperties.getDatasource();
            String sourKey = dynamicDataSourceProperties.getPrimary();
            for (String key : map.keySet()) {
                if (!key.equals(dynamicDataSourceProperties.getPrimary())){
                    sourKey = key;
                    break;
                }
            }

            DynamicDataSourceContextHolder.push(sourKey);
        } else {
            DynamicDataSourceContextHolder.push(dynamicDataSourceProperties.getPrimary());

        }
    }

    @Before("checkArgs()")
    public void afterAdvice() {
        DynamicDataSourceContextHolder.clear();
    }

}
