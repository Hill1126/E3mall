package cn.gdou.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.gdou.e3mall.cart.service.CartService;
import cn.gdou.e3mall.pojo.TbItem;
import cn.gdou.e3mall.pojo.TbUser;
import cn.gdou.e3mall.service.ProductService;

/**
 * 购物车管理controller
 * @author HILL
 *
 */

@Controller
public class CartController {

	@Value("${CART_ITEM_COOKIE}")
	private String CART_ITEM_COOKIE;
	@Value("${COOKIE_MAXAGE}")
	private int COOKIE_MAXAGE;
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	
	/**
	 * @see 对用户添加购物车的操作进行响应
	 * @param itemId	
	 * @param num
	 * @return 
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addtoCart(@PathVariable Long itemId,int num,
			HttpServletRequest request, HttpServletResponse response){
		//获取用户cookie中的购物车列表
		List<TbItem> list = getItemByCookie(request);
		
		//判断用户是否登陆
		TbUser user =  (TbUser) request.getAttribute("user");
		//若登陆则把购物车放入redis中
		if( user != null ){
			//合并cookie中的购物车商品
			if(list.size()>0){
				for (TbItem tbItem : list) {
					cartService.mergeItemCart(user.getId(), tbItem.getId(), num);
				}
				//把合并后的cookie删除
				CookieUtils.deleteCookie(request, response, CART_ITEM_COOKIE);
			}
			
			E3Result result = cartService.addCartToServer(user.getId(), itemId, num);
			if(result.getStatus()==200){
				return "cartSuccess";
			}
		}

		//没有登陆则执行下列操作
		//查找列表中是否存在需要添加的商品
		for (TbItem tbItem : list) {
			if(tbItem.getId() == itemId.longValue()){
				//若存在则直接添加数量
				tbItem.setNum(tbItem.getNum()+num);
				CookieUtils.setCookie(request, response, CART_ITEM_COOKIE,
						JsonUtils.objectToJson(list), COOKIE_MAXAGE);
				return "cartSuccess";
			}
		}
		//若不存在则查找数据库，把商品信息添加到列表中
		TbItem tbItem = productService.getTbitemById(itemId);
		tbItem.setNum(num);
		list.add(tbItem);
		//把购物车列表写回cookie中
		CookieUtils.setCookie(request, response, CART_ITEM_COOKIE,
				JsonUtils.objectToJson(list), COOKIE_MAXAGE);
		return  "cartSuccess";
		
	}
	
	@RequestMapping("/cart/cart.html")
	public String toItemCart(HttpServletRequest request){
		List<TbItem> list = null;
		TbUser user = (TbUser) request.getAttribute("user");
		//用户已登陆，从服务器上取得购物车数据
		if(user!= null){
			list = cartService.getItemCart(user.getId());
			request.setAttribute("cartList", list);
			return "cart";
		}
		
		
		 list = getItemByCookie(request);
		request.setAttribute("cartList", list);
		return "cart";
		
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateItemCartWithUnlogin(@PathVariable Long itemId,@PathVariable int num,
			HttpServletRequest request){
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			E3Result result = cartService.updateCartItem(user.getId(), itemId, num);
			return result;
		}
		
		
		List<TbItem> list  = getItemByCookie(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId() == itemId.longValue()){
				tbItem.setNum(num);
				break;
			}
		}
		return E3Result.ok();
		
	}
	@RequestMapping("/cart/delete/{itemId}.html")
	public String deleteItem(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response	){
		

		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		
		List<TbItem> list = getItemByCookie(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId() == itemId.longValue()){
				list.remove(tbItem);
				break;
			}
		}
		String itemList = JsonUtils.objectToJson(list);
		CookieUtils.setCookie(request, response, CART_ITEM_COOKIE, itemList, COOKIE_MAXAGE);
		
		return "redirect:/cart/cart.html";
		
	}
	
	
	
	
	private List<TbItem> getItemByCookie(HttpServletRequest request){
		String value = CookieUtils.getCookieValue(request, CART_ITEM_COOKIE);
		if(StringUtils.isBlank(value)){
			return new ArrayList<TbItem>();
		}
		
		return JsonUtils.jsonToList(value, TbItem.class);
		
	}
	
}
