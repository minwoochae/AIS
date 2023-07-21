package com.AIS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PackageController {
	@GetMapping(value="/sitterpackage")
	public String sitterpackage(){
		return "/sitterpackagemain/sitterpackage";
	}
}
