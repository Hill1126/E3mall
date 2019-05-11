package cn.gdou.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.gdou.e3mall.pojo.TbItem;

public interface CartService {

	E3Result addCartToServer(long userId,long itemId,int num);
	
	E3Result mergeItemCart(long userId,long itemId,int num);
	
	List<TbItem> getItemCart(long userId);
	
	E3Result deleteCartItem(long userId,long itemId);
	
	E3Result updateCartItem(long userId,long itemId,int num);
	
	E3Result deleteUserItem(long userId);
}
