package com.geshanzsq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RecSharingSysApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RecSharingSysApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
