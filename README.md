# Docker Manager

A lightweight project for managing Docker resources from a single place.

## Overview

Docker Manager is a Spring Boot REST API that talks to the Docker daemon directly (no `docker` CLI required by the API itself). It currently supports:

- Listing running and stopped containers
- Creating containers from an image
- Starting, stopping, and removing containers
- Listing, filtering, and searching images
- Pulling images asynchronously with progress tracking
- Removing images and listing containers that use a given image

## Requirements

- Java 21
- Maven
- Docker Engine or Docker Desktop
- Permission to access the Docker daemon (the API connects over the Docker socket, `unix:///var/run/docker.sock` by default)

## Getting started

1. Clone the repository:

    ```bash
    git clone https://github.com/ar35-rjm/docker-manager-api.git
    cd docker-manager-api
    ```

2. Start Docker Engine or Docker Desktop.

3. Run the Docker Manager API:

    ```bash
    cd backend
    mvn spring-boot:run
    ```

### Configuration

Useful properties in `backend/src/main/resources/application.properties`:

```properties
spring.application.name=docker-manager
# Docker daemon address (Linux/macOS unix socket shown; on Windows use
# npipe:////./pipe/docker_engine)
docker.socket.path=unix:///var/run/docker.sock
```

## Usage

Typical workflows include:

- Inspecting all containers (`?all=true`) or only running ones (`?all=false`)
- Pulling an image in the background and polling its status until complete
- Removing an image, with an optional `force` flag for images still in use
- Searching Docker Hub for images before pulling them

## API endpoints

The API listens on `http://localhost:8080` by default. The base path is `/api/v1`.

### Containers

| Method | Endpoint | Usage |
| --- | --- | --- |
| `GET` | `/api/v1/container?all=true` | List containers (`all=false` for running only) |
| `POST` | `/api/v1/container?imageName=<image>` | Create a container from an image |
| `POST` | `/api/v1/container/{id}/start` | Start a container |
| `POST` | `/api/v1/container/{id}/stop` | Stop a container |
| `POST` | `/api/v1/container/{id}` | Remove a container |

### Images

| Method | Endpoint | Usage |
| --- | --- | --- |
| `GET` | `/api/v1/images` | List local images |
| `GET` | `/api/v1/images/filter?imageName=<name>` | Filter local images by name |
| `GET` | `/api/v1/images/search?term=<term>` | Search Docker Hub |
| `POST` | `/api/v1/images/pull?repository=<repo>&tag=<tag>` | Start an image pull (returns `202 Accepted` immediately) |
| `GET` | `/api/v1/images/pull-status?repository=<repo>&tag=<tag>` | Poll pull progress (`PULLING`, `COMPLETED`, `FAILED`) |
| `DELETE` | `/api/v1/images/{id}?force=false` | Remove an image (`force=true` to override images in use) |
| `GET` | `/api/v1/images/{id}/containers` | List containers created from an image |

### Examples

List containers:

```bash
curl http://localhost:8080/api/v1/container?all=true
```

Pull `alpine:3.20` in the background:

```bash
curl -X POST "http://localhost:8080/api/v1/images/pull?repository=alpine&tag=3.20"
```

Poll the pull status:

```bash
curl "http://localhost:8080/api/v1/images/pull-status?repository=alpine&tag=3.20"
```

Example response:

```json
{
  "repository": "alpine",
  "tag": "3.20",
  "startedAt": "2026-10-20T10:15:30Z",
  "state": "COMPLETED",
  "layersDone": 3,
  "layersTotal": 3,
  "lastMessage": "Downloaded newer image",
  "error": null,
  "finishedAt": "2026-10-20T10:15:35Z"
}
```

Container lifecycle:

```bash
curl -X POST http://localhost:8080/api/v1/container/<container-id>/start
curl -X POST http://localhost:8080/api/v1/container/<container-id>/stop
curl -X POST http://localhost:8080/api/v1/container/<container-id>
```

### Error responses

Docker daemon errors are mapped to meaningful HTTP status codes by a global exception handler:

| Status | Cause |
| --- | --- |
| `400` | Invalid request input |
| `404` | Container/image not found, or no pull recorded for that image |
| `409` | Conflict, e.g. removing an image still used by a container |
| `500` | Any other Docker daemon error |

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "..."
}
```

## Project structure

```
backend/src/main/java/com/rogerjosemaria/docker_manager/
├── config/          # Docker client and pull executor beans
├── controllers/     # REST controllers and exception handling
├── models/          # API models (e.g. PullStatus)
└── services/        # Docker container and image services
```

## Development

Contributions are welcome. Create a branch for your changes, keep updates focused, and verify Docker-related behavior locally before opening a pull request.

Build and test:

```bash
cd backend
mvn test
mvn package
```

## Troubleshooting

- Confirm the daemon is reachable with `docker info`.
- Check that your user can access the Docker socket (e.g. add your user to the `docker` group on Linux).
- On Windows, set `docker.socket.path=npipe:////./pipe/docker_engine`.
- Review the API logs and, for container-specific issues, `docker logs <container>`.

## License

No license has been specified yet.
