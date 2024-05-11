# Используем официальный образ OpenJDK для сборки приложения
FROM gradle:latest AS build

# Копируем исходный код в контейнер
COPY ./ /home/gradle/src
WORKDIR /home/gradle/src

# Собираем приложение с помощью Gradle
RUN gradle build --no-daemon

# Отдельный этап сборки для уменьшения размера образа
FROM openjdk:17-jdk-alpine

# Копируем собранный JAR-файл из предыдущего этапа в контейнер
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

# Указываем порт, который будет прослушивать приложение
EXPOSE 8080

# Команда для запуска приложения при старте контейнера
CMD ["java", "-jar", "/app/app.jar"]
