 # Docker Manager

 A lightweight project for managing Docker resources from a single place.

 ## Overview

 Docker Manager is intended to simplify common Docker workflows, such as viewing and managing containers, images, networks, and volumes. It provides a convenient foundation for working with Docker without repeatedly typing low-level commands.

 ## Requirements

 - Java 21
 - Maven project manager
 - Docker Engine or Docker Desktop
 - Docker CLI available on your `PATH`
 - Permission to access the Docker daemon

 ## Getting started

 1. Clone the repository:

	 ```bash
	 git clone https://github.com/ar35-rjm/docker-manager-api.git
	 cd docker-manager
	 ```

 2. Start Docker Engine or Docker Desktop.

 3. Run the Docker Manager:

	```bash
	 mvn spring-boot:run
	```

 ## Usage

 Use Docker Manager to inspect and manage Docker resources. Typical workflows include:

 - Listing running and stopped containers
 - Starting, stopping, and removing containers
 - Viewing and cleaning up images
 - Managing Docker networks and volumes
 - Monitoring resource usage and container status

 ## API endpoints

 Replace `http://localhost:8080` with the address where Docker Manager is running.

 | Method | Endpoint | Usage |
 | --- | --- | --- |
 | `GET` | `/api/containers` | List containers |
 | `GET` | `/api/containers/:id` | View a container |
 | `POST` | `/api/containers/:id/start` | Start a container |
 | `POST` | `/api/containers/:id/stop` | Stop a container |
 | `DELETE` | `/api/containers/:id` | Remove a container |
 | `GET` | `/api/images` | List images |
 | `GET` | `/api/networks` | List networks |
 | `GET` | `/api/volumes` | List volumes |

 Example requests:

 ```bash
 curl http://localhost:3000/api/containers
 curl -X POST http://localhost:3000/api/containers/<container-id>/start
 curl -X POST http://localhost:3000/api/containers/<container-id>/stop
 curl -X DELETE http://localhost:3000/api/containers/<container-id>
 ```

 ## Development

 Contributions are welcome. Create a branch for your changes, keep updates focused, and verify Docker-related behavior locally before opening a pull request.

 ## Troubleshooting

 - Confirm that Docker is running with `docker info`.
 - Check that your user can access the Docker socket.
 - Review container output with `docker logs <container>`.

 ## License

 No license has been specified yet.
