# === Builder stage ===
FROM gradle:8.14.3-jdk21 AS builder
WORKDIR /application

# Кэшируем зависимости Gradle отдельно
COPY build.gradle settings.gradle.kts ./
COPY gradle ./gradle
RUN gradle clean build -x test --no-daemon || true  # предварительная сборка для кэша зависимостей

# Копируем весь исходный код и билдим
COPY . .
RUN gradle clean build -x test --no-daemon

# === Layers extraction stage ===
FROM bellsoft/liberica-openjre-debian:21.0.6 AS layers
WORKDIR /application
COPY --from=builder /application/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# === Final runtime stage ===
FROM bellsoft/liberica-openjre-debian:21.0.6
VOLUME /tmp
RUN useradd -ms /bin/bash spring-user
USER spring-user

WORKDIR /application
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
