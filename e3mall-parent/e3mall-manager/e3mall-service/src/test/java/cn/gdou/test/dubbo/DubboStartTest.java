package cn.gdou.test.dubbo;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboStartTest {

	@Test
	public void start() throws IOException{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("商品service已经启动");
		System.in.read();
		System.out.println("商品service已经关闭");
	}
	
}
