# Architecture
* The project is divided into a few modules. 
* Some of those modules are designed to evolve as independent micro-services in the future
* The project is kept as one monolothic service dur to lack of time to test the distributed architecure if multiple micro-services were used (would have needed a discovery service, a gateway, feign clients, etc)
* Modules that represent a potential micro-service are usually desinged in pairs: **{name}-api** and **{name}-service**
* **{name}-api** modules include public interfaces and DTOs that can be used by other modules. When potentially moving them into a new micro-service they would include the feign clients that would allow calling the REST implementation provided by the underlying **{name}-service** modules 
* **{name}-service** modules include controllers (REST API implementations) and the underlying services, respositories, converters and error hadnling. So basically the actual business logic owned by the micro-service.
* The reason foreign keys are not used is because of the assumption that those services would be running in their own space and using their own databases.

# Services
## Reservation application
Has one module:
* reservation-app includes all the required dependencies to run the appliation, as well as the configurations and the main Spring Boot app.

## Schema Migration
Has one module:
* schema-migration has flyway migrations that allow creating and altering tables, column, etc. as well as populating initial data.

## User Service
Has two modules:
* user-api has public interfaces and dtos
* user-service has CRUD operations for user entities that are run against Posgtres `users` table.

## Availability Service
Has two modules:
* availability-api has public interfaces and DTOs
* availability-service provides an endpoint to find when the camp site is available.   
It also helps to protect against overlapping reservations by providing an optimistic lock over the populated `day` column. It also has a job that runs every night at exactly 12AM to remove today from the list of availability and add one day to the populated 30 days that the user can search.

## Reservation Service
Has two modules:
* reservation-api has public interfaces and dtos
* reservation-service provides CRUD endpoints for the campsite reservation.

## Improvements
* bundle the service and the api in a single module that has the pair
* add logs
* better error handling
* caching and indexing
* the job needs to validate the availabilities every time it starts to make sure it didn't skip a day due to a crash for example

# REST API
## reserve
`curl -X POSTation/json" --data '{"user": {"email": "wfadel@gmail.com", "firstName": "Waseem", "lastName": "Fadel"}, "checkInDate": "2020-09-06", "checkOutDate": "2020-09-09"}' | jq`

## get reservation
`curl -X GET 'http://localhost:8080/api/v1/reservations/e1472106-1fd2-4788-bac8-4552644a0ebe' | jq`

## update reservation
`curl -X PUT 'http://localhost:8080/api/v1/reservations/e1472106-1fd2-4788-bac8-4552644a0ebe' --header "Content-Type: application/json" --data '{"user": {"email": "wfadel@gmail.com", "firstName": "Waseem", "lastName": "Fadel"}, "checkInDate": "2020-09-07", "checkOutDate": "2020-09-09"}' | jq`

## cancel reservation
`curl -X DELETE 'http://localhost:8080/api/v1/reservations/7f967244-1871-4420-9e10-54ff9b1d68b6'`

## find availabilities
`curl 'http://localhost:8080/api/v1/availabilities?start_date=02-09-2020&end_date=08-09-2020' | jq`
