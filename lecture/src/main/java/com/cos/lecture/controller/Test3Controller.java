package com.cos.lecture.controller;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test3Controller {

		@GetMapping("/root/log")
		public String UserLogin() {
			
			
			
			return "test/root";
		}
	
}
