package com.AIS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PricingController {
	@GetMapping(value="pricing")
	public String pricing(){
		return "/pricingmain/pricing";
	}
}
