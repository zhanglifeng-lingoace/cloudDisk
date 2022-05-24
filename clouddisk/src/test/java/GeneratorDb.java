

import com.edu.cloudDisk.CloudDiskApplication;
import com.edu.framework.config.Generator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudDiskApplication.class)
public class GeneratorDb {

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String dbUrl;

    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String username;

    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String password;


    @Test
    public void generate() {
        String packageName = "com.edu.cloudDisk";
        boolean serviceNameStartWithI = false;//auth -> UserService, 设置成true: auth -> IUserService
        //String pathMic = "/Users/shanshan/IdeaProjects/lms/Generator";
        String pathWindows = "D:\\generator";
        String[] tableNames = {"course_package_tree_relation"};
        Generator.generateByTables(serviceNameStartWithI, packageName, "zhanglf",
                dbUrl, username, password, pathWindows, tableNames);
    }

}
