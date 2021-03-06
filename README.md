# The FINDER service

Welcome to my playground. This is simply an opportunity to play with **Spring Boot**, **AWS**, **Alexa** and because we use Google Calendar at the office, **Google Calendar**.  It is an effort to learn and play - suggestions welcome.

This is an Alexa Service that calls Google Calendar API to get a person's current event (meeting).

The Finder service is an Alexa Web Service built with Spring Boot that handles a variety of requests from an Alexa device through customer handlers. The HandlerSpeechlet class extends the Amazon Alexa SDK SpeechletV2 following a factory pattern to instantiate and respond to the request through the appropriate handler:

1. AMAZON.CancelIntent - maps to the AmazonCancelIntentHandler class
2. AMAZON.FallbackIntent - maps to the AmazonFallbackIntentHandler class
3. AMAZON.HelpIntent - maps to the AmazonHelpIntentHandler class
4. AMAZON.StopIntent - maps to the AmazonStopIntentHandler class
5. FindPeople - custom intent created to call a serivce.  In this case, it calls the Google Calendar service to then locate people via their Google Calendar.

## Why not use a Lambda function?

This is a simple Java web application, built with Spring Boot.

Why would I use Spring boot deployed into an EC2 instance over an AWS Lambda? Well ... I don't get to code much anymore and I wanted a reference implementation of both Spring boot AND an Alexa skill so this is about killing two birds with one stone. 

## Minimum configuration

1. java version "1.8.0_45" (build 1.8.0_45-b14)
2. Apache Maven 3.3.3

You will need Google API credentials saved to the resources directory in order to get Google Calendar Events from Google Calendar. You can generate JSON format credentials here: https://console.developers.google.com/apis/credentials. Note the application.properties file in the resources directory has a setting for this file in case you call it something different.

Application configuration can be found under /src/main/resources/application.properties

## Build

$ mvn clean package (defaults -P development)

Maven build command: mvn clean package -P production</br>

Use: <strong>-P stage</strong> for building to the AWS dev account.</br>

Use: <strong>-P production</strong> for building to the AWS prod account.</br>

To skip tests add: <strong>-Dmaven.test.skip=true</strong></br>

If running locally run in develloment mode, which will use AWS credentials locally stored in .aws directory.</br>
Deploy in Production mode, where the service will look to IAM permissions to access Cognito (see DAO class for credential management).</br>

Alternatively after calling 'mvn clean package', you can run the application locally in debug mode: </br>

`java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/directory-0.0.1-SNAPSHOT.war. `

Using Eclipse, you can then be easily connected to the running application through Debug Configurations and creating a Remote Java Application configuration.</br>

## Supported REST endpoints

OpenAPI3.0 (or Swagger) documentation is available here: http://[hostname]:[port]/swagger-ui.html for example, http://lcoalhost:8080/swagger-ui.html

Alternatively, for auto-generating an AWS API Gateway you need the output from /api-docs for example, http://localhost:8080/api-docs

GET localhost:8080</br>
GET localhost:8080/snoop</br>
GET localhost:8080/health heartbeat endpoint to verify health.</br>
GET localhost:8080/quote Calls the Spring Boot random expression generator. This is just for fun.</br>
GET localhost:8080/calendar?user=john%20doe Calls the same behavior Alexa calls via the /find endpoint.</br>
POST localhost:8080/find Called from the com.youi.finder.alexa.config.Configuration servlet and is used to call the HandlerSpeechlet handler factory to process the Alexa request.  This is the endpoint you need to use in your alexa skill.</br>
</br>

/find and POST requests require header: Content-Type = application/json when using Postman.






