package com.wbd.pc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wbd.common.pojo.UserEntity;
import com.wbd.common.utils.WBDResult;
import com.wbd.pc.web.feign.MemberServiceFeign;

/**
 * 注册相关controller
* <p>Title: RegisterController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月22日
 */
@Controller
public class RegisterController {
	
	//依赖注入MemberServiceFeign 的接口，实现客户端的LB
	@Autowired
	private MemberServiceFeign  memberServiceFeign;
	
	/**
	 * 直接跳转到注册页面
	 * <p>Title: forword</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/register")
	public String forwordRegister()
	{
		return "register";
	}
	
	
	/**
	 * 注册具体业务
	 * <p>Title: register</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String register(UserEntity user,Model model)
	{
		WBDResult result = memberServiceFeign.registerUser(user);
		
		if(result.getStatus()!=200) {
			model.addAttribute("error", "注册失败");
			return "register";
		}
		return "login";
	}

}
