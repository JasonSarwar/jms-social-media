# my-social-media

To start, using Maven, run the maven_install script and the do 
`mvn exec:java`

To start, using Gradle, do
`gradle build run`

Make sure that the `mock_data_service.use` property in `api-sparkjava/src/main/resources/application.properties` is set to true. Otherwise, the program will attempt to connect to an Oracle database specified in the properties file. Will post the required SQL scripts soon.
