package edu.sjsu.cmpe.library.domain;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
public class QueueProducer {
	
	private String user;
	private String password;
	private String host;
	private Integer port;
	private String queueName;
	private StompJmsConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Destination dest;
	private MessageProducer producer;
	
	
	public QueueProducer(LibraryServiceConfiguration configuration){
		user = configuration.getApolloUser();
		password = configuration.getApolloPassword();
		host = configuration.getApolloHost();
		port = configuration.getApolloPort();
		queueName = configuration.getStompQueueName();
	}

	public void initialise(){
		factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		try {
		    connection = factory.createConnection(user, password);
		    connection.start();
		    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);	
		    dest = new StompJmsDestination(queueName);
		    producer = session.createProducer(dest);
		    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}catch(JMSException e){
			System.out.println("Jms exe");
		}
	}
	
	public void sendDataByQueue(String data){
	
		try{
		    TextMessage msg = session.createTextMessage(data);
			msg.setLongProperty("id", System.currentTimeMillis());
			System.out.println("Sending msg :"+ msg.getText());
			producer.send(msg);
		}
		catch(JMSException exception)
		{
			System.out.println("Error JMSException " + exception);
		}
	}
	
	public void close(){
		try{
		connection.close();
		}
		catch(JMSException exception)
		{
			System.out.println("Error JMSException " + exception);
		}
	}
	
}
