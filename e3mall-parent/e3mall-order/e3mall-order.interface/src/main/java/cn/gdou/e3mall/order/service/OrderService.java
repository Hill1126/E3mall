package cn.gdou.e3mall.order.service;

import cn.e3mall.common.pojo.E3Result;
import cn.gdou.e3mall.order.pojo.OrderInfo;

public interface OrderService {

	E3Result addOrder(OrderInfo info);
}
