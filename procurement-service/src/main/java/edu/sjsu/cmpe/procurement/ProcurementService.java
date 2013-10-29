package edu.sjsu.cmpe.procurement;

import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import de.spinscale.dropwizard.jobs.JobsBundle;
import edu.sjsu.cmpe.procurement.api.resources.RootResource;
import edu.sjsu.cmpe.procurement.config.ProcurementServiceConfiguration;
import edu.sjsu.cmpe.procurement.domain.QueueConsumer;
import edu.sjsu.cmpe.procurement.domain.TopicPublisher;

public class ProcurementService extends Service<ProcurementServiceConfiguration> {

    public static void main(String[] args) throws Exception {
	new ProcurementService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ProcurementServiceConfiguration> bootstrap) {
	bootstrap.setName("procurement-service");
	bootstrap.addBundle(new JobsBundle("edu.sjsu.cmpe.procurement"));
    }

    @Override
    public void run(ProcurementServiceConfiguration configuration,
	    Environment environment) throws Exception {
   
    final Client client = new JerseyClientBuilder().using(configuration.getJerseyClientConfiguration())
                .using(environment)
                .build();
       	
	    QueueConsumer queueConsumer = new QueueConsumer(configuration);
	    queueConsumer.initQueue();
		
	    TopicPublisher topic = new TopicPublisher(configuration,client);
	    topic.initTopic();

	/** Root API */
	environment.addResource(new RootResource());
    }
}
