1. mvn clean package  to create jar
1. copy scalaz core and scalaz concurrent jar to spark/jars folder
2. To run on hadoop yarn:

./bin/spark-submit --class com.multiapps.Demo --master yarn --deploy-mode cluster --num-executors 2 --driver-memory 2g --executor-cores 1 multiapps-1.0-SNAPSHOT.jar 3 "/test/tempdata"

For akka run on yarn:

./bin/spark-submit --master yarn --deploy-mode client --conf spark.driver.extraClassPath="/shared/akka-actor_2.11-2.4.8.jar:/shared/config-1.3.0.jar" --class com.multiapps.Demo multiapps-1.0-SNAPSHOT.jar 2 /data/finalest_merge

2 = number of parallel queries that need to be executed
/data/finalest_merge = input data path
