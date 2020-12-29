[![CircleCI](https://circleci.com/gh/springframeworkguru/mssc-beer-order-service.svg?style=svg)](https://circleci.com/gh/springframeworkguru/mssc-beer-order-service)

# MSSC Beer Order Service

Steps for Deconstruction into  Microservices
1. Dependency Management
2. (Local) MySQL Configuration
3. JMS Messaging
4. JMS with Microservices

Deconstruction Process - 12/28/2020

	- Where We Are At
		- Beer Monolith has been broken down into 3 independent microservices
			- Beer Service, Beer Inventory and Beer Order Service
		- Each microservice is using its own in-memory database, its own repository
		- Setup inter-service communication for read operations
			- the Beer API is dependent on Beer Inventory to get inventory data 
			- the Beer Order Service is dependent on Beer Service to get data about the Beer objects themselves (descriptions, properties, etc.)
			- So we can see how these services are starting to work together

	- What's Not Working
		- Order Allocation is not working
		- Beer 'Brewing" is not working
		- Monolith was using Spring Events and scheduled jobs for these features
			- Events and consumers of events is broken
		- The 3 services are using Maven, but there is a high degree of duplication in Maven POM files


	- Next Steps
		- Establish a Maven BOM (Bill of Materials) to reduce duplication in Maven POMs
			- Also - good technique for standardization and compliance across the enterprise
			- By making all subsystems and microservice POMs dependent on a BOM, don't have to go in and update each one every time some version is updated or patched
		- Setup MySQL for database - will help with trouble shooting, and configuration for target deployment
		- Transaction to JMS (Java Message Service) to publish events as messages
		- Build Saga's to coordinate microservice actions for events
			- ie Order Allocation