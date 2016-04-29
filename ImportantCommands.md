###Tasks

######./gradlew ear

compile  and create ear (location build/libs)

######./gradlew cargoRun --info 

start jboss to view the result open browser on http://localhost:8080/web/

######./gradlew test

run tests 

######./gradlew  runH2

starts a h2 database you dont have to invoke this commat it is called automaticaly by cargoRun

######./gradlew webH2

start a h2 webservice which can be used to manage the embeded db  (select generic h2 server)

the connection string is jdbc:h2:tcp://localhost/~/ASE

###Folders/Projects

######businessEjb 

contains the bussines logic also provides rest interface

######businessInterface 

interfaces for the bussineslogic (required for ejb)

######common 
Included by all pojects currently contains model classes

######dao

Session beans for database operations ie Data Access Objects

######daoInterface 

Interfaces for Daso beans

######lib 

java library files contains h2 database stuff

######web

the web project contains jsp js css ... 

=======
to build:
./gradlew ear

to start server 
./gradlew cargoRun --info
http://localhost:8080/web/


to start the whole system with the UI client

feature/clientside branch einchecken
./gradlew restoreDatabase
./gradlew cargoRun --info

und dann localhost:8080/web/... aufrufen
