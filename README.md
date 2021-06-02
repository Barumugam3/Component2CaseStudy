E-Stock Market

This is a Micro services project created with Spring boot and Java version 1.8.
This project contains these microservices:

* EdgeMicroService: This service is based on Netflix Zuul and acts as an API Gateway
* DiscoveryMicroService: This service works as a eureka server and handles service discovery.
* CompanyMicroService: This service handles the operation for Company entity
* StockCommandService: This service follow CQRS Pattern with event sourcing (Apache Kafka) and handles the write operations for stock prices
* StockQueryService: This service follow CQRS Pattern with event sourcing (Apache Kafka) and handles the read operations for stock prices
* AuthMicroService: This service acts an in-memory authorization service. EdgeMicroService is dependent on this.
* Grafana Dashboards
* Prometheus
* ELK Stack: This uses Apache Kafka as a Log stream to LogStash which create indexes on these logs and send to Elasticsearch and Kibana is used to visualize the logs.

All the services are built and deployed using docker containers

To start the microservices:

* Clone this repository.
* Run ```docker-compose up --build -d``` from the parent directory, this will first build
  and then deploy the services.
* Or, if just want to run the services use ```docker-compose up``` from the parent directory

If everything goes well, you can access the following services at given location:

* Eureka-Service: http://localhost:8761
* Edge-Service: http://localhost:8092
* Auth-Service: http://localhost:8093
* Company-Service: http://localhost:8089
* Stock-Command-Service: http://localhost:8090
* Stock-Query-Service: http://localhost:8091
* ELK Stack: 
  > Elasticsearch - http://localhost:9200, Logstash - http://localhost:9600, Kibana - http://localhost:5601
* Grafana Dashboards - http://localhost:3000
* Prometheus - http://localhost:9090
* Kafka is running on port 29092 with following topics:
  > add-stock for adding stocks stock-command-service publishes and stock-query-service consumes messages from this topic
  
  > delete-stock for delete stocks stock-command-service publishes and stock-query-service consumes messages from this topic
  
  > stock-query-service-logs for publishing logs to kafka used by stock-query-service
  
  > stock-command-service-logs for publishing logs to kafka used by stock-command-service
  
  > company-service-logs for publishing logs to kafka used by company-service

API Documentation:

| API      | Endpoint | Edge Url     |
| ----------- | ----------- | ------------- |
| POST | /api/v1.0/market/company/register| http://localhost:8092/company-service/api/v1.0/market/company/register   |
| GET   | /api/v1.0/market/company/info/```companycode``` | http://localhost:8092/company-service/api/v1.0/market/company/info/1213 |
| GET | /api/v1.0/market/company/getall | http://localhost:8092/company-service/api/v1.0/market/company/getall |
| DELETE | /api/v1.0/market/company/delete/```companycode``` | http://localhost:8092/company-service/api/v1.0/market/company/delete/1213|
| POST | /api/v1.0/market/stock/add/```companycode```| http://localhost:8092/stock-command-service/api/v1.0/market/stock/add/1213|
| GET | /api/v/1.0/market/stock/get/```companycode```/```startdate```/```enddate```|http://localhost:8092/stock-query-service/api/v1.0/market/stock/get/1213/2021-05-31/2021-05-31|


* Company API is documented in [company_api.yaml](https://github.com/akhilgandhi/EStockMarketCaseStudy/blob/main/company_api.yaml)
    - Company API Swagger UI: http://localhost:8089/swagger-ui/
* StockPrice API is documented in [stockprice_api.yaml](https://github.com/akhilgandhi/EStockMarketCaseStudy/blob/main/stockprice_api.yaml)
    - StockPrice API Swagger UI: http://localhost:8090/swagger-ui/
  > Complete API can be accessed from Zuul Gateway Endpoint: http://localhost:8092/swagger-ui/