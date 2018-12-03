package com.wbd.pay.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.config.AlipayConfig;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wbd.common.utils.WBDResult;
import com.wbd.pay.api.pojo.PaymentInfo;
import com.wbd.pay.api.service.PayCallbackService;
import com.wbd.pay.dao.PaymentInfoDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PayCallbackServiceImpl implements PayCallbackService {

	@Autowired
	private PaymentInfoDao paymentInfoDao;

	/**
	 * 同步回调
	 */
	public WBDResult synCallback(@RequestParam Map<String, String> params) {
		log.info("同步回调的参数*******:" + params);

		// 1.获取 支付宝GET返回的信息
		JSONObject data = new JSONObject();
		try {
			// 调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
					AlipayConfig.charset, AlipayConfig.sign_type);
			if (!signVerified) {

				return WBDResult.build(500, "验签失败");
			}

			// 商户订单号
			String outTradeNo = params.get("out_trade_no");
			// 支付宝交易号
			String tradeNo = params.get("trade_no");
			// 付款金额
			String totalAmount = params.get("total_amount");

			data.put("outTradeNo", outTradeNo);
			data.put("tradeNo", tradeNo);
			data.put("totalAmount", totalAmount);
		} catch (AlipayApiException e) {
			log.info("支付宝同步回调出现错误*******:" + e);
			return WBDResult.build(500, "系统错误");

		}

		return WBDResult.ok(data);
	}

	/**
	 * 异步回调
	 */
	public String asynCallback(@RequestParam Map<String, String> params) {

		log.info("异步回调的参数*******:" + params);
		//1.验证签名
		boolean signVerified;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
					AlipayConfig.sign_type);
			if (!signVerified) {
				return "fail";

			}
		} catch (AlipayApiException e) {
			log.info("异步回调阿里api出现异常*******:" + e);
			return "fail";
		}
		//2.获取商品订单号
		String orderId = params.get("out_trade_no");
		
		//3.根据订单号查询商品信息
		PaymentInfo paymentInfo = paymentInfoDao.getByOrderIdPayInfo(orderId);
		if(paymentInfo==null) {
			
			return "fail";
		}
		//4.如果状态为1，表示已经成功，已经下单过，直接返回成功
		Integer state = paymentInfo.getState();
		if(state==1) {
			return "success";
		}

		//5.获取支付宝交易号
		String tradeNo =params.get("trade_no");
		
		//6.获取交易状态
		String trade_status =params.get("trade_status");
		
		//7.如果异步通知成功，就修改paymentInfo表的 对应信息， 比如支付状态为1，交易流水号
		if (trade_status.equals("TRADE_SUCCESS")) {
			paymentInfo.setPayMessage(params.toString());
			paymentInfo.setPlatformorderId(tradeNo);
			paymentInfo.setState(1);
			paymentInfoDao.updatePayInfo(paymentInfo);
		}
		
		log.info("异步回调结束######################");
		return "success";
		
	}

}
