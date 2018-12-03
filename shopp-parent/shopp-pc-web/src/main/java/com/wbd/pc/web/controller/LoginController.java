package com.wbd.pc.web.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import com.wbd.common.pojo.UserEntity;
import com.wbd.common.utils.CookieUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.pc.web.feign.MemberServiceFeign;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录控制器
* <p>Title: LoginController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月22日
 */
@Controller
@Slf4j
public class LoginController {

	
	
	//依赖注入MemberServiceFeign 的接口，实现客户端的LB
	@Autowired
	private MemberServiceFeign  memberServiceFeign;
	
	/**
	 * 登录页面跳转
	 * <p>Title: login</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/login")
	public String login()
	{
		return "login";
	}
	
	/**
	 * 输入用户名和密码进行 登录
	 * 需要把token写入 cookie中
	 * <p>Title: doLogin</p>  
	 * <p>Description: </p>  
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String doLogin(UserEntity user, HttpServletRequest request, HttpServletResponse response)
	{
		WBDResult wbdResult = memberServiceFeign.login(user.getUsername(), user.getPassword());
		if(wbdResult.getStatus()!=200) {
			request.setAttribute("error", "账号或者密码错误!");
			return "login";
		}
		
	
		LinkedHashMap object = (LinkedHashMap) wbdResult.getData();
		String token = (String) object.get("token");

		if (StringUtils.isEmpty(token)) {
			request.setAttribute("error", "会话已经失效!");
			return "login";
		}
		
		//写入cookie中
		CookieUtils.setCookie(request, response, "token", token, true);
		return "redirect:/"; //重定向
	}
	
	// maven 安装jar到本地厂库
	  
	//C:\Users\jwh>mvn install:install-file -DgroupId=com.sdk4j -DartifactId=sdk4j -Dv
    //			ersion=1.0 -Dpackaging=jar -Dfile=D:\Download\jar\Sdk4j.jar
	
	/**
	 * 生成qq授权链接
	 * 利用QQ的sdk
	 * <p>Title: locaQQLogin</p>  
	 * <p>Description: </p>  
	 * @param request
	 * @return
	 */
	@RequestMapping("/locaQQLogin")
	public String locaQQLogin(HttpServletRequest request) {
		
		String authorizeURL ="";
		try {
			authorizeURL = new Oauth().getAuthorizeURL(request);
		} catch (QQConnectException e) {
			log.info("授权出现异常{}"+e.getMessage());
			
			
			
		}
		
		return "redirect:"+authorizeURL;
	}
	
	
	
	/**
	 * 授权成功，之后回调qqLoginCallback方法，获取授权码，通过授权码获取accesstoken，
	 * 再通过accesstoken或者openid
	 * 1.允许授权，返回uri，获取授权码code
	 * 2.使用授权码code获取accessToken
	 * 3.使用accessToken获取openid
	 * 4.调用会员微服务，根据openid查询是否有关联用户
	 *  4.2没有关联账户，跳转到关联账户界面，绑定openid与用户信息(把openid存入session中，关联的方法需要)
	 *  4.3 如若有，自动登录，把用户信息存入cookie中
	 * 
	 */
	
	@RequestMapping("/qqLoginCallback")
	public String qqLoginCallback(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
		
		//1.获取授权码
		try {
			AccessToken accessTokenByRequest = new Oauth().getAccessTokenByRequest(request);
		if(accessTokenByRequest==null) {
			request.setAttribute("error", "QQ授权失败");
			return "error";
		}
		//2.通过授权码获取accesToken
		String accessToken = accessTokenByRequest.getAccessToken();
		
		if(accessToken==null) {
			request.setAttribute("error", "accessToken为空");
			return "error";
		}
		
		//3.使用accessToken获取openid
		OpenID openID = new OpenID(accessToken);
		String openId = openID.getUserOpenID();
		
		//4.调用会员服务接口，查询是否关联账户
		WBDResult wbdResult = memberServiceFeign.findByOpenId(openId);
		
		if(wbdResult.getStatus()!=200) { //5.如果没有绑定，跳转到绑定界面进行绑定
			
			httpSession.setAttribute("qqOpenid", openId);
			return "qqrelation";
		}
		LinkedHashMap object = (LinkedHashMap) wbdResult.getData();
		String token = (String) object.get("token");
		//写入cookie中
		CookieUtils.setCookie(request, response, "token", token, true);
		
		} catch (QQConnectException e) {
			
			e.printStackTrace();
		}
		//6.如果关联账户，获取token，写入cookie

		return "redirect:/";
	}
	
	   /**
	    * 未绑定下，跳转到绑定页面，把openid更新到数据库
	    * <p>Title: qqRelation</p>  
	    * <p>Description: </p>  
	    * @param userEntity
	    * @param request
	    * @param response
	    * @param httpSession
	    * @return
	    */
		@RequestMapping(value = "/qqRelation", method = RequestMethod.POST)
		public String qqRelation(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
			
			//1.通过sesion获取openid
			String qqOpenid = (String) httpSession.getAttribute("qqOpenid");
			
			if (StringUtils.isEmpty(qqOpenid)) {
				request.setAttribute("error", "么有获取到openid");
				return "error";
			}
			
			userEntity.setOpenid(qqOpenid);
			WBDResult wbdResult = memberServiceFeign.qqLogin(userEntity);
			if(wbdResult.getStatus()!=200) {
				request.setAttribute("error", "账户或者密码错误");
				
				return "login";
			}
			
			LinkedHashMap object = (LinkedHashMap) wbdResult.getData();
			String token = (String) object.get("token");
			if(StringUtils.isEmpty(token)) {
             request.setAttribute("error", "会话已经失效");
				
				return "login";
			}
			//写入cookie中
			CookieUtils.setCookie(request, response, "token", token, true);
			return  "redirect:/";
			
		}
}
