package com.wbd.pc.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wbd.common.utils.WBDResult;
import com.wbd.pc.web.feign.PayServiceFeign;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付控制器
* <p>Title: PayController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月30日
 */
@Slf4j
@Controller
public class PayController {
	
	//支付订单
	@Autowired
	private PayServiceFeign  payServiceFeign;
	


	/**
	 * 通过token创建订单， 然后返回form表单，通过post请求支付宝对应的网关
	 * <p>Title: aliPay</p>  
	 * <p>Description: </p>  
	 * @param payToken
	 * @param response
	 */
	@RequestMapping("/aliPay")
	public void aliPay(String payToken, HttpServletResponse response) {
		//设置返回的类型
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer =null;
		try {
		 writer = response.getWriter();
		} catch (IOException e) {
			log.info("aliPay控制器出现异常....."+e);
			e.printStackTrace();
		}
		//1.参数验证
		if(StringUtils.isEmpty(payToken)) {
			return;
		}
		
		//2.调用支付接口，获取支付宝html元素
		WBDResult wbdResult = payServiceFeign.findPayToken(payToken);
		
		//3.如果调用失败，返回失败信息
		if(wbdResult.getStatus()!=200) {
			String msg = wbdResult.getMsg();
			writer.println(msg);
			return;
		}
		
		log.info("aliPay返回的信息....."+wbdResult.getData());
		
		//4.返回可执行的元素给客户端
		writer.println(wbdResult.getData());
		writer.close();
	} 
}
