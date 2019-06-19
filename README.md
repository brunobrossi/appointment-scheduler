# Getting Started

  This project was done as part of the interview process at Sesame.
  
  Project was developed using Java 8, Spring Boot 2, Junit 5, gradle, docker, docker-compose, Test Containers.

### Prerequisites

  To run the project is necessary:
  
  - Docker
  - Docker-compose
  - Java 8
  
  The project uses Lombok for code generation, and to run on intelij is necessary to install the
    plugin called Lombok also, and on the properties search for ``Enable process annotations`` 
    to make it work properly.
    
## Tests

 The tests are using Test containers, that create a container binded on a random port for integration tests,
 the test class manges everything, so you can simple run the integration tests.
 
 ``./gradlew clean test``


## Deployment

The gradle project has a task to generate a docker image of the project

To generate the Docker image you can run:

```
./gradlew buildImage
```

It should run normally, but there is a BUG happening in the lib that can't read a docker config: ``.docker/config.json``
if this happens removing the json should make it work. You can remove an add again if needed.

The docker-compose file is already configured to run the Database + the project.

The project will start on the port 9090.


You can also start the project on your IDE, but to run the database only, and not the image of the project
simply run:

  ``docker-compose up -d postgres``
  
And run the project normally.

## Using the project

You can also checkout Swagger: ``Swagger -> http://localhost:9090/swagger-ui.html#/``

The are endpoint for each type action.
``

 Create a new Appointment: 
     
    POST - /api/v1/appointments
    
    This accepts a jsobody on this format:
    
      {
    	  "appointmentDate" : "2019-06-17T12:36:17.000",
    	  "appointmentDurationInMins": 15,
    	  "doctorName" : "bruno",
    	  "status" : "AVAILABLE",
    	  "price" : 10.00
      }

    Other fields will be auto-generated
   
 Get an appointment:
 
     GET - /api/v1/appointments/{id}
     
 Update an appointment:
 
    PUT -  /api/v1/appointments/{id}
    
    This accepts the status to be updated as:
    
           {
         	  "status" : "BOOKED"
           }
           
            OR
            
            {    
          	  "status" : "AVAILABLE"
            }           
 
 Delete an appointment:
 
     DELETE - /api/v1/appointments/{id}
     
 Search dates between:
 
     GET  - /api/v1/appointments/search?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd&sortType=ASC
     
     There is also an optional parameter for sorting the price sortType, this can be ASC or DESC, if not present
     the default one is ASC.
 
 
 Also a task will be generating Random appointments at random intervals
 
 
## Perks

  The project when running expose some endpoints for Documentation and Monitoring
  
   ``Health -> /actuator/health`` 
   See ``/actuator`` for more infos
   
   
   ``Prometheus -> /actuator/prometheus`` 
   Prometheus is monitoring the time on every request on the endpoints, check it out!
   
   
## Next Steps

  There are a couple improvements that can be done overtime:
  
  - Swagger is writing in the same database when you use Try it!, not a good approach.
  - Swagger documentation can be improved.
  - Bug with the Docker image creation.
  - Better use of spring Profile, now is only being used for the container
  - Monitoring the amount of HTTP 5XX, 4XX and etc.. 
  