package com.edu.framework;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.edu.framework")
@PropertySource(value = {"classpath:mybatis.properties"}, encoding = "UTF-8", name = "mybatis.properties", ignoreResourceNotFound = true)
public class Initialize {
}
