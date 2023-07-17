package com.AIS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VetController {

		@GetMapping(value = "/vet")
		public String about() {
			return "/veterinarian/vet";
		}
		

}
