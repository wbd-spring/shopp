package com.wbd.pay.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wbd.common.utils.WBDResult;
import com.wbd.pay.api.pojo.PaymentInfo;

/**
 * 支付接口
 * 1.通过token来进行支付，因为订单是有有效期的
 * 所以一般把token存入redis中。
* <p>Title: PayService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月28日
 */
@RequestMapping("/pay")
public interface PayService {

	/**
	 * 根据支付信息创建令牌：
	 * 提交订单之前，把订单信息存入数据库，返回表中的id
	 * 
	 * <p>Title: createToken</p>  
	 * <p>Description: </p>  
	 * @param paymentInfo
	 * @return
	 */
	@RequestMapping(value="/createToken")
	public WBDResult createToken(@RequestBody PaymentInfo paymentInfo);
	
	/**
	 * 根据支付令牌查询支付信息,最后组装成form表单，通过post方法提交到支付宝
	 * 
	 * <p>Title: findPayToken</p>  
	 * <p>Description: </p>  
	 * @param payToken
	 * @return 组装好的form表单信息
	 */
	@RequestMapping("/findPayToken")
	public WBDResult findPayToken(@RequestParam("payToken") String payToken);
}
