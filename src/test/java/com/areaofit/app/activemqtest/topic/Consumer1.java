package com.areaofit.app.activemqtest.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer1 {

	// 连接工厂
	private ConnectionFactory connectionFactory;

	// 连接对象
	private Connection connection;

	// session对象
	private Session session;
	
	// 消息地址
	private Destination destination;

	// 消息消费者
	private MessageConsumer consumer;
	
	public Consumer1() {
		try {
			this.connectionFactory = new ActiveMQConnectionFactory("hugh", "123456", "tcp://localhost:61616");
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.destination = this.session.createTopic("topic1");
			this.consumer = this.session.createConsumer(destination);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receiver() {
		try {
			this.consumer.setMessageListener(new Listener());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void destory(){
		try {
			if (this.connection != null) {
				connection.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	class Listener implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
				if (message instanceof MapMessage) {
					MapMessage mapMsg = (MapMessage) message;
					System.out.println(mapMsg.toString());
					System.out.println(mapMsg.getString("name"));
					System.out.println(mapMsg.getString("age"));
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Consumer1 consumer = new Consumer1();
		consumer.receiver();
	}
}
