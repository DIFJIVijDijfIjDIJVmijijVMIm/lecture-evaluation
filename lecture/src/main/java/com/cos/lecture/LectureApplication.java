package com.cos.lecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class LectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(LectureApplication.class, args);
	}

}
