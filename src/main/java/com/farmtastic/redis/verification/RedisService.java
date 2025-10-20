package com.farmtastic.redis.verification;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	private StringRedisTemplate redisTemplate;
	
	public RedisService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public void setVerificationCode(String code, String username, long timeoutMinutes) {
		redisTemplate.opsForValue().set(code, username, timeoutMinutes, TimeUnit.MINUTES);
	}
	
//	public String getMemAccByCode(String code) {
//		return redisTemplate.opsForValue().get(code);
//	}
	
	public String getMemEmailByCode(String code) {
		return redisTemplate.opsForValue().get(code);
	}
	
	public void deleteCode(String code) {
		redisTemplate.delete(code);
	}
	
}
