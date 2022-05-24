package com.edu.cloudDisk.core.log;

import com.edu.cloudDisk.core.utils.LingoAceJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


public class LogPrint {

    private static Logger logger = LoggerFactory.getLogger(LogPrint.class);

    public static void error(String method, Exception ex) {
        logger.error(method, ex);
    }

    public static void error(HttpServletRequest httpServletRequest, Exception ex) {
        String requestInfo = constructRequestInfo(httpServletRequest);
        logger.error(requestInfo, ex);
    }

    public static final String constructRequestInfo(HttpServletRequest httpServletRequest) {
        return constructRequestInfo(httpServletRequest, null);
    }

    public static final String constructRequestInfo(HttpServletRequest httpServletRequest, Object postBody) {
        StringBuilder stringBuilder = new StringBuilder();
        if (httpServletRequest != null) {
            stringBuilder.append(httpServletRequest.getRequestURI());
            Enumeration pNames = httpServletRequest.getParameterNames();
            while (pNames.hasMoreElements()) {
                String name = (String) pNames.nextElement();
                String value = httpServletRequest.getParameter(name);
                stringBuilder.append(name);
                stringBuilder.append(" : ");
                stringBuilder.append(value + "\n");
            }
            if (HttpMethod.GET.name().equalsIgnoreCase(httpServletRequest.getMethod())) {

            } else {
                try {
                    if (postBody != null) {

                        stringBuilder.append(LingoAceJSON.Serialize(postBody) + "\n");
                    }
                } catch (Exception e) {
                    stringBuilder.append("postBody to json Exception " + postBody.toString() + "\n");
                }
            }
        }
        return stringBuilder.toString();
    }


    public static void info(String data) {
        logger.info("数据:" + data);
    }


    public static void slowRequest(String data) {
        logger.info(data);
    }


}
