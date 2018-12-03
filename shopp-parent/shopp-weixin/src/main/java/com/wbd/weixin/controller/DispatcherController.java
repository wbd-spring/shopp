package com.wbd.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wbd.common.base.TextMessage;
import com.wbd.common.utils.CheckUtil;
import com.wbd.common.utils.HttpClientUtil;
import com.wbd.common.utils.XmlUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class DispatcherController {
	
	private static final String REQESTURL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";
	/**
	 * 服务器验证接口地址
	 * <p>Title: dispatcherGte</p>  
	 * <p>Description: </p>  
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 */
	@RequestMapping(value="/dispatcher",method=RequestMethod.GET)
	public String dispatcherGte(String signature, String timestamp, String nonce, String echostr) {
		boolean flag = CheckUtil.checkSignature(signature, timestamp, nonce);
		if(!flag) {
			return null;
		}
		return echostr;
	}
	
	/**
	 * 微信消息推送,测试
	 * <p>Title: dispatcherGte</p>  
	 * <p>Description: </p>  
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 * @throws IOException 
//	 */
//	@RequestMapping(value="/dispatcher",method=RequestMethod.POST)
//	public void dispatcherPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
//		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		
//		//1.将微信信息xml转换为xml
//		String msg = "";
//		//返回微信信息
//		PrintWriter pw = response.getWriter();
//		try {
//			Map<String, String> parseXml = XmlUtils.parseXml(request);
//			log.info("###收到微信消息####resultMap:" + parseXml.toString());
//	    //2.判断消息类型
//		String msgType =	parseXml.get("MsgType");
//			
//	  
//		switch (msgType) {
//		case "text": //如果为普通文本
//			//
//			String toUserName =	parseXml.get("ToUserName");//开发者微信号
//			String fromUserName =	parseXml.get("FromUserName");//发送方帐号
//			String content =	parseXml.get("Content");//消息内容
//			
//			if(content.equals("万贝贷")) {
//				
//				msg=	setText("http://www.wanbeidai.com", toUserName, fromUserName);
//			}else {
//				msg=	setText("没有找到你想要的信息", toUserName, fromUserName);
//			}
//			
//			pw.println(msg);
//			break;
//
//		default:
//			break;
//		}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		pw.close();
//		
//	}
	
	
	private String setText(String content,String fromUserName,String toUserName) {
		
		TextMessage message = new TextMessage(); 
		message.setContent(content);
		message.setFromUserName(fromUserName);
		message.setToUserName(toUserName);
		message.setMsgType("text");
		return XmlUtils.messageToXml(message);
	}
	
	
	// 微信动作推送，调用第三方机器人接口
	@RequestMapping(value = "/dispatcher", method = RequestMethod.POST)
	public void dispatCherPost(HttpServletRequest reqest, HttpServletResponse response) throws Exception {
		reqest.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 1.将xml转换成Map格式
		Map<String, String> resultMap = XmlUtils.parseXml(reqest);
		log.info("###收到微信消息####resultMap:" + resultMap.toString());
		// 2.判断消息类型
		String msgType = resultMap.get("MsgType");
		// 3.如果是文本类型，返回结果给微信服务端
		PrintWriter writer = response.getWriter();
		switch (msgType) {
		case "text":
			// 开发者微信公众号
			String toUserName = resultMap.get("ToUserName");
			// 消息来自公众号
			String fromUserName = resultMap.get("FromUserName");
			// 消息内容
			String content = resultMap.get("Content");
			String resultJson = HttpClientUtil.doGet(REQESTURL + content);
			JSONObject jsonObject = JSONObject.parseObject(resultJson);
			Integer resultCode = jsonObject.getInteger("result");
			String msg = null;
			if (resultCode == 0) {
				String resultContent = jsonObject.getString("content");
				msg = setText(resultContent, toUserName,fromUserName);
			}else {
				msg = setText("我现在有点忙.稍后回复您!", toUserName,fromUserName);
			}
			writer.println(msg);
			break;

		default:
			break;
		}
		writer.close();
	}

}
