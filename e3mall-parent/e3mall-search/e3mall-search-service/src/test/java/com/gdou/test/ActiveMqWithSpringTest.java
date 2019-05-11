package com.gdou.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gdou.e3mall.serach.service.SearchItemService;
import cn.gdou.e3mall.serach.service.SearchItemServiceImpl;

public class ActiveMqWithSpringTest {

	@Test
	public void consumerTest() throws Exception{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
	}
	
/*	@Test
	public void testAddSolrItem(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		SearchItemService service = (SearchItemService) applicationContext.getBean(SearchItemServiceImpl.class);
		service.addSolrItem(155247912135850L);
		System.out.println("成功");
	}*/
}
