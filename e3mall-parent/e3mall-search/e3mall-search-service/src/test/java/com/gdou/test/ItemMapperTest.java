package com.gdou.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.pojo.SearchItem;
import cn.gdou.e3mall.serach.mapper.ItemMapper;

public class ItemMapperTest {

	
	@Test
	public void ItemMapper(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		ItemMapper mapper = ac.getBean(ItemMapper.class);
		List<SearchItem> list = mapper.getItemList();
	}
}
