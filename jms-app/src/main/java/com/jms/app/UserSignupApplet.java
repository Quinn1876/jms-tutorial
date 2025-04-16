package com.jms.app;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.cli.ArgumentParser;
import com.cli.ArgumentParserException;
import com.google.gson.Gson;
import com.jms.appachemq.AppacheMQConfigurations;
import com.jms.model.Topics;
import com.jms.model.UserSignUpEvent;

public class UserSignupApplet {
	public static void main(String[] argV) {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AppacheMQConfigurations.getUrl());
		
		ArgumentParser parser = ArgumentParser.builder()
				.addPositionalArgument("email")
				.addPositionalArgument("user_name")
				.build();
		try {
			Connection connection = connectionFactory.createConnection(AppacheMQConfigurations.getUser(), AppacheMQConfigurations.getPwd());
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(Topics.USER_SIGN_UP);
			MessageProducer producer = session.createProducer(topic);
			Gson gson = new Gson();
			
			var args = parser.parseArgs(argV);
			
			UserSignUpEvent event = new UserSignUpEvent(args.getStringArg("user_name"), args.getStringArg("email"));
			
			TextMessage message = session.createTextMessage(gson.toJson(event));
			
			producer.send(message);
			
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (ArgumentParserException e) {
			System.out.println("Usage: UserSignupApplet [email] [user_name]");
		}
		System.out.println("[UserSignUpApplet] Exiting...");
	}
}
