### Run DEV profile

Compile jar with all dependencies file with command (from server directory)

Create-Docker-image

    ./gradlew bootJar

Copy compiled jar (with all dependencies) from `server/build/libs/server-*.jar` to current folder and name
it `server.jar`

Run follow command:

    docker compose -f ./dev.yml up -d
