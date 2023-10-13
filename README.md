# Friend Recommendation System [WIP]

### Overview 
Facebook is a social media platform that connects people who may know each other. By leveraging advanced algorithms and user data analysis, the system identifies common interests, mutual connections, and relevant activities to propose individuals who are likely to share meaningful connections.

### General System Requirements
1. Users should have the ability to send, cancel, accept, reject and delete friend requests 
2. The system should be able to suggest potential friends based on mutual connections. 
3. Users should be able to provide basic information.
4. Users should only receive newly updated lists when new users are added or updates are made, rather than with every reload. 

**Supporting Repositories**
- https://github.com/philipdaquin/friend-recommendation-provision
- https://github.com/philipdaquin/friend-recommendation-infra





## Final Architecture
![FinalArchitecture](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/80b35dae-8159-4cb6-a10f-350019f5d2be)

**Main Uses:**
- **Add** / **Update** / **Delete** User profile
- **Send** / **Accept** / **Reject** / **Delete** Friend Request
- **Find Mutual Friends** / **Recommend Friends** Recommendations

**Users and Roles**
- **Member** — All members can send each other friend request, cancel request, create their own profile etc.
- **Admin** — responsible mainly for adding, deleting users
- **System** — responsible mainly for suggesting users

**Scaling Goals**
1. **Highly Available / Reliable** — The system should be able to withstand peak hours but occasional data point loss is acceptable  
2. **Scalable** — The system should accommodate large volumes of user
3. **Privacy** — The recommendations should be private to the user

**Performance Goals**
1. **Highly Performant** — The system should be able to send near real time recommended users
2. **Eventual Consistency** — few second delays for retrieving new set of list

## Higher Level Design
### Back of the envelope estimation

Determine the potential scale and challenges our solution will need to address. Some constraints and assumptions are list below:

**Constraints:** 

- Users are suggested with new friends based on their mutual friends and other similar aspects from other users
- There’s typically a short interval delay when fetching a new list of recommended friends

********Assumptions:********

- Users have a maximum friend count of 5000
- Users typically add 10 new friends per week
- On average, ten million users are on the platform every day
- The number of concurrent users is 10% of DAY, so the number of concurrent users is One million

******Calculate QPS******

Calculate the number of new friends per second 

$$
10 New Friends / 24 * 60 * 60 * 7  = 0.016 Per SecondPer User
$$

Calculate the total generate list per user

- Assuming we generate 1 per day

$$
Total Generated = Number of Users * Queries per User
$$

$$
1 Query / 24  * 60 * 60 = 1.16*10^5 Queries per Second per user 
$$

Calculate the total queries per second 

$$
Total Queries per second = Total Generated + New Friends Added
$$

$$
116,000 Generated Queries + 16,000 Queries PerSecond = 132, 000 Queries Per Second
$$

QPS of 132,000 is relatively low compared to some high traffic system. 

The QPS calculated is based on the assumptions of user’s behaviours and the actions that occur in the system

### Architecture Services

- User Service
- Recommendation Service
- Friend Service
- API Gateway
- Discovery Service
- Load Balancer

### Choosing the right database
Given to what we know about our system:

- The raw data is relational
- The average QPS is ~130,000, so the system is write-heavy.

We can use different type of databases here: 

- Relational would be optimal for storing User Information. The other options would be to use No SQL databases like Cassandra or Mongo DB.
- Mongo DB would be ideal for storing the Friends Service since transactions are not necessarily needed in this service.
- Neo4J would be optimal for this type of QPS to support Recommendation Service. As the QPS goes higher, we may be able to use more robust and proven tools like HDFS and Spark.

## Final Architecture
![FinalArchitecture](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/80b35dae-8159-4cb6-a10f-350019f5d2be)

### Services 
| Service Name | Ports | Persistence | Messaging | Caching | Service Type |
| --- | --- | --- | --- | --- | --- |
| User | 7000 | R2DBC | Apache Kafka | Redis | Service  |
| Friend | 7001 | Mongo Db  | Apache Kafka | N/A | Service |
| Recommendation | 7002 | Neo 4j | Apache Kafka | N/A | Aggregation |
| Gateway | 8090 | N/A | N/A | N/A | Cloud Gateway |
| Discovery | 8761 | N/A | N/A | N/A | Eureka Service |
| Config  | 8088 | N/A | N/A | N/A | N/A |


### CQRS Design pattern 
CQRS stands for Command and Query Responsibility Segregation, a pattern that separates the read and write operations for a common datastore. 
The Recommendation Service reads every Event that takes place in both User and Friend Domains.
- By using the CQRS, it will allow us to minimise data contention at the domain level, integrate our Recommendation Service more efficiently and support high traffic request during peak periods.
- By reading the stream of events rather than the actual data, we can avoid data conflicts and maximise both performance and scalability. The events can be used to asynchronously generate materialised views on the data that can be used to populate our read store.
![cqrs](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/3528001a-277d-4a40-9e66-8837ee52d6e2)

**Pros**:
- Support different types of data schemas
- Independent scaling solutions
- More performant
- Separation of concerns

**Cons**: 
- Data contention between two services — when the system goes down, it is vulnerable to duplicated events, and errors in the messaging queue.
- Added infrastructure complexity — because the system attempts to perform dual writes, added Event-Sourcing patterns can make it difficult and requires a different approach to designing systems.
- Generating materialised views for the read model can require significant of time to reflect on its persistence
- Eventual consistency — there will be some delay between the event being generated and the data being updated



## How it works 
### Sequential Diagram 
******************************Adding Friends****************************** 
1. Check if they’re already friends
2. Execute a dual write to database and Kafka cluster. Send an event called `Add-Friend` Command to the Friend Service and wait for Kafka acknowledgement.
3. The aggregated service listens to this event and replicates the domain data on the graph database.
4. If on error, rollback the database transaction.
![addongfroedm ](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/996ca6e5-ca1b-456c-a7a2-7df26449caed)

******************************Recommending Friends****************************** 
![Recommendation](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/76e3c470-8614-4a47-b109-a28cbb08de1b)
Using Neo4J real time queries, it is possible to return recommendations to users without extensive machine learning.
1. Find mutual frineds of your frineds' list.
2. Remove the user's friends from the list
3. Provide a list of individuals who are not directly friends
4. Rank by the most number of mutual friends 

## API Design 
### Use Case Diagram 
![useractivty](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/725faeff-ae04-4cf4-834a-5e2183aac55b)

**************************User Service************************** 

- Create User / Update User / Get User / Delete User
- Block User

| API | Detail |
| --- | --- |
| GET /v1/users/{user:_id} | Get User |
| POST /v1/users | Create new User |
| PUT /v1/users/{user:_id} | Update User |
| DELETE /v1/users/{user:_id} | Delete user |

********Recommendation Service******** 

- Get Mutual Friends
- Recommend Friends

| API | Detail |
| --- | --- |
| GET /v1/users/{user:_id}/command/find-mutual-friends | Return a Page of Users (Webflux)  |
| GET /v1/users/{user:_id}/command/recommend-friends | Return a Page of Users (Webflux) |

******************************Friend Service****************************** 

- Send / Cancel / Reject Friend Request
- Get Friend
- Add Friend
- Update Friend
- Get all Friends
- Remove Friends

| API | Detail |
| --- | --- |
| GET /v1/users/{user:_id}/friends | Find user’s friends by their id. Return a List of Friends. |
| GET /v1/friends/{user:_id} | Find Friend by its own Id |
| POST /v1/users/{user:_id}/command/add-friend | Adds a friend. On the side note, a dual write is necessary on both Friends and User services. If an Error occurs, the transaction must be rolled back  |
| POST /v1/users/{user:_id}/command/remove-friend | Remove a friend.  On the side note, a dual write is necessary on both Friends and User services. If an Error occurs, the transaction must be rolled back  |
| POST /v1/users/{user:_id}/request/{requestId}/command/reject-request | Rejects friend request.  |
| POST /v1/users/{user:_id}/request/{requestId}/command/accept-request | Accept friend request. |
| POST /v1/users/{user:_id}/requests | Create Friend Request  |
| GET /users/{id}/friend/from/request | Get All Friend Requests by the User |
| GET /users/{id}/friend/to/request | Get All Friend Request For the User |
| DELETE /v1/users/{user:_id}/requests/{id} | Delete a Friend Request  |
|  |  |

## Class-relations Diagram 
![diagram](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/b79a8998-77a5-491c-a771-4bf7212ff6ee)


## Centralised Monitoring, Alerting and Logging System 
### Architecture
The main objectives for a monitoring system:
- Monitor the application’s performance
- Assist in performing quick root cause analysis
- Provide self service to stakeholders

Monitoring is mainly comprised of the following four sets of activities:
- Instrumentation of the applications
- Metrics collections
- Metrics visualisation
- Alerts and notifications
![moniyoring](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/6a0b4adf-1174-4c37-87bc-51a369e06b2b)
### Observability services: 
- **Application Observability** - Micrometer
- **Distributed Tracing** — Tempo, Zipkin
- **Telemetry —** Open Telemetry
- **Health Check API  —** Spring Cloud Actuato
- **Real time metrics  —** Grafana
- **Monitoring System  —** Prometheus
- **Log** **aggregation** — Loki, Promtail
- **Alert System**: Alert Manager
- **Container Observability**: cAdvisor

![kubernetesMonitoring](https://github.com/philipdaquin/friend-recommendation-system/assets/85416532/0ffa5424-e415-446e-aaff-bdac9ff85df2)
### Kubernetes Observabilty:
The kubernetes monitoring services are provided by Prometheus Operator. You can find more information
[here](https://github.com/prometheus-operator/kube-prometheus/tree/main):

The services deployed in this application are: 
- Node Exporter
- Kube State Metrics
- ThanosRuler
- Prometheus Operator
- Alert Manager
- Prometheus Adapter
- Thanos [Optional]


