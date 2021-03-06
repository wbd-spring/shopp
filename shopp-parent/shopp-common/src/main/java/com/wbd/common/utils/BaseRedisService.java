package com.wbd.common.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 封装jedis的基本操作
* <p>Title: BaseRedisService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月20日
 */
@Component
public class BaseRedisService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public void setString(String key, Object data) {
		setString(key, data, null);
	}

	public void setString(String key, Object data, Long timeout) {
		if (data instanceof String) {
			String value = (String) data;
			stringRedisTemplate.opsForValue().set(key, value);
		}
		if (timeout != null) {
			stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	public String getString(String key) {
		return (String) stringRedisTemplate.opsForValue().get(key);
	}

	public void delKey(String key) {
		stringRedisTemplate.delete(key);
	}

}
