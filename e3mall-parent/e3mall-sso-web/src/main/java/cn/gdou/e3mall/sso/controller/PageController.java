package cn.gdou.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面controller
 * @author HILL
 *
 */

@Controller
public class PageController {

	@RequestMapping("/page/login")
	public String showLogin(String returnUrl,Model model){
		model.addAttribute("returnUrl", returnUrl);
		return "login";
	}
	
	@RequestMapping("/page/register")
	public String toRegister(){
		return "register";
	}
}
