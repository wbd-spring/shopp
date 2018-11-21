package com.wbd.message.service.email;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wbd.message.adapter.MessageAdapter;

import lombok.extern.slf4j.Slf4j;
/**
 * 具体发邮件的实现， 可以调用第三方
* <p>Title: EmailServiceImpl.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月20日
 */
@Service
@Slf4j
public class EmailServiceImpl implements MessageAdapter {
	
	@Value("${msg.subject}")
	private String subject;
	
	@Value("${msg.text}")
	private String text;
	
	@Autowired
	private JavaMailSender mailSender;

	public void sendMsg(JSONObject body) {
		//处理发邮件
		String email = body.getString("email");
		
		if(StringUtils.isEmpty(email)) {
			return;
		}
      //调用发邮件
		
		log.info("真正发邮件{}"+email);
		
		SimpleMailMessage  smm = new SimpleMailMessage();
		//设置发送方与接收方
		smm.setFrom(email);
		smm.setTo(email);
		
		//设置主题与内容
		smm.setSubject(subject);
		smm.setText(text.replace("{}", email));
		
		mailSender.send(smm);
	}

}
