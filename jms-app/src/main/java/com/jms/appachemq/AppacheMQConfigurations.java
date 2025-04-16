package com.jms.appachemq;

import org.apache.activemq.ActiveMQConnection;

public class AppacheMQConfigurations {
	// TODO: Move these into environment variables
	private static String user = "artemis";
	private static String pwd = "artemis";
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	public static String getUser() {
		return user;
	}
	
	public static String getPwd() {
		return pwd;
	}
	
	public static String getUrl() {
		return url;
	}
}
