package com.in28minutes.rest.webservices.restfulwebservices.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InstanceInformationService {

	private static final String HOST_NAME = "HOSTNAME";

	private static final String DEFAULT_ENV_INSTANCE_GUID = "LOCAL";

	// @Value(${ENVIRONMENT_VARIABLE_NAME:DEFAULT_VALUE})
	@Value("${" + HOST_NAME + ":" + DEFAULT_ENV_INSTANCE_GUID + "}")
	private String hostName;
	
	// Explanation for the above line:
	// @Value("${HOSTNAME:LOCAL})
	// It will search for the variable name with HOSTNAME.
	// HOSTNAME has to be specified in configuration.
	// In this case, HOSTNAME will be available in Docker Runtime Environment.
	// HOSTNAME will be available when we deploy in Kubernetes as well.
	// If not available, it will be displayed as LOCAL.

	public String retrieveInstanceInfo() {
		return hostName.substring(hostName.length()-5);
	}

}