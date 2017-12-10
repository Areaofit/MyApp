package com.areaofit.app.listener.messageListener;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * 
 * @Description 消息监听器
 * 
 * SessionAwareMessageListener是Spring为我们提供的，它不是标准的JMS MessageListener。
 * MessageListener的设计只是纯粹用来接收消息的，
 * 假如我们在使用MessageListener处理接收到的消息时我们需要发送一个消息通知对方我们已经收到这个消息了，
 * 那么这个时候我们就需要在代码里面去重新获取一个Connection或Session。
 * SessionAwareMessageListener的设计就是为了方便我们在接收到消息后发送一个回复的消息，
 * 它同样为我们提供了一个处理接收到的消息的onMessage方法，但是这个方法可以同时接收两个参数，
 * 一个是表示当前接收到的消息Message，另一个就是可以用来发送消息的Session对象
 * 
 * @Author Huangjinwen
 * @Date 2017年12月10日-上午11:56:53
 */
public class MySessionAwareMessageListener implements SessionAwareMessageListener<TextMessage>{

	@Override
	public void onMessage(TextMessage arg0, Session arg1) throws JMSException {
		// TODO 对message进行处理
		
	}

}
