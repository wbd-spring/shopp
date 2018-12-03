package com.wbd.member.service;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wbd.common.pojo.UserEntity;
import com.wbd.common.utils.WBDResult;
/**
 * 1.mapper dao 接口中的参数 用 @Param声明
 * 2.微服务中的接口参数采用 @RequestParam声明， 实现类也必须声明@RequestParam
 * 接口中有参数必须有@RequestParam 参数，方便提供给Feign接口使用
* <p>Title: MemberService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月22日
 */
@RequestMapping("/member")
public interface MemberService {
	
	@RequestMapping("/testRest")
	public WBDResult testRest() ;
		
	@RequestMapping("/setRedis")
	public WBDResult setRedis(@RequestParam("key") String key, @RequestParam("value") String value) ;
	
	@RequestMapping("/getRedis")
	public WBDResult getRedis(@RequestParam("value")String key);
	
	@RequestMapping("/findByUserId")
	public WBDResult findByUserId(@RequestParam("userId")Long userId);
	
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
	public WBDResult login(@RequestParam("username") String username, @RequestParam("password") String password);
	
	
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
	public WBDResult findByToken(@RequestParam("token") String token);
	
	/**
	 * 根据openid查询用户信息
	 * <p>Title: findByOpenId</p>  
	 * <p>Description: </p>  
	 * @param openid
	 * @return
	 */
	@RequestMapping("/findByOpenId")
	public WBDResult findByOpenId(@RequestParam("openid") String openid);

	
    // 用户登录
	@RequestMapping("/qqLogin")
	WBDResult qqLogin(@RequestBody UserEntity user);
}
