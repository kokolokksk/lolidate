git pull
#export PATH=/home/app/maven/apache-maven-3.8.1/bin:$PATH
#mvn -B package --file pom.xml
gradle bootJar
cp build/libs/lolidate-0.0.1-SNAPSHOT.jar ~/lolidate/
# shellcheck disable=SC2164
cd ~/lolidate
./stop.sh
./start.sh