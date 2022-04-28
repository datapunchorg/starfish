# starfish

How to start the starfish application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/starfish-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Request Examples
---

```
curl -X POST localhost:8080/starfish/v1/emr/clusters -H 'Content-Type: application/json' -d '{"emrRelease": "emr-6.5.0"}'

curl localhost:8080/starfish/v1/emr/clusters/j-1W87FUAZ35XO9

curl -X DELETE localhost:8080/starfish/v1/emr/clusters/j-1W87FUAZ35XO9


```