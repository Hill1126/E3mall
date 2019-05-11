package cn.gdou.e3mall.search.handlerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器，负责处理异常，记录错误日志，给用户回调友好的界面
 * @author HILL
 *
 */
public class GlobalHandlerException implements HandlerExceptionResolver {

	//用的是slf4j的logger
	Logger logger = LoggerFactory.getLogger(this.getClass());
			
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {
		//记录异常
		logger.error("系统发生异常", ex);
		//通知相关人员
		//发短信、邮箱
		//给用户一个友好的错误界面
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/error/exception");
		return mav;
	}

}
