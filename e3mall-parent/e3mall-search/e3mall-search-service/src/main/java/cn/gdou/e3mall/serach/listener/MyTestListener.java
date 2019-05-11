package cn.gdou.e3mall.serach.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyTestListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textmessage = (TextMessage) message;
			System.out.println(textmessage.getText());
			
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
