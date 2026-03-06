<h2>how to deploy:</h2>
<p>first we need to deploy a database which is PostgreSQL</p>
<p>then deploy repository of the springboot project but for the first time it will crash</p>
<p>so we need to fix it. First i need to change the code in application.properties: localhost to ${PGHOST}, serverport to ${PGPORT}, name of database to ${PGDATABASE} this for url and then change username to ${PGUSER} and password to ${PGPASSWORD}</p>
<p>next we need to copy the value of PGDATABASE,PGHOST(copy url from public network domain),PGPORT(number in url from public network domain),PGPASSWORD,PGUSER from variable in PostgreSQL dtabase and paste it into variable of springboot project</p>
<p>then deploy it and it will success</p><br>
<h2>what i use inn this project:</h2>
<p>- Railway for deploy</p>
<p>- IDE for SpringBoot</p>
<p>- PostgreSQL for Database</p>
<p>- in Springboot i use thymeleaf, lombok, Spring web, Spring security, Spring Data JPA, PostgreSQL Driver</p>
