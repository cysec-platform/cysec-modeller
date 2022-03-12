# CYSEC Modeller

This application can be used to visualize and explore the *Cybersecurity Self-assessment Model for Micro and Small Businesses*.
It allows navigating between the dependencies and external sources.

## Usage

1. Acquire Excel file of the model.
2. Run modeller in the directory of the model
   * ... directly as JAR while passing the location of the Excel model file using the `app.model-file` parameter:
     ```bash
     java -jar cysec-modeller-app-*.jar --app.model-file="msesec-model.xlsx"
     ```
   * ... as Docker container with the model file mounted as a volume:
     ```bash
     docker run --rm -p 8080:8080 \
         -v "$(pwd)/msesec-model.xlsx:/application/data/model.xlsx:ro" \
         -e "app.model-file=/application/data/model.xlsx" \
         ghcr.io/cysec-platform/cysec-modeller
     ```

4. Visit `localhost:8080`


## Build

*JAR including frontend:*
```
mvn clean package
```

*Docker image:*
```
mvn clean package -P build-docker-image
```

## License
This project is licensed under the Apache 2.0 license, see [LICENSE](LICENSE).
