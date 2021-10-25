# code-with-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Related guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and JPA
- RESTEasy JSON-B ([guide](https://quarkus.io/guides/rest-json)): JSON-B serialization support for RESTEasy
- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

# Run example on container

## Docker

Install and run Docker from your local machine and launch from project root:

```shell script
./mvnw clean package -Dquarkus.container-image.build=true -DskipTests=true
```

Then start containers with:

```shell script
docker compose -f src/main/docker/docker-compose.yaml up
```

If everything goes well, run 'docker ps' and you should see at least the 3 containers running:

```
adminer - a postgres dashboard)
postgres - the db host
<user>/quarkus-postgres-example:1.0.0-SNAPSHOT - the app who connect to postgres 
```

Open your browser with the following address:

```
http://localhost:8081
```

Connect on DB using the following parameters:

```
username: postgres
db: host-db
password: example
database: postgres
```

if successfully connected, you should see a table 'company' already created, thanks to the inits script below on **docker-compose.yaml**

```shell script
volumes:
- ./sql:/docker-entrypoint-initdb.d/ # *.sql *.sh files under this folder are automatically executed at startup
```

You can try to insert an item through host-app quarkus web application using *curl* command:

```shell script
curl --location --request POST 'http://localhost:8080/companies' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Starred SPA"
}'
```

The expected output would be like:

```
{"id":1,"name":"Starred SPA"}
```

## Install as a service on Kubernates with minikube

Run minikube

Now, for every rebuild of the app package as a docker image you must indicate the docker daemon inside minikube instance, otherwise minikube will not retrieve latest docker image built from local.


Run:

```shell script
eval $(minikube docker-env)
```

Download the command kompose to convert docker compose yaml files to ku and run:

```shell script
kompose convert -f src/main/docker/docker-compose.yaml -o src/main/docker/kompose-out --volumes hostPath
```

Output would be something like:

```
INFO Kubernetes file "src/main/docker/kompose-out/adminer-service.yaml" created
INFO Kubernetes file "src/main/docker/kompose-out/host-app-service.yaml" created
INFO Kubernetes file "src/main/docker/kompose-out/host-db-service.yaml" created
INFO Kubernetes file "src/main/docker/kompose-out/adminer-deployment.yaml" created
INFO Kubernetes file "src/main/docker/kompose-out/host-app-deployment.yaml" created
INFO Kubernetes file "src/main/docker/kompose-out/host-db-deployment.yaml" created 
```

Create kubernates resources with:

```shell script
kubectl apply -f src/main/docker/kompose-out/
```

The following output should be displayed:

```
deployment.apps/adminer created
service/adminer configured
deployment.apps/host-app created
service/host-app configured
persistentvolumeclaim/host-db-claim0 created
deployment.apps/host-db created
service/host-db configured
```

Check deployments and pods status with:

```shell script
kubectl get deployments
kubectl get po
```

If pods are correctly installed, you should see something like:


```
NAME                        READY   STATUS    RESTARTS   AGE
adminer-7554f9cbdc-s4f9s    1/1     Running   0          5m25s
host-app-7f9d5db669-ch4w5   1/1     Running   0          5m25s
host-db-846b7b5ccc-vwcd9    1/1     Running   0          5m25s
```

You can launch adminer from a random free port with the command:

```shell script
minikube service adminer
```

It is possible to import *script.sql* file directly from dashboard

**NOTE**

#### Volume mount issue

As the kompose version 1.22, volume mount is not supported.

In order to run *host-app* pod as a network service and export at port 8080, run:

```shell script
kubectl port-forward service/host-app 8080:8080
```

#### Refresh image

In order to refresh docker image, remove services, pod and deployments and then re-apply kubernetes config gile.

### Hot deploy

It is also possible to run quarkus with remote dev mode so any update will be hot swapped and seen immediately.

With application already running on container, run this:

```shell script
mvn quarkus:remove-dev
```

