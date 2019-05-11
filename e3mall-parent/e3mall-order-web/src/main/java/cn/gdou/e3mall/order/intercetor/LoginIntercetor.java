package cn.gdou.e3mall.order.intercetor;

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
	
	@Value("${USER_TOKEN}")
	private String USER_TOKEN;
	@Value("${LOGIN_DIR}")
	private String LOGIN_DIR;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取用户浏览器中的token
		String token = CookieUtils.getCookieValue(request, USER_TOKEN, true);
		//判断token是否有值
		if(StringUtils.isBlank(token)){
			response.sendRedirect(LOGIN_DIR+"/page/login?returnUrl="+request.getRequestURL());
			return false;
		}
		//若有值，调用userService查询redis中该用户信息是否存在
		E3Result userByToken = userService.getUserByToken(token);
		TbUser user =  (TbUser) userByToken.getData();
		//若不存在，封装当前的url，跳转到登录页面
		if(user==null){
			response.sendRedirect(LOGIN_DIR+"/page/login?returnUrl="+request.getRequestURL());
			return false;
		}
		//若用户存在，把信息放入request中，放行
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
