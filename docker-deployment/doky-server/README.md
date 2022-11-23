### Run DEV profile

Compile jar file with command (from server directory)

    ./gradlew bootJar

Copy compiled jar (with all dependencies) from `server/build/libs/server-*.jar` to current folder and name
it `server.jar`

Run follow command:

    docker compose -f ./dev.yml up -d
