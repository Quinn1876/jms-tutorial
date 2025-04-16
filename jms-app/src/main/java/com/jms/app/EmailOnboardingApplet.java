package com.jms.app;

import java.io.FileWriter;
import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;
import com.jms.appachemq.AppacheMQConfigurations;
import com.jms.model.Topics;
import com.jms.model.UserSignUpEvent;

public class EmailOnboardingApplet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AppacheMQConfigurations.getUrl());
		try {
			
		Connection connection = connectionFactory.createConnection(AppacheMQConfigurations.getUser(), AppacheMQConfigurations.getPwd());
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(Topics.USER_SIGN_UP);
		MessageConsumer consumer = session.createConsumer(topic);
		Gson gson = new Gson();
		
		while (true) {
			System.out.println("Waiting for message...");
			Message msg = consumer.receive();
			System.out.println("Message received.");
			TextMessage textMessage = (TextMessage) msg;
			UserSignUpEvent event = gson.fromJson(textMessage.getText(), UserSignUpEvent.class);
			FileWriter fw = new FileWriter("output/output_email" + event.getEmail() + ".txt");
			fw.write("TO: " + event.getEmail() + "\n\nHello " + event.getUserName() + ",\nWelcome to the org. Please click the link below to complete registration");
			fw.close();
		}
		
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
