package com.wbd.message.mq;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.wbd.message.adapter.MessageAdapter;
import com.wbd.message.service.email.EmailServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息管理接口，利用mq消息监听器， 接收到消息之后再发送邮件
 * <p>
 * Title: ConsumerDistribute.java
 * </p>
 * <p>
 * Description:
 * </p>
 * MQ接口协议定义
 * {
    "header": {
        "interfaceType": "接口类型"
    },
    "content": {}
}
 * 
 * @author 朱光和
 * @date 2018年11月20日
 */
@Component
@Slf4j
public class ConsumerDistribute {
	
	
	
	
	private MessageAdapter messageAdapter;
	
	@Autowired
	private EmailServiceImpl emailService;

	// 监听消息
	@JmsListener(destination = "messages_queue")
	public void distribute(String json) {

		log.info("消息平台接受消息{}", json);

		if (StringUtils.isEmpty(json)) {
			return;
		}

		/**
		 *  MQ接口协议定义
		 * {
		    "header": {
		        "interfaceType": "接口类型"
		    },
		    "content": {}
	    	}
	    	
		 */
		
		JSONObject root = new JSONObject().parseObject(json);
		JSONObject header = root.getJSONObject("header");
		String interfacetype = header.getString("interfaceType");
		if (StringUtils.isEmpty(interfacetype)) {
			return;
		}

		if (interfacetype.equals("email")) { //发邮件
			messageAdapter = emailService;
		}

		if (interfacetype.equals("msg")) { //发短信

		}
		
		if(messageAdapter==null) {
			return;
		}
		
		JSONObject jsonObject = root.getJSONObject("content");
		log.info("消息{}", jsonObject);
		messageAdapter.sendMsg(jsonObject);
	}

}
