package com.wbd.message.adapter;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建适配接口
 * 
 * 统一发送的消息平台，采用适配器设计模式
* <p>Title: MessageAdapter.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月20日
 */
public interface MessageAdapter {
	
	public void sendMsg(JSONObject body);

}
