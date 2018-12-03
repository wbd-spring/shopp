package com.wbd.common.utils;

import java.util.UUID;

public class TokenUtils {

	 public static String getMemberToken(){
		 return "TOKEN_MEMBER-"+UUID.randomUUID();
	 }
	
	 
	 public static String getPayToken() {
			return "TOKEN_PAY" + "-" + UUID.randomUUID();
		}
}
