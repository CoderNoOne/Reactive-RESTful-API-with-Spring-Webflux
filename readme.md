# Reactive RESTful API with Spring Webflux!

Application developed in **Domain Driven Design** (DDD) pattern on top of reactive streams provided by Spring Webflux dependency with the **Netty** -  an asynchronous event-driven network application framework. The technology stack includes **Mongo** - NoSQL database implementation with reactive driver enabled, so the application flow is ansynchronous with fully non-blocking back pressure.

As mentioned [webflux performance ](https://filia-aleks.medium.com/microservice-performance-battle-spring-mvc-vs-webflux-80d39fd81bf0) there is one thing, I believe, that we should bear in mind:

> Spring Webflux with WebClient and Apache clients wins in all cases. The most significant difference(4 times faster than blocking Servlet) when underlying service is slow(500ms). It 15–20% faster then Non-blocking Servlet with CompetableFuture. Also, it doesn’t create a lot of threads comparing with Servlet(20 vs 220).
Unfortunately, we cannot use WebFlux everywhere, because we need asynchronous drivers/clients for it. Otherwise, we have to create custom thread pools/wrappers.

Reactive approch uses much more less resources (20 vs 220 threads) and also it is faster than non-blocking servlet with CompleteableFuture approach, **BUT**

We should NOT use reactive approach everywhere, as reactive programming **complitates** the code, very complex in such cases like transactional scoped methods, where you are modifying multiple documents. Also you'll have a much tougher time reasoning about the order of execution and debugging your reactive streams.
## Prerequisites:
- all you need is Docker and docker compose with docker swarm mode enabled :)
# Application architecture

The whole application is containerized with **docker compose** technology  - this includes the code, the runtime, the system tools and libraries

Docker files are:
- [ ] docker-compose.yml - used by me mostly for development purposes



- Execute command: `docker-compose up -d --build` to start the containers defined in the file in the   background and leaves them running
- Execute command:  `docker-compose logs -f` to follow logs of all containers participating in a service
- Execute command: `docker-compose down` to stop containers and remove containers, networks, volumes created by `up`command

To speed up development, I decided to not pack FAT JAR into my docker image everytime I recompile the project. Instead, I used **maven-dependency-plugin** to split FAT jar into dependencies and classes packages


- [ ] docker-swarm.yml - used by me to deploy an application on a docker swarm mode
-  Execute command: `docker stack deploy -c docker-swarm.yml <applicationName>` to deploy a cluster of docker containers through docker swarm

- Execute command: `docker stack ps  <applicationName>` to list the tasks in the stack
- Execute command: `docker stack rm <applicationName>` to remove `<applicationName>` stack

## **Architecture of the replica set**

The applications utilizes MongoDB distributed transactions, which adds support for multi-document transactions on sharded clusters and incorporates the existing support for multi-document transactions on replica sets. The application consists of 2 slave (secondary) mongo nodes, and 1 master (primary) node as show below.

![MongoDB replica set](https://docs.mongodb.com/manual/images/replica-set-primary-with-two-secondaries.bakedsvg.svg)


- The primary node receives all write operations
- The secondaries replicate the primary's oplog and apply the operations to their data sets such that the secondaries' data sets reflect the primary's data set. If the primary is unavailable, an eligible secondary will hold an election to elect itself the new primary
- the application is configured to read from the primary, but if it is unavailable, operations read are allowed also from secondary members (read from primary node is set to  prefered to maximize data consistency due to the fact that the data replication between primary and secondaries is asynchronous)

## Architecture for replica set with docker

All replica set nodes are containarized as shown below:

![MongoDB replica set with docker](https://i.imgur.com/eQS28HV.png)

The volumes are attached to every mongo node instance as it is the preferred mechanism for persisting data generated by and used by Docker containers


## OpenApi 3.0 UI

Swagger webflux UI is enabled on: {server_host:8080}/docs e.g. on local machine: localhost:8080/docs 

![MongoDB replica set with docker](https://i.imgur.com/5V8h10G.png)

