package jcg.demo.jms.util;

import jcg.demo.model.CustomerEvent;
public class DataUtil {
	public static String NEW_CUSTOMER = "NEW_CUSTOMER";
	public static String TEST_GROUP1_QUEUE_1 = "test.group1.queue1";
	public static String TEST_GROUP1_QUEUE_2 = "test.group1.queue2";
	public static String TEST_GROUP2_QUEUE_2 = "test.group2.queue2";
	public static String TEST_GROUP2_QUEUE_1 = "test.group2.queue1";
	public static String TEST_GROUP1_TOPIC = "test.group1.topic";
	public static String CUSTOMER_VTC_TOPIC = "VirtualTopic.Customer.Topic";
	public static String TEST_VTC_TOPIC_SELECTOR = "JCG.Mary.Topic";
	
	public static CustomerEvent buildDummyCustomerEvent() {
		return new CustomerEvent(NEW_CUSTOMER, 0);
	}
}
