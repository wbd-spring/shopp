package com.wbd.member.mq;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * mq
 * 消息发送者
* <p>Title: RegisterMailboxProducer.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月20日
 */
@Service
public class RegisterMailboxProducer {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	public void sendMsg(Destination destination, String json) {
		jmsMessagingTemplate.convertAndSend(destination, json);
	}
	
}
