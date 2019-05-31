# The FINDER service

This is a simple Java web application, built with Spring Boot.

Why would I use Spring boot over an AWS Lambda? Well ... I don't get to code much anymore and I wanted a reference implementation of both Spring boot AND an Alexa skill so this is about killing one bird with two stones. 

## Minimum configuration

1. java version "1.8.0_45" (build 1.8.0_45-b14)
2. Apache Maven 3.3.3

hint: if you have a mac, use homebrew to install the above.

## Build/Run

1. $ mvn clean install
2. $ mvn spring-boot:run

Application configuration can be found under /src/main/resources/application.properties

## Supported REST endpoints

GET localhost:8080</br>
GET localhost:8080/snoop</br>
GET localhost:8080/health</br>
POST localhost:8080/find</br>
POST localhost:8080/getUserData</br>

/find and /getUserData POST requests require header: Content-Type = application/json






