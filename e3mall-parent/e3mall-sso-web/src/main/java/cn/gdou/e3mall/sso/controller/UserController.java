package cn.gdou.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.gdou.e3mall.pojo.TbUser;
import cn.gdou.e3mall.sso.UserService;

/**
 * 用户业务controller
 * @author HILL
 *
 */

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	
	@RequestMapping("/user/login")
	@ResponseBody
	public E3Result login(String username, String password ,
			HttpServletRequest request, HttpServletResponse response){
		E3Result result = userService.login(username, password);
		String token = result.getData().toString();
		CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		
		return result;
	}
	
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result registUser(TbUser user){
		E3Result result = userService.registUser(user);
		return result;
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkParam(@PathVariable String param , @PathVariable int type){
		return userService.checkParam(param, type);
		
	}
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		//根据token查询用户，判断并返回
		E3Result result = userService.getUserByToken(token);
		//根据callback的值判断该请求是否为跨域请求
		String json = JsonUtils.objectToJson(result);
		if(StringUtils.isBlank(callback)){
			return json;
		}
		
		//跨域请求，拼接字符串
		return callback+"("+json+")";
		
		/*
		 * spring4.1以上可以使用
		 *	MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
		 *	mappingJacksonValue.setJsonpFunction(callback);
		 *	return mappingJacksonValue;
		 * 
		 */
		
		
	}
	
	
}
