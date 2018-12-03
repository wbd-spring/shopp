package com.wbd.pc.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wbd.common.utils.WBDResult;
import com.wbd.pc.web.feign.PayCallbackServiceFeign;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理支付宝 同步通知和异步通知
* <p>Title: CallbackController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月30日
 */
@Slf4j
@RequestMapping("/alibaba/callBack")  
@Controller
public class CallbackController {

	
	
	//支付宝回调的处理
	@Autowired
	private PayCallbackServiceFeign  payCallbackServiceFeign;
	
	private static final String PAY_SUCCESS = "pay_success";
	
	// 同步通知
	@RequestMapping("/returnUrl")
	public void synCallBack(HttpServletRequest request,HttpServletResponse response) throws IOException {
		log.info("returnUrl............");
	response.setContentType("text/html;charset=utf-8");
	Map<String, String> params = new HashMap<String, String>();
	Map<String, String[]> requestParams = request.getParameterMap();
	for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
		}
		// 乱码解决，这段代码在出现乱码时使用
		valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		params.put(name, valueStr);
	}
	log.info("###支付宝同步回调CallBackController####synCallBack开始 params:{}", params);	
	WBDResult result = payCallbackServiceFeign.synCallback(params);
	
	log.info("###支付宝同步回调#########结束");	
	if(result.getStatus()!=200) { //返回出错
		return;
	}
	
	//对返回的结果进行 拼接，返回html元素
	LinkedHashMap data = (LinkedHashMap) result.getData();
	String outTradeNo = (String) data.get("outTradeNo");
	// 支付宝交易号
	String tradeNo = (String) data.get("tradeNo");
	// 付款金额
	String totalAmount = (String) data.get("totalAmount");

	log.info("###支付宝同步回调CallBackController####synCallBack結束 params:{}", params);
	PrintWriter writer = response.getWriter();
	// 封装成html 浏览器模拟去提交
	String htmlFrom="<form name='punchout_form' "
			+ "method='post' "
			+ "action='http://zgh.tunnel.qydev.com/alibaba/callBack/synSuccessPage'>"
			+ "<input type='hidden' name='outTradeNo' value='"+outTradeNo+"'>"
			+ "<input type='hidden' name='tradeNo' value='"+tradeNo+"'>"
					+ "<input type='hidden' name='totalAmount' value='"+totalAmount+"'>"
							+ "<input type='submit' value='立即支付' style='display:none'>"
							+ "</form>"
							+ "<script>document.forms[0].submit();</script>";
	writer.println(htmlFrom);

	writer.close();
	}
	
	// 以post表达隐藏参数
		@RequestMapping(value = "/synSuccessPage", method = RequestMethod.POST)
		public String synSuccessPage(HttpServletRequest request, String outTradeNo, String tradeNo, String totalAmount) {
			request.setAttribute("outTradeNo", outTradeNo);
			request.setAttribute("tradeNo", tradeNo);
			request.setAttribute("totalAmount", totalAmount);
			return PAY_SUCCESS;
		}
		
	/**
	 * 以post方法接受异步通知，修改订单状态	
	 * <p>Title: asynCallBack</p>  
	 * <p>Description: </p>  
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/notifyUrl")
	public String asynCallBack(HttpServletRequest request,HttpServletResponse response) throws IOException {
			
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
//			// 乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		log.info("###支付宝异步回调CallBackController####synCallBack开始 params:{}", params);
		String result = payCallbackServiceFeign.asynCallback(params);
		
		return result;
		}
}
