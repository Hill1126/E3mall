package cn.gdou.e3mall.cart.intercetor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.gdou.e3mall.pojo.TbUser;
import cn.gdou.e3mall.sso.UserService;

public class LoginIntercetor implements HandlerInterceptor {

	@Autowired
	private UserService userService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	/*
	 * 方法执行前执行该方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//取得用户的浏览器中的token
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		if(StringUtils.isBlank(token)){
			return true;
		}
		//根据tonken判断用户是否登陆
		E3Result e3Result = userService.getUserByToken(token);
		TbUser user =  (TbUser) e3Result.getData();
		//未登录，直接放行
		if(user==null){
			return true;
		}
		//已登陆，把user对象放入request中放行
		request.setAttribute("user", user);
		return true;
	}
	
	//方法执行后，返回视图前
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	//页面返回之后执行
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
