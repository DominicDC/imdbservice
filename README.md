<h4><b>Project Overview:</b></h4>
<pre>
<b>Project Name:</b> ImdbService
<b>Description:</b> A collection of RESTful services to download feed from IMDB, persist it and re-compute ratings based on case study   
<b>Tech stack:</b> Spring Boot (REST) + POSTGRES + FLYWAY (migration) + Spring JPA (ORM: Hibernate) + Spring HATEOAS
</pre>

<h4><b>Case Study:</b></h4>
<pre>
1. Build an application which pulls movies, their ratings, and cast lists from IMDB.
   <b>NOTE:</b> <i>To limit scope, please pull only movies released in 2017</i> 
2. Persist the feed into POSTGRES DB for verification
3. Calculate the rating for a season based on its episodes. You can implement your own logic
4. Expose a service to update the rating for a title  
</pre>

<h4>Insructions:</h4>
<pre>
1. Download the project on your local and build as a Maven project
2. Run the application
3. <b>Testing via POSTMAN:</b> Import "ImdbService.postman_collection.json" collection into POSTMAN
4. <b>Testing via curl command:</b> Execute commands from file "CurlCommands"
</pre>