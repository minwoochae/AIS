package com.AIS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DistributionController {

	@GetMapping(value="/distribution")
	public String distribution(){
		return "/distributionmain/distribution";
	}

}
