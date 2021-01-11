##### Part of John Thompson's Microservices course

**Beer Service** is responsible for generating the Beer objects used in the application and stores that Beer object data in a database. 
**Beer Order Service** and **Beer Inventory** make calls to **Beer Service** to get information about the Beer objects.

Beer object example:

- UUID **id** = '026cc3c8-3a0c-4083-a05b-e908048c1b08' 
- String **beer_name** = 'Pinball Porter' 
- String **beer_style** = 'PORTER' 
- Timestamp **created_date** = CURRENT_TIMESTAMP 
- Timestamp **last_modified_date** = CURRENT_TIMESTAMP 
- Integer **min_on_hand** = 12 
- Integer **quantity_to_brew** = 200 
- BigDecimal **price** = 12.95 
- String **upc** = '0083783375213' 
- Long **version** = 1


[![CircleCI](https://circleci.com/gh/kawgh1/mssc-beer-order-service.svg?style=svg)](https://circleci.com/gh/kawgh1/mssc-beer-order-service)

# MSSC Beer Order Service

## Steps for Deconstruction into  Microservices
#### 1. Dependency Management
#### 2. (Local) MySQL Configuration
#### 3. JMS Messaging
#### 4. JMS with Microservices
#### 5. Spring State Machine
#### 6. Using Sagas with Spring
#### 7. Integration Testing Sagas
#### 8. Compensating Transactions
#### 9. Spring Cloud Gateway

- # Refactoring Model to Common Package
    - ### Goal - refactor package structure in all 3 microservices to share a common 'Brewery' package for all the Java objects that are shared between the microservices
        - When microservices are working with identical objects between them, avoids a lot of headaches with conversions, object manipulations, etc.
        - much cleaner, safer, etc.
          
    - **Model** - Objects exposed to JSON 
        - #### things that we will send out to the Client as JSON, changing them, and bring them back in to the back-end as JSON, convert to Java objects and perform business logic and database operations from there
    - **Problem** - In Spring, serialization / de-serialization expects same package / object name
        - Could address w/ additional Jackson configuration
    - **Solution** - Move model objects to package - 'com.kwgdev.brewery.model'
        - All services are related under the 'Brewery' set of services
    - **Problem** - Lombok Builders with parent classes problematic
        - **Solution** - flatten classes
        
        
- # Notes on Agile Software Development Process
    - Agile Software Development is an iterative development process
        - Software is developed in small increments
        - Widely adopted by companies
            - Often poorly implemented!
        - Agile is a VERY large topic!
    - Companies that use Agile often use SCRUM
    - SCRUM is a process framework
        - Tasks are planned in a backlog
        - Tasks are planned into a "Sprint"
    - A **Sprint** is typically a 2-4 week period of work for 5 to 9 people
        - Length and team size are highly debated
    - At the ***end of a Sprint***, a **Retrospective** is held
        - Purpose is to reflect and improve for next iteration
        
    - ## A Typical Sprint Retrospective Model
        - ## What worked well?
            - Planning Issues in GitHub
                - Allows better visibility of what source code was changed
            - Having CI Builds to catch problems
                - 3 failures detected
        - ## What could be improved?
            - Ideally should not have had 3 CI build failures
                - Testing is VERY light
            - Need better examples of Compensating Transactions
        - ## What will we commit to doing in the next Sprint?
            - Improve Test Coverage
                - Testing has not been a focus
                - Saga's clearly have a lot of moving parts!
                    - Verify the Saga with Saga Integration Tests
                - Improve examples of Compensating Transactions
                
        - ## *Scrum Team members make actionable commitments*
            - ### Integration Tests
                - Unit Tests typically target a single class
                - Integration tests will test the interactions between components
                - In our microservice, we want to test:
                    - Receiving a new order
                    - Sending / Receiving JMS messages
                    - Persistence
                    - State Changes in the State Machine
                    
            - ### Compensating Transactions
                - Problem - current 'non-happy' path events end in a terminal state
                - need example of compensating transactions
                    - Non-happy path - call service to 'undo' action

- Deconstruction Process - 12/28/2020

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