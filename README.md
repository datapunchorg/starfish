# starfish

This is a REST API Gateway to create EMR cluster and submit Spark application.

How to start the server application
---

1. Run `mvn clean package` to build the jar file
2. Edit file `starfish-server/config.yml` to use your own configure values
3. Config your local AWS environment to make sure having permission to do EMR operations
4. Start server with `java -jar starfish-server/target/starfish-server-1.0-SNAPSHOT.jar server starfish-server/config.yml`
5. To check that your server is running, browse url `http://localhost:8080/starfish/v1/`

Health Check
---

To see your server health, browse url `http://localhost:8080/admin/healthcheck`

Request Examples: EMR Cluster
---

## Create EMR Cluster

Install `jq` to parse JSON response from `curl` command:

```
brew install jq

```

Send requests to the server using `curl`:

```
export response=$(curl -X POST localhost:8080/starfish/v1/emr/clusters -H 'Content-Type: application/json' -d '{"emrRelease": "emr-6.5.0"}')
echo $response

export clusterFqid=$(echo $response | jq -r '.clusterFqid')
echo clusterFqid: $clusterFqid

curl localhost:8080/starfish/v1/emr/clusters/$clusterFqid
```

## Delete EMR Cluster

```
curl -X DELETE localhost:8080/starfish/v1/emr/clusters/$clusterFqid
```

Request Examples: Spark
---

## Submit Spark application

```
export response=$(curl -X POST localhost:8080/starfish/v1/emr/clusters/$clusterFqid/spark -H 'Content-Type: application/json' -d '{"mainClass": "org.apache.spark.examples.SparkPi", "mainApplicationFile": "s3a://datapunch-public-01/jars/spark-examples_2.12-3.1.2.jar"}')
echo $response

export submissionId=$(echo $response | jq -r '.submissionId')
echo submissionId: $submissionId

curl localhost:8080/starfish/v1/emr/clusters/$clusterFqid/spark/$submissionId
```

Code Example to Create Cluster and Submit Spark Application Without Using REST
---

See: [Integration Test: EmrSparkControllerIT.java](starfish-emr/src/test/java/org/datapunch/starfish/core/EmrSparkControllerIT.java)
