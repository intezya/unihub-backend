# === Builder stage ===
FROM gradle:8.14.3-jdk21 AS builder
WORKDIR /app

# Кэшируем зависимости Gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
# Сборка зависимостей без исходников и без bootJar
RUN gradle build -x test -x bootJar --no-daemon

# Копируем исходный код и собираем приложение
COPY . .
RUN gradle clean bootJar -x test --no-daemon

# === Layers extraction stage ===
FROM bellsoft/liberica-openjre-debian:21.0.6 AS layers
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# === Final runtime stage ===
FROM bellsoft/liberica-openjre-debian:21.0.6
WORKDIR /app

# Пользователь для безопасности
RUN useradd -ms /bin/bash spring-user
USER spring-user

# Копируем только слои, которые нужны для runtime
COPY --from=layers /app/dependencies/ ./
COPY --from=layers /app/spring-boot-loader/ ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/ ./

VOLUME /tmp
EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
