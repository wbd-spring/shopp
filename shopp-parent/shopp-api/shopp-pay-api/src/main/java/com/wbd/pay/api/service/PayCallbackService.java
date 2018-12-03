package com.wbd.pay.api.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wbd.common.utils.WBDResult;

/**
 * 支付宝的返回的 同步通知和异步通知，实现分布式事物的一致性
 * 
* <p>Title: PayCallbackService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月30日
 */
@RequestMapping("/callback")
public interface PayCallbackService {
	
	//同步回调
	@RequestMapping("/synCallbackService")
	public WBDResult synCallback(@RequestParam Map<String,String> params);
	
	
	//异步回调
	@RequestMapping("/asynCallbackService")
	public String asynCallback(@RequestParam Map<String,String> params);

}
