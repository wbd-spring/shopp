package com.wbd.pc.web.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.wbd.pay.api.service.PayService;

/**
 * 支付feing接口
* <p>Title: PayServiceFeign.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月30日
 */
@FeignClient("pay")
@Component
public interface PayServiceFeign extends PayService{

}
