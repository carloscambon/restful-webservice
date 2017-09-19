# Offer WebService

Restful api that allows a merchant to create a new simple offer

## Getting Started

Clone the repository at:

https://github.com/carloscambon/restful-webservice

### Prerequisites

It needs to run on Java 1.8 and uses Maven for dependency management

### Installing

You can get the Offer Webservice started by:

   a) On your IDE, run the OfferWebservice class as an application
   b) Or from a terminal run the same class:

### Release

To release a new version execute the following maven command:

mvn clean compile assembly:single

This will create the executable jar with the release

### Running the application
 
 You can run the class OfferWebservice or create a release as above and run the jar generated in "target" folder as:
  ```
 java -jar ./target/offer-webservice-[version].jar 
  ```
 By default the server will run on port 8080, but you may also specify the server port:
  ```
 java -Dserver.port=4567 -jar ./target/offer-webservice-[version].jar 
  ```
 Once the server is started, you may do a POST to the url:
  ```
 http://localhost:8080/services/offers
  ```
 with a sample offer:
 
 ```
 {
     "title": "Washing machine in good condition",
     "description": "In warranty, free delivery in UK",
     "price": "244,65"
 }
 ```
 
 And you will get a response:
 ```
 {
     "status": "Success",
     "data": {
         "title": "Washing machine in good condition",
         "price": "24465.00 GBP",
         "description": "In warranty, free delivery in UK",
         "id": "4b8575f1-eb4f-47b6-8342-95ca32bcab4e"
     }
 }
 ```
 
 The restful Offer api supports POST, PUT, GET and DELETE

## Running the tests

Run the OfferWebserviceTest class on your IDE, or run the maven command:

mvn test

## Built With

* [Spark Framework](http://sparkjava.com/documentation) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [JUNIT 5](http://junit.org/junit5/) - Testing framework
