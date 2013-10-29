package edu.sjsu.cmpe.library.config;

import javax.annotation.Nonnull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class LibraryServiceConfiguration extends Configuration {
    
	@NotEmpty
    @JsonProperty
    private String stompQueueName;

    @NotEmpty
    @JsonProperty
    private String stompTopicName;

    @NotEmpty
    @JsonProperty
    private String apolloUser;
    
    @NotEmpty
    @JsonProperty
    private String apolloPassword;
    
    @NotEmpty
    @JsonProperty
    private String apolloHost;
    
    @Nonnull
    @JsonProperty
    private Integer apolloPort;
  
    @NotEmpty
    @JsonProperty
    private String libraryName;
    
    
    
    public String getStompQueueName() {
	return stompQueueName;
    }

    public void setStompQueueName(String stompQueueName) {
	this.stompQueueName = stompQueueName;
    }

    public String getStompTopicName() {
	return stompTopicName;
    }

    public void setStompTopicName(String stompTopicName) {
	this.stompTopicName = stompTopicName;
    }
    
    public Integer getApolloPort() {
	return apolloPort;
    }

    public void setApolloPort(Integer apolloPort) {
	this.apolloPort = apolloPort;
    }
    
    public String getApolloHost() {
	return apolloHost;
    }

    public void setApolloHost(String apolloHost) {
	this.apolloHost = apolloHost;
    }
    
    public String getApolloPassword() {
	return apolloPassword;
    }

    public void setApolloPassword(String apolloPassword) {
	this.apolloPassword= apolloPassword;
    }
    
    public String getApolloUser() {
	return apolloUser;
    }

    public void setApolloUser(String apolloUser) {
	this.apolloUser = apolloUser;
    }
    
    public String getLibraryName() {
	return libraryName;
    }

    public void setLibraryName(String libraryName) {
	this.libraryName = libraryName;
    }
    
}
