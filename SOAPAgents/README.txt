Files:
- SMTP_Client:
	- This package includes all of the source code for the C# .NET SMTP Client Web Service.
	- In order to run this web service, you will need: 
		- .NET
		- IIS
	- Simply build the source to IIS

- demo
	- this package includes all of the java source codes for both the AddressBook Web service and the SOAPAgents
	- AddressPackage contains all of the source for the AddressBook Web Service.
	- SOAPAgents contains all of the source code for the SOAPAgents(Repair, SOAPAgentAPI). It also contains a sample client that simulates Workflake's role called Client.
	- javadocs contains all of the java documentation for the SOAPAgents.

- Running AddressBook service:
	- You will need Tomcat, and apache axis.
	- Please read installation instructions for axis on tomcat
	- Copy AddressBook.jar into tomcat_home/webapps/axis/WEB-INF/lib
	- run tomcat
	- deploy the service by running the wsdd file you find in the AddressPackage/ws folder
		org.apache.axis.client.AdminClient AddressPackage/ws/deploy.wsdd

- Running SOAPAgents:
	- Make sure the AddressBook service is running
	- register the RMI service which is SOAPAgentService
	- A sample policy file can be found in the SOAPAgents package.
		rmiregistry &
		rmic SOAPAgents.SOAPAgentAPI
		java -Djava.security.policy=/SOAPAgents/policy SOAPAgents.SOAPAgentService

- Running Client:
	- Make sure that the RMI service is already running
	- simply run the Client
	- The current SOAPAgentService is set to run locally.
		java -Djava.security.policy=/SOAPAgents/policy SOAPAgents.Client localhost

- Notes:
	- The URL for the web serivces are hardcoded into Client. Please change the URLs to the appropriate URL to where you have the services running.
	- As mentioned above, the RMI Service is set to run in the same host as the RMI Client. You can easily make changes to the RMI Service to run on another host.