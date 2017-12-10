package com.areaofit.app.listener.messageListener;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 
 * @Description 消息监听器
 * 
 * MessageListener是最原始的消息监听器，它是JMS规范中定义的一个接口。
 * 其中定义了一个用于处理接收到的消息的onMessage方法，该方法只接收一个Message参数。
 * 
 * @Author Huangjinwen
 * @Date 2017年12月10日-上午11:53:07
 */
public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message paramMessage) {
		// TODO 对message进行处理
	}

}
