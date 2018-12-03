package com.wbd.pc.web.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.wbd.pay.api.service.PayCallbackService;
/**
 * 处理支付宝同步与异步通知接口
* <p>Title: PayCallbackServiceFeign.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月30日
 */
@FeignClient("pay")
@Component
public interface PayCallbackServiceFeign extends PayCallbackService{

}
