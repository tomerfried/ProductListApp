How to run the application all together:

- place folders tomer-fried-products-service and tomer-fried-products-ui-service
under the same folder (make sure these are their names)
- run docker
- run from tomer-fried-products-service folder a cmd command: "docker compose up"
- Once all containers were made and running, run in the browser: http://localhost:4200/
- If dummy products were not loaded, run "docker-compose down -v" and "docker compose up" again

Unit Tests:
Can be ran using an IDE, I have run them by opening the project in Intellij IDEA
