package edu.sjsu.cmpe.procurement.domain;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;
import edu.sjsu.cmpe.procurement.config.ProcurementServiceConfiguration;


@Every("2min")
public class QueueConsumer extends Job {
	
	private static String user;
	private static String password;
	private static String host;
	private static Integer port;
	private static String queueName;
	private static StompJmsConnectionFactory factory;
	private static Connection connection;
	private static MessageConsumer consumer;
	private static Destination dest;
	private static Session session;
	private static Client client;

	
	public QueueConsumer(){
		//no arg constructor
	}
	
	public QueueConsumer(ProcurementServiceConfiguration configuration){
		user = configuration.getApolloUser();
		password = configuration.getApolloPassword();
		host = configuration.getApolloHost();
		port = configuration.getApolloPort();
		queueName = configuration.getStompQueueName();
		client = Client.create();
	}
	
	public void initQueue()
	{
		try
		{	
			factory = new StompJmsConnectionFactory();
			factory.setBrokerURI("tcp://" + host + ":" + port);
			connection = factory.createConnection(user, password);
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			dest = new StompJmsDestination(queueName);
			consumer = session.createConsumer(dest);

		}catch(JMSException e){
			e.printStackTrace();
		}
	}
	
	public void submitBookOrder(OrderBook book)
	{
		//Post response to Publisher
		try{			
			WebResource webResource = client.resource("http://54.215.210.214:9000/orders");
			String response = webResource.type(MediaType.APPLICATION_JSON_TYPE)
										.post(String.class,book);
				 
			System.out.println(response);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	@Override
	public void doJob() {
		try 
		{
			//TODO wait till queue is empty and keep adding ISBN to array.
			
			System.out.println("Waiting for messages from " + queueName + "...");
			String body ="";
			Message msg;
			OrderBook book = new OrderBook();
			boolean isBookLost = false;
			
			while((msg = consumer.receive(1000*30)) !=null){
				if(msg instanceof StompJmsMessage ){
					StompJmsMessage smsg = ((StompJmsMessage) msg);
					body = smsg.getFrame().contentAsString();
					System.out.println("QueueConsumer Received message = " + body);
					String[] parts=body.split(":");
					System.out.println("ISBN Number to be sent ::"+parts[1]);
					book.setOrder_book_isbns(Integer.parseInt(parts[1]));
					isBookLost = true;
				}
			}
			if(isBookLost)
				submitBookOrder(book);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
