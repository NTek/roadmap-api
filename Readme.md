# Roadmap backend #
[ ![Codeship Status for Ramotion/roadmap-api](https://codeship.com/projects/2ba911f0-4afc-0132-8280-0ac24b6883cd/status?branch=master)](https://codeship.com/projects/46560)

### Version 0.1 ###

* Hello world =)

### Version 0.1.1 ###

* Added database support with JPA
* Implemented couple entities of model

### Version 0.1.2 ###

* Implemented preview version of model
* Implemented listener update audit timestamps

### Version 0.1.3 ###

* Experiments with STOMP over websocket
* Some minor changes in model entities

### Version 0.2.0 ###

* Added Liquibase(www.liquibase.org) tool for database version control and migration
* Added datasource configuration from environment variables for heroku

### Version 0.2.1 ###

* Some changes for heroku database

### Version 0.2.2 ###

* Fixes in heroku database connection settings
* Fixes in model entities

### Version 0.2.3 ###

* Configured logging to file
* Refactored database connection settings

### Version 0.2.4 ###

* Many changes in model
* Languages moved from model entity to enum with all ISO639-1 language codes
* Web API interface designed
