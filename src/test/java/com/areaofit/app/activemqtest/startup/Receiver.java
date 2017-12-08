package com.areaofit.app.activemqtest.startup;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * @Description 消息的接收方
 * @Author Huangjinwen
 * @Date 2017年12月5日-上午10:39:51
 */
public class Receiver {

	public static void main(String[] args) throws Exception {

		// 第一步---->创建ConnectionFactory工厂对象，需要填入用户名、密码、连接地址，默认的连接及端口号为
		// tcp://localhost:61616
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://localhost:61616");

		// 第二步---->创建connection连接，并启动连接
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// 第三步---->创建session会话（上下文环境对象），用于接受消息，有2个参数配置，第一个参数为是否启用事物模式；第二个参数为签收模式，一般设置自动签收
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 第四步---->创建Destination对象，对于生产者来说，它是消息的目的地；对于消费者来说，它是消息的来源
		Destination destination = session.createQueue("Test");

		//第五步---->创建消费者MessageConsumer，指定Destination
		MessageConsumer consumer = session.createConsumer(destination);

		TextMessage message = (TextMessage) consumer.receive();

		System.out.println(message.getText());

		if (connection != null) {
			connection.close();
		}
	}
}
