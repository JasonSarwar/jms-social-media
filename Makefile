gradlebuild:
	gradle clean build

gradlerun: gradlebuild
	 gradle run

mavenbuild:
	mvn clean package

mavenrun: mavenbuild
	mvn exec:java -Dexec.mainClass="com.jms.socialmedia.app.App"

dockerbuild:
	mvn -P assemble-jars package
	docker build --build-arg PATH_TO_LIB=./api/target/libs/ --tag jms-social-media .

dockerbuildwithgradle:
	gradle clean allJars
	docker build --build-arg PATH_TO_LIB=./api/build/libs/ --tag jms-social-media .

dockerrun: dockerbuild
	docker run --rm -p 4567:4567 --name jms-social-media jms-social-media

dockerrunwithgradle: dockerbuildwithgradle
	docker run --rm -p 4567:4567 --name jms-social-media jms-social-media

dockercompose: dockerbuild
	docker-compose up

mavenclean:
	mvn clean

gradleclean:
	gradle clean

clean: mavenclean gradleclean
	docker-compose down
	docker rm -f jms-social-media | true
