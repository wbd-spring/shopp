package com.wbd.member.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wbd.common.pojo.UserEntity;
import com.wbd.common.utils.WBDResult;

@RequestMapping("/member")
public interface MemberService {
	
	@RequestMapping("/testRest")
	public WBDResult testRest() ;
		
	@RequestMapping("/setRedis")
	public WBDResult setRedis(String key, String value) ;
	
	@RequestMapping("/getRedis")
	public WBDResult getRedis(String key);
	
	@RequestMapping("/findByUserId")
	public WBDResult findByUserId(Long userId);
	
	@RequestMapping("/registerUser")
	public WBDResult registerUser(@RequestBody UserEntity user);
	
	/**
	 * 登录成功之后把token存入到redis中，然后再把token返回
	 * <p>Title: login</p>  
	 * <p>Description: </p>  
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/login")
	public WBDResult login(String username, String password);
	
	
	/**
	 * 根据token获取用户信息
	 * 1.先去redis中获取userid
	 * 2.然后再通过userid去数据库中查询用户信息， 返回用户信息
	 * <p>Title: findByToken</p>  
	 * <p>Description: </p>  
	 * @param token
	 * @return
	 */
	@RequestMapping("/findByToken")
	public WBDResult findByToken(String token);

}
