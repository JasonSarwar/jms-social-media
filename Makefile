gradlebuild:
	gradle clean build

gradlerun: gradlebuild
	 gradle run

mavenbuild:
	mvn clean install:install-file -Dfile=core/lib/ojdbc6.jar -DgroupId=oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar install

mavenrun: mavenbuild
	mvn exec:java -Dexec.mainClass="com.jms.socialmedia.app.App"
	
dockerbuild:
	docker build -t jms-social-media .
	
dockerrun: dockerbuild
	docker run -p 4567:4567 --name jms-social-media jms-social-media