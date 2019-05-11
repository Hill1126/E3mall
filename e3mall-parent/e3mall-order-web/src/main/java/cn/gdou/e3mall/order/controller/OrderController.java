package cn.gdou.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.gdou.e3mall.cart.service.CartService;
import cn.gdou.e3mall.order.pojo.OrderInfo;
import cn.gdou.e3mall.order.service.OrderService;
import cn.gdou.e3mall.pojo.TbItem;
import cn.gdou.e3mall.pojo.TbUser;

/**
 * 订单业务controller
 * @author HILL
 *
 */

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/order/order-cart.html")
	public String toOrderPage(HttpServletRequest request, Model model){
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> list = cartService.getItemCart(user.getId());
		model.addAttribute("cartList", list);
		
		return "order-cart";
	}
	
	
	@RequestMapping("/order/create.html")
	public String addOrder(OrderInfo orderinfo,HttpServletRequest request ){
		
		TbUser user = (TbUser) request.getAttribute("user");
		orderinfo.setUserId(user.getId());
		E3Result result = orderService.addOrder(orderinfo);
		
		//添加订单成功，删除购物车数据
		cartService.deleteUserItem(user.getId());
		request.setAttribute("orderId", result.getData());		//回显订单号
		request.setAttribute("payment", orderinfo.getPayment());
		
		
		return "success";
	}
	
}
