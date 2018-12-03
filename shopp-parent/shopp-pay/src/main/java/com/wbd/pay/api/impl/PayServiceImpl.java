package com.wbd.pay.api.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.config.AlipayConfig;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wbd.common.utils.BaseRedisService;
import com.wbd.common.utils.TokenUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.pay.api.pojo.PaymentInfo;
import com.wbd.pay.api.service.PayService;
import com.wbd.pay.dao.PaymentInfoDao;

@RestController
public class PayServiceImpl implements PayService {
	
	@Autowired
	private  PaymentInfoDao  paymentInfoDao;
	
	
	@Autowired
	private  BaseRedisService  baseRedisService;

	//创建支付令牌
	public WBDResult createToken(@RequestBody PaymentInfo paymentInfo) {
		
		//1.创建支付请求信息,
		Integer result = paymentInfoDao.savePaymentType(paymentInfo);
		if(result<=0) {
			return WBDResult.build(500, "创建支付令牌失败");
		}
		
		//2.生成token
		String payToken = TokenUtils.getPayToken();
		
		Long timeout = (long) (60*15);
		
		//3.存入redis中，key=payToken,value=paymentInfo.getId();
		baseRedisService.setString(payToken, paymentInfo.getId()+"",timeout);
		
		//5.返回token
		
		return WBDResult.ok(payToken);
	}

	/**
	 *根据支付令牌查询支付信息
	 *
	 */

	public WBDResult findPayToken(@RequestParam("payToken") String payToken) {
	
		//1.验证
		if(StringUtils.isEmpty(payToken)) {
			return WBDResult.build(404, "payToken为空");
		}
		
		//2.判断token有效期，去redis中查询
		String payId = baseRedisService.getString(payToken);
		if(StringUtils.isEmpty(payId)) {
			return WBDResult.build(500, "支付请求已经超时，请重新下单");
		}
		
		long id = Long.parseLong(payId);
		
		//3.利用redis中存储的id查询对应的信息
		
		PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(id);
		
		if(paymentInfo==null) {
			
			return WBDResult.build(500, "未找到支付信息");
		}
		
		//4.对接支付宝，支付代码，把从数据库中查询到的paymentInfo对象以form的形式提交到支付宝
		
		DefaultAlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, 
				AlipayConfig.merchant_private_key,"json",AlipayConfig.charset,AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
		
		
		
		
		//5.设置请求参数，支付宝返回(同时有两个通知)同步通知和异步通知，他们的的区别：同步通过浏览器重定向给用户，告诉用户支付结果，以get方法返回
		// 不能修改订单的状态，异步通过接口来接受以post方式返回， 一般第三方平台收到该接口信息，需要修改自己平台的相关数据
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
			//5.1设置同步的通知的url
		request.setReturnUrl(AlipayConfig.return_url);
		    //5.2设置异步的通知的url
		request.setNotifyUrl(AlipayConfig.notify_url);
		
		//5.3 商户订单号，必须填写
		String out_trade_no = paymentInfo.getOrderId();
		
		//5.4 付款金额，必须填写
		String total_amount= paymentInfo.getPrice()+"";
		
		//5.5 订单名称
		String subject = "gumei pay";
		
		request.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
				+ "\"," + "\"subject\":\"" + subject + "\","
				// + "\"body\":\""+ body +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		
		//6.发送请求
		String result = "";
		try {
			//最终返回一个form， 组装为一个form，以post方法提交给支付宝
			result = alipayClient.pageExecute(request).getBody();
		} catch (AlipayApiException e) {
			return WBDResult.build(500, "支付异常");
		}
		return WBDResult.ok(result);
	}

	

}
