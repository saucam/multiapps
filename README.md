1. mvn clean package  to create jar
1. copy scalaz core and scalaz concurrent jar to spark/jars folder
2. To run on hadoop yarn:

./bin/spark-submit --class com.multiapps.Demo --master yarn --deploy-mode cluster --num-executors 2 --driver-memory 2g --executor-cores 1 multiapps-1.0-SNAPSHOT.jar 3 "/test/tempdata"

For akka run on yarn:

./bin/spark-submit --master yarn --deploy-mode client --conf spark.driver.extraClassPath="/shared/akka-actor_2.11-2.4.8.jar:/shared/config-1.3.0.jar" --class com.multiapps.Demo multiapps-1.0-SNAPSHOT.jar 2 /data/finalest_merge

2 = number of parallel queries that need to be executed
/data/finalest_merge = input data path

./bin/spark-submit --master yarn --deploy-mode client --conf spark.driver.extraClassPath="/shared/akka-actor_2.11-2.4.8.jar:/shared/config-1.3.0.jar:/shared/spray-can_2.11-1.3.3.jar:/shared/spray-http_2.11-1.3.3.jar:/shared/spray-routing-shapeless2_2.11-1.3.3.jar:/shared/spray-util_2.11-1.3.3.jar:/shared/spray-io_2.11-1.3.3.jar:/shared/parboiled-scala_2.11-1.1.7.jar:/shared/parboiled-core-1.1.7.jar:/shared/shapeless_2.11-2.1.0.jar:/shared/spray-httpx_2.11-1.3.3.jar" --class com.multiapps.Demo /shared/multiapps-1.0-SNAPSHOT.jar 8093 /data/small_data

curl --data "select distinct (unique_id) from multiappsTable where state_name IN ('Confirm payment')" 127.0.0.1:8093/api/sql
curl --data "select distinct (unique_id) from multiappsTable where state_name IN ('Confirm payment')" 127.0.0.1:8093/api/multsql
