package com.wbd.member.service.impl;

import java.util.Date;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wbd.common.pojo.UserEntity;
import com.wbd.common.utils.BaseRedisService;
import com.wbd.common.utils.MD5Util;
import com.wbd.common.utils.TokenUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.member.dao.MemberDao;
import com.wbd.member.mq.RegisterMailboxProducer;
import com.wbd.member.service.MemberService;

@RestController
public class MemberServiceImpl implements MemberService {

	@Autowired
	private BaseRedisService baseRedisService;

	@Autowired
	private RegisterMailboxProducer registerMailboxProducer;

	@Autowired
	private MemberDao memberDao;

	public WBDResult testRest() {
		return WBDResult.build(200, "success");
	}

	public WBDResult setRedis(@RequestParam("key") String key, @RequestParam("value") String value){
		baseRedisService.setString(key, value);
		return WBDResult.ok();
	}

	public WBDResult getRedis(@RequestParam("value")String key) {
		String value = baseRedisService.getString(key);
		return WBDResult.ok(value);
	}

	public WBDResult findByUserId(@RequestParam("userId")Long userId) {
		UserEntity userEntity = memberDao.findByID(userId);
		if (userEntity == null) {
			return WBDResult.build(500, "没有找到对应的信息");
		}
		return WBDResult.ok(userEntity);
	}

	public WBDResult registerUser(@RequestBody UserEntity user) {
		String passWord = user.getPassword();
		String newPassWord = MD5Util.MD5(passWord);
		user.setPassword(newPassWord);
		user.setCreated(new Date());
		user.setUpdated(new Date());
		Integer insertUser = memberDao.insertUser(user);
		if (insertUser <= 0) {
			return WBDResult.build(500, "插入用户数据失败");
		}
		// 注册之后，向mq平台发送一条消息
		// 1.利用注册的email组装mq协议规范
		String json = this.emailJson(user.getEmail());
		// 2.调用mq发送邮件
		this.sendMsg(json);
		return WBDResult.ok();
	}

	/**
	 * 
	 * mq协议规范 { "header": { "interfaceType": "接口类型" }, "content": {} }
	 */

	/**
	 * 发送消息体的组装过程，
	 * <p>
	 * Title: emailJson
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param email
	 * @return
	 */
	private String emailJson(String email) {
		JSONObject root = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("interfaceType", "email");

		JSONObject content = new JSONObject();
		content.put("email", email);

		root.put("header", header);
		root.put("content", content);

		return root.toJSONString();
	}

	// 发送消息
	private void sendMsg(String json) {
		// 创建队列
		ActiveMQQueue activeMQQueue = new ActiveMQQueue("messages_queue");

		registerMailboxProducer.sendMsg(activeMQQueue, json);
	}

	public WBDResult login(@RequestParam("username") String username, @RequestParam("password") String password) {

		// 1.非空判断
		if (StringUtils.isEmpty(username)) {

			return WBDResult.build(500, "用户名为空");
		}

		if (StringUtils.isEmpty(password)) {

			return WBDResult.build(500, "密码");
		}

		String newPwd = MD5Util.MD5(password);

		// 2.查询
		UserEntity userEntity = memberDao.login(username, newPwd);

		//3.调用自动登录
		return setLogin(userEntity);
	}

	
	/**
	 * 自动登录，生成token，写入redis，返回token
	 * <p>Title: setLogin</p>  
	 * <p>Description: </p>  
	 * @param userEntity
	 * @return
	 */
	private WBDResult setLogin(UserEntity userEntity) {
		if (userEntity == null) {
			return WBDResult.build(500, "用户名或者密码有误");
		}

		// 3.生成token，也就是之前开发的sessionId
		String token = TokenUtils.getMemberToken();

		// 4.把token存入，redis， key=token value=userid

		String value = userEntity.getId() + "";

		baseRedisService.setString(token, value);

		// 5.返回结果中必须包含token信息

		JSONObject json = new JSONObject();

		json.put("token", token);

		return WBDResult.ok(json);
	}

	public WBDResult findByToken(@RequestParam("token") String token) {

		// 1.验证
		if (StringUtils.isEmpty(token)) {

			return WBDResult.build(500, "token不存在");
		}

		// 2.通过redis获取UserId

		String userId = baseRedisService.getString(token);

		// 3.验证
		if (StringUtils.isEmpty(userId)) {

			return WBDResult.build(500, "token失效");
		}

		UserEntity userEntity = memberDao.findByID(Long.parseLong(userId));

		if (userEntity == null) {
			return WBDResult.build(500, "未查询到用户信息");
		}

		userEntity.setPassword("");
		return WBDResult.ok(userEntity);
	}

	public WBDResult findByOpenId(String openid) {
		//验证
		if (StringUtils.isEmpty(openid)) {
			return WBDResult.build(500, "openid不能为null");
		}
		//使用openid查询数据库信息
		UserEntity userEntity = memberDao.findByOpenIdUser(openid);
		
		if(userEntity==null) {
			
			return WBDResult.build(500, "该用户为关联");
		}
		//如果关联，自动登录
		
		return this.setLogin(userEntity);
	}

	public WBDResult qqLogin(UserEntity user) {
		
		//1.判断openid
		String openid = user.getOpenid();
		if(StringUtils.isEmpty(openid)) {
			return WBDResult.build(500, "openid不能为null");
		}
		//2.进行账户登录
		WBDResult wBDResult = this.login(user.getUsername(), user.getPassword());
		if(wBDResult.getStatus()!=200) {
			return wBDResult;
		}
		//3.自动登录， 更新openid
		JSONObject obj = (JSONObject) wBDResult.getData();
	    String token = 	obj.getString("token");
	   
	    //4.根据token找到id
	    WBDResult wbdResult2 = this.findByToken(token);
	    if(wbdResult2.getStatus()!=200) {
			return wbdResult2;
		}
	     //5. 更新openid
	    UserEntity ue= (UserEntity) wbdResult2.getData();
	    Integer updateResult = memberDao.updateByOpenIdUser(openid, ue.getId());
	    if(updateResult<=0) {
	    	WBDResult.build(500, "关联账户失败");
	    }
		return wBDResult;
	}

}
