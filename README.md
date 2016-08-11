# salesstock_tech
Backend Technical Assestment on salesstockid

Pattern dan Konsep 
------------------
- Domain Driven Design untuk logic 
- Messaging System untuk publish dan subscribe semua event yang terjadi.
- CQRS / Comand Query Responsibility Segration untuk beberapa view yang besar agar dapat ditampilkan secara cepat (ex: olap engine for analystical report).
- Event Sourcing untuk aggregate snapshot

STACK DEVELOPMENT
-----------------
	- Java8
	- Spring Boot
	- Axon Framework
    - Maven
	- Mongo
	- Redis
	- Postgress
	- ELK (Elasticsearch/Logstash/Kibana)
	- 
	
Package Foundation
-----------------
letak core config untuk framewok-framework yang digunakan, spring boot, axon, spring data sampai serialization

Package Salesstock
-----------------
terderi dari 3 package yaitu API / Domain / Intfrastructure

Package Monolith
-----------------
container spring boot untuk applikasi secara kesuluran.. 
