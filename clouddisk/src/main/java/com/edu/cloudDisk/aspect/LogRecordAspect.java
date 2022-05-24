package com.edu.cloudDisk.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.edu.common.response.CommonResult;
import com.edu.cloudDisk.core.log.LogPrint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Random;

@Aspect
@Component
@Order(1)
public class LogRecordAspect {
    private final static String POST = "POST";
    private final static String GET = "GET";
    private static final Logger httpSlowLog = LoggerFactory.getLogger("httpSlow");
    /**
     * 切点
     */
    @Pointcut("execution(* com.edu.cloudDisk.controller.*..*(..))")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        long start = System.currentTimeMillis();
        String requestId = start + getRandom();
        if (!request.getRequestURI().equals("/cloudDisk/alive")) {
        LogPrint.info("请求开始requestId:" + requestId + "===地址:" + request.getRequestURL().toString());
        String pathUri = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        String token = request.getHeader("token");
        String param = "";
        LogPrint.info("请求开始requestId:" + requestId + "===token:" + token);
        try {
            //获取请求参数集合并进行遍历拼接
            if (args.length > 0) {
                if (POST.equals(method)) {
                    for (Object arg : args) {
                        if (!(arg instanceof BeanPropertyBindingResult)) {
                            LogPrint.info("请求开始requestId:" + requestId + "===POST参数:" + JSON.toJSONString(arg, SerializerFeature.IgnoreNonFieldGetter));
                            param = JSON.toJSONString(arg, SerializerFeature.IgnoreNonFieldGetter);
                        }
                    }
                } else if (GET.equals(method)) {
                    //restful 风格无需打印参数
                    LogPrint.info("请求开始requestId:" + requestId + "===GET参数:" + queryString);
                    param = queryString;
                }
            }
        } catch (Exception e) {
            LogPrint.error("对象转换参数错误", e);
        }

        Object result = null;
        try {
            result = pjp.proceed();
            long end = System.currentTimeMillis();
            LogPrint.info("请求结束requestId:" + requestId + "==用时:" + (end - start) + "ms===返回值:" + JSON.toJSONString(result, SerializerFeature.IgnoreNonFieldGetter));
            if ((end - start) > 20000) {
                httpSlowLog.info("请求超时：" + requestId + "毫秒: " + (end - start) + "，地址：" + request.getRequestURL().toString() + "参数：" + param);
            }

        } catch (ConstraintViolationException e) {
            for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
                LogPrint.info("请求结束requestId:" + requestId + "返回值:" + JSON.toJSONString(CommonResult.failed(0, constraintViolation.getMessageTemplate()), SerializerFeature.IgnoreNonFieldGetter));
                throw e;
            }
            LogPrint.info("请求结束requestId:" + requestId + "返回值:" + JSON.toJSONString(CommonResult.failed(0, e.getMessage()), SerializerFeature.IgnoreNonFieldGetter));
            throw e;
        } catch (Exception e) {
            LogPrint.error("对象转换返回值错误", e);
            throw e;
        }
        return result;
    } else {
        Object result = pjp.proceed();
        return result;
    }
    }

    //获取随机数
    public String getRandom() {
        Random ran = new Random();
        int random = ran.nextInt(10000);
        return String.valueOf(random);
    }
}
