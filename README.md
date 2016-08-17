1. mvn clean package  to create jar
1. copy scalaz core and scalaz concurrent jar to spark/jars folder
2. To run on hadoop yarn:

./bin/spark-submit --class com.multiapps.Demo --master yarn --deploy-mode cluster --num-executors 2 --driver-memory 2g --executor-cores 1 multiapps-1.0-SNAPSHOT.jar 3 "/test/tempdata"
