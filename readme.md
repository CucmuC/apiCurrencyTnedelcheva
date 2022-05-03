# Invoicing API challenge

This is a sample Java / Maven / Spring Boot application that lets you sum invoice documents in different currencies via a file. 

## How to Run 

* Clone the repository 
* Make sure you are using JDK 1.8 and Maven 3.x

 No Tomcat or JBoss installation is necessary. Simply run the **ApiApplication**

## Additional Info
 
 The API endpoint is http://localhost:8080/upload
 
 The parameterrs which expects are as the example input of the task: 
 
 *file
 *currencyList
 *currency
 *vatNumber
 
 The parameters are passed in the body of the POST request (Postman used for test)
 
 The input data is verified upon some parameters, and custom exception messages are risen. 
 
 