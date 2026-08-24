# Microservices: OpenFeign + RabbitMQ (CourseHub mini)

A three-service Spring Boot microservices platform demonstrating the two
core inter-service communication patterns: a **synchronous call via
OpenFeign** and an **asynchronous event via RabbitMQ**. Built for a
massively-scalable-applications course module on service-to-service
communication.

## Scenario

A student enrolls in a course section:

1. `enrollment-service` receives the enrollment request and calls
   `course-service` synchronously (OpenFeign) to reserve a seat and get
   the tuition price.
2. Once confirmed, `enrollment-service` publishes an `EnrollmentConfirmed`
   event to RabbitMQ.
3. `notification-service` consumes that event asynchronously and logs a
   (stubbed) confirmation notification — decoupled from the request path.

## Services

| Service                | Port | Role                                                          |
|-------------------------|------|-----------------------------------------------------------------|
| `course-service`        | 8081 | Owns course sections; exposes a reserve-seat endpoint consumed via Feign |
| `enrollment-service`    | 8082 | Creates enrollments, calls `course-service` via `CourseClient` (OpenFeign), publishes `EnrollmentConfirmedEvent` |
| `notification-service`  | 8083 | Listens on the RabbitMQ queue and logs a notification stub |

## Tech stack

- Java 25, Spring Boot 4, Spring Cloud (OpenFeign)
- PostgreSQL (one database per service)
- RabbitMQ (topic exchange / routing key)
- Maven multi-module build
- Docker / Docker Compose

## Key code

- `enrollment-service/.../feign/CourseClient.java` — declarative Feign
  client calling `POST /api/courses/sections/{id}/reserve` on
  `course-service`
- `enrollment-service/.../messaging/EnrollmentPublisher.java` — publishes
  the confirmation event to the RabbitMQ exchange
- `notification-service/.../messaging/EnrollmentConfirmedListener.java` —
  `@RabbitListener` consuming the event
- `*/config/RabbitTopologyConfig.java` — exchange/queue/binding
  declarations, shared topology between publisher and consumer

## Quick start

```bash
docker compose up -d postgres rabbitmq
mvn clean install -DskipTests
docker compose up --build
```

RabbitMQ management UI: http://localhost:15672 (guest / guest).

## Try it

```bash
# reserve/check a section directly on course-service
curl http://localhost:8081/api/courses/sections/1

# create an enrollment (triggers the Feign call + RabbitMQ event)
curl -X POST http://localhost:8082/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"sectionId":1}'

# watch the notification-service logs for the consumed event
```
