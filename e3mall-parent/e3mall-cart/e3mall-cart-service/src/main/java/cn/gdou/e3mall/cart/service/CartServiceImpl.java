package cn.gdou.e3mall.cart.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.gdou.e3mall.mapper.TbItemMapper;
import cn.gdou.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;
	
	
	@Value("${REDIS_CART}")
	private String REDIS_CART;
	
	
	@Override
	public E3Result addCartToServer(long userId, long itemId, int num) {
		//根据userId查询redis
		Boolean hexists = jedisClient.hexists(REDIS_CART+":"+userId, itemId+"");
		//商品已存在，则修改商品数量
		if(hexists){
			String json = jedisClient.hget(REDIS_CART+":"+userId, itemId+"");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			jedisClient.hset(REDIS_CART+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		//商品不存在，则根据商品id从数据库中查询商品写入redis中
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		jedisClient.hset(REDIS_CART+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		
		return E3Result.ok();
	}


	@Override
	public E3Result mergeItemCart(long userId, long itemId, int num) {
		return addCartToServer(userId,itemId,num);
	}


	@Override
	public List<TbItem> getItemCart(long userId) {
		List<String> fieldList = jedisClient.hvals(REDIS_CART+":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String field : fieldList) {
			if(!StringUtils.isBlank(field)){
				TbItem item = JsonUtils.jsonToPojo(field, TbItem.class);
				itemList.add(item);
			}
		}
		return itemList;
	}


	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		List<String> fieldValueList = jedisClient.hvals(REDIS_CART+":"+userId);
		for (String value : fieldValueList) {
			TbItem tbItem = JsonUtils.jsonToPojo(value, TbItem.class);
			if(tbItem.getId()==itemId){
				jedisClient.hdel(REDIS_CART+":"+userId, itemId+"");
				break;
			}
		}
		
		return E3Result.ok();
	}


	@Override
	public E3Result updateCartItem(long userId, long itemId, int num) {
		//根据userId查询redis
		Boolean hexists = jedisClient.hexists(REDIS_CART+":"+userId, itemId+"");
		//商品已存在，则修改商品数量
		if(hexists){
			String json = jedisClient.hget(REDIS_CART+":"+userId, itemId+"");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(num);
			jedisClient.hset(REDIS_CART+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		return E3Result.build(400, "更新失败");
	}


	@Override
	public E3Result deleteUserItem(long userId) {
		jedisClient.del(REDIS_CART+":"+userId);
		return E3Result.ok();
	}
	
	

}
