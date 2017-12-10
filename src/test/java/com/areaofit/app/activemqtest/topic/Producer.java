package com.areaofit.app.activemqtest.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	
	// 连接工厂
	private ConnectionFactory connectionFactory;
	
	// 连接对象
	private Connection connection;
	
	// session对象
	private Session session;
	
	// 消息生产者
	private MessageProducer producer;
	
	public Producer() {
		try {
			this.connectionFactory = new ActiveMQConnectionFactory("hugh", "123456", "tcp://localhost:61616");
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.producer = this.session.createProducer(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Session getSession(){
		return this.session;
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
	
	public void sendMsg() {
		try {
			Destination destination = this.session.createTopic("topic1");
			MapMessage message1 = this.session.createMapMessage();
			message1.setString("name", "李思");
			message1.setString("age", "31");
			this.producer.send(destination, message1, DeliveryMode.NON_PERSISTENT, 2, 6000*1*10L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Producer producer = new Producer();
		producer.sendMsg();
		producer.destory();
	}

}
