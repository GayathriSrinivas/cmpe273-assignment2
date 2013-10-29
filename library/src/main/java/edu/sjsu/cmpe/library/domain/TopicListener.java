package edu.sjsu.cmpe.library.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

public class TopicListener implements Runnable{

	private String user;
	private String password;
	private String host;
	private Integer port;

	private StompJmsConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Destination dest;
	private MessageConsumer consumer;
	private String topicName;
	private static BookRepositoryInterface bookRepository;
	
	public TopicListener(LibraryServiceConfiguration configuration,BookRepositoryInterface bookRepo){
		user = configuration.getApolloUser();
		password = configuration.getApolloPassword();
		host = configuration.getApolloHost();
		port = configuration.getApolloPort();
		topicName = configuration.getStompTopicName();
		bookRepository = bookRepo;
	}

	public void initTopic(){
		factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		try {
		    connection = factory.createConnection(user, password);
		    connection.start();
		    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);	
		    dest = new StompJmsDestination(topicName);
		    consumer = session.createConsumer(dest);
		}catch(JMSException e){
			System.out.println("Jms exe");
		}
	}
	
	/* If the book is Lost, set the status back to available
	 * else	if new book, create an entry in the hashmap
	 * else discard the message
	 */	
	public void processMessage(String body){
		
		ConcurrentHashMap<Long, Book> hashMap= bookRepository.getHashMap();
		
		//Parsing the input String 
		body.split("[^p]:");
		String[] parts= body.substring(0, body.length() - 1).split("\"?:\"?");
		
		if(parts[0] == null){
			System.out.println("parts[0] is empty");
			return;
		}
		
		long isbn = Long.parseLong(parts[0]);
		
		if(hashMap.containsKey(isbn)){
			Book book = hashMap.get(isbn);
			if(book.getStatus() == Book.Status.lost)
				book.setStatus(Book.Status.available);
		}else{
			Book book = new Book();
			book.setIsbn(Long.parseLong(parts[0]));
			book.setTitle(parts[1]);
			book.setCategory(parts[2]);
			String url = parts[3]+":"+parts[4];
			try {
			    book.setCoverimage(new URL(url));
			} catch (MalformedURLException e) {
			    e.printStackTrace();
			}
			hashMap.putIfAbsent(book.getIsbn(), book);
		}
	}
	
	/*  The Topic Listener is spawned in a new Thread and 
	 *  it runs in a while loop waiting for messages from the
	 *  topic publisher.
	 */
	@Override
	public void run() {
		try{
			while(true) {
				Thread.sleep(6000);
				System.out.println("Waiting for msg from topic :: " + topicName);
			    Message msg = consumer.receive();
				StompJmsMessage smsg = ((StompJmsMessage) msg);
				String body = smsg.getFrame().contentAsString();
				System.out.println("Received message from topic ::  "+ topicName);
				System.out.println("Message ::" + body);
				processMessage(body);
			}
		}catch(Exception exception){
			System.out.println("Error JMSException " + exception);
		}
	}
}
