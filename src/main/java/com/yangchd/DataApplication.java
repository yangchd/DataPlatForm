package com.yangchd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * @author yangchd 2017/12/11
 */

@SpringBootApplication
public class DataApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

}

//@SpringBootApplication
//public class DataApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(DataApplication.class);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(DataApplication.class, args);
//    }
//
//}
