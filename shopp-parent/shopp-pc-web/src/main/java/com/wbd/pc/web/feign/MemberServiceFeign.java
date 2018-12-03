package com.wbd.pc.web.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.wbd.member.service.MemberService;
/**
 * 客户端Feign 继承api的接口，
* <p>Title: MemberServiceFeign.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月22日
 */
@Component
@FeignClient("member")
public interface MemberServiceFeign extends MemberService{

}
