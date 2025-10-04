package com.itheima;

import com.example.EnableHeaderConfig;
import com.example.HeaderConfig;
import com.example.MyImportSelector;
import com.example.TokenParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@EnableHeaderConfig // 使用第三方依赖提供的Enable开头的注解
//@Import(MyImportSelector.class) // 导入ImportSelector接口实现类
//@Import({HeaderConfig.class})
//@Import({TokenParser.class}) // 导入的类会被spring加载到IOC容器中
@SpringBootApplication
//@ComponentScan({"com.itheima", "com.example"}) // 指定要扫描的包
public class SpringbootWebConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebConfigApplication.class, args);
    }

}
