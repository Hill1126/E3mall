package cn.gdou.e3mall.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.gdou.e3mall.mapper.TbOrderItemMapper;
import cn.gdou.e3mall.mapper.TbOrderMapper;
import cn.gdou.e3mall.mapper.TbOrderShippingMapper;
import cn.gdou.e3mall.order.pojo.OrderInfo;
import cn.gdou.e3mall.pojo.TbOrderItem;
import cn.gdou.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired 
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	
	@Value("${ORDER_ID_KEY}")
	private String ORDER_ID_KEY;
	@Value("${ORDER_DEFAULT_VALUE}")
	private String ORDER_DEFAULT_VALUE;
	@Value("${ORDER_ITEM_ID_KEY}")
	private String ORDER_ITEM_ID_KEY;
	
	@Override
	public E3Result addOrder(OrderInfo info) {
		// 使用redis设置订单id
		
		if(!jedisClient.exists(ORDER_ID_KEY)){
			jedisClient.set(ORDER_ID_KEY, ORDER_DEFAULT_VALUE);
		}
		String orderId = jedisClient.incr(ORDER_ID_KEY).toString();
		info.setOrderId(orderId);
		//设置订单状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		info.setStatus(1);
		//设置订单创建时间及更新时间
		info.setCreateTime(new Date());
		info.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insert(info);
		
		
		List<TbOrderItem> items = info.getOrderItems();
		for (TbOrderItem tbOrderItem : items) {
			String orderItemId = jedisClient.incr(ORDER_ITEM_ID_KEY).toString();
			//设置订单id到订单商品详情
			tbOrderItem.setOrderId(orderId);
			//设置订单商品详情id
			tbOrderItem.setId(orderItemId);
			//插入订单商品详情表
			orderItemMapper.insert(tbOrderItem);
		}
		
		//设置地址信息中的订单id
		TbOrderShipping orderShipping = info.getOrderShipping();
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShipping.setOrderId(orderId);
		orderShippingMapper.insert(orderShipping);
		
		
		return E3Result.ok(orderId);
	}

}
