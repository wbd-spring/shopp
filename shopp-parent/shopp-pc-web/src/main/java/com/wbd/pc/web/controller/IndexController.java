package com.wbd.pc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 主要控制器
* <p>Title: IndexController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月22日
 */
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}

}
