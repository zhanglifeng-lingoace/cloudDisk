package com.edu.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class Generator {

//    public static void main(String[] args) {
//        String packageName = "com.edu.curriculum";
//        boolean serviceNameStartWithI = false;//auth -> UserService, 设置成true: auth -> IUserService
//        String[] tableNames = {"course_template_property", "course_property_info"};
//        String dbUrl = "jdbc:mysql://dev-db1.cq6gbzb8eeqc.us-west-1.rds.amazonaws.com:3306/lingoace_edu_curriculum?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false";
//        String username = "lingoace-edu";
//        String password = "clmdxoVgR7RD";
//        String path = "/Users/lingoace/edu/Generator";
//        generateByTables(serviceNameStartWithI, packageName, "kylor", dbUrl, username, password, path, tableNames);
//        System.out.println("completed...");
//    }
    /**
     * @param serviceNameStartWithI
     * @param packageName           包名
     * @param author                作者
     * @param dbUrl                 数据库名
     * @param username              数据库名称
     * @param password              数据库密码
     * @param path                  生成文件路径
     * @param tableNames            表名
     */
    public static void generateByTables(boolean serviceNameStartWithI, String packageName, String author,
                                         String dbUrl, String username, String password, String path, String... tableNames) {
        GlobalConfig config = new GlobalConfig();

        com.baomidou.mybatisplus.generator.config.DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(username)
                .setPassword(password)
                .setDriverName("com.mysql.cj.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames) //修改替换成你需要的表名，多个表名传数组
                .setRestControllerStyle(true);

        config.setActiveRecord(false)
                .setAuthor(author)
                .setOutputDir(path)
                .setFileOverride(true)
                .setEnableCache(false)
                .setBaseResultMap(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }

        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("controller")
                                .setEntity("entity")
                                .setMapper("mapper")
                                .setService("service")
                                .setServiceImpl("service.impl")
                                .setXml("mappers")
                ).execute();
    }
}
