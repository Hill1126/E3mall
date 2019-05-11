package cn.gdou.test.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMqWithSpringTest {

	@Test
	public void producerTest() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//获取jmsTemplate对象（即producer对象）
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//从spring获得topic对象
		Destination destination= (Destination) applicationContext.getBean("queueDestination");
		//操作jmsTemplate对象发送消息
		jmsTemplate.send( destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("maneger service mq");
				return textMessage;
			}
		});
		
		
	}
}
