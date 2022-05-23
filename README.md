# Домашняя работа #3

## Задание
Используя Spring Boot доделать веб-приложение по работе с сущностями Person (люди) и Department (отдел).
Реализовать базовые CRUD операции над ними, дописать unit-тесты и задеплоить его на Heroku.
 
**Person**:
* `GET /` – получение подробной информации о всех людях и о том, какому они принадлежат отделу.
* `GET /{id}` – получение подробной информации о человеке. Если информация по id не найдена, то возвращать 404 ошибку.
* `POST /` – создание новой записи о человеке.
* `PATCH /{id}` – обновление информации о человеке. Если информация по id не найдена, то возвращать 404 ошибку.
* `DELETE /{id}` – удаление информации о человеке и удаление его из отдела. Если запись с таким id не найдена, ничего не делать.

**Department**:
* `GET /` – получение краткой информации о всех департаментах.
* `GET /{id}` – получение подробной информации о департаменте и краткой информации о людях в нем. Если информация по id не найдена, то возвращать 404 ошибку.
* `POST /` – создание нового департамента.
* `PATCH /{id}` – обновление данных о департаменте. Если информация по id не найдена, то возвращать 404 ошибку.
* `DELETE /{id}` – удаление всех людей из департамента и удаление самого департамента. Если запись с таким id не найдена, ничего не делать.
* `POST /{departmentId}/{personId}` – добавление нового человека в департамент. Если не найден человек или департамент, отдавать 404 ошибку.
Если департамент закрыт, то отдавать 409 ошибку.
* `DELETE /{departmentId}/{personId}` – удаление человека из департамента. Если департамент не найден, отдавать 404 ошибку, если не найден человек в департаменте, то ничего не делать.
* `POST /{id}/close` – удаление всех людей из департамента и установка отметки на департаменте, что он закрыт для добавления новых людей. Если информация по id не найдена, то возвращать 404 ошибку.

Сущность _Person_ имеет поля:
```postgresql
CREATE TABLE person
(
    id          INTEGER NOT NULL CONSTRAINT person_pkey PRIMARY KEY,
    first_name  VARCHAR(80) NOT NULL,
    last_name   VARCHAR(80) NOT NULL,
    middle_name VARCHAR(80),
    age         INTEGER
);
```

Сущность _Department_ имеет поля:
```postgresql
CREATE TABLE department
(
    id     INTEGER NOT NULL CONSTRAINT department_pkey PRIMARY KEY,
    name   VARCHAR(80) NOT NULL CONSTRAINT idx_department_name UNIQUE,
    closed BOOLEAN DEFAULT FALSE NOT NULL
);
```

Требуется добавить дополнительные поля к этим сущностям и отдавать их в API.

## Требования
1. Дописать бизнес-функционал и unit-тесты.
1. Создание схемы базы данных сделать через скрипты миграции flyway.
1. Для работы с базой данных использовать Hibernate (т.е. EntityManager). Использовать JPA нельзя.
1. Используя Jacoco (code coverage), сгенерировать отчет о покрытии и задеплоить его на [https://codecov.io/](https://codecov.io/).
1. Задеплоить приложение на Heroku.
1. В GitHub Actions проверить что сборка и прогон интеграционных тестов завершились успешно. 

## Сборка приложения 
```shell script
# запустить PostgreSQL в docker-контейнере
docker compose up -d postgres

# загружает gradle wrapper 6.8
./gradlew wrapper

# сборка проекта, прогон unit-тестов, локальный запуск приложения (по-умолчанию профиль local)
./gradlew clean build bootRun 
```

##  Комментарии
1. Все токены нужно прописывать в открытом виде без SECRETS, потому что при создании Pull Request SECRETS недоступны, а значит тесты будут падать.
1. Для настройки Codecov нужно:
    1. авторизоваться на [https://codecov.io/](https://codecov.io/) через GitHub, выбрать нужный репозиторий;
    1. в .github/workflows/main.yml явно прописать свой токен;
    1. после прогона тестов, данные по покрытию отображаются странице проекта `https://codecov.io/gh/<github-user>/<repository-name>`.
1. По описанию методов генерируется OpenAPI спецификация, описание в json доступно по адресу http://localhost:8080/swagger-ui.html.
1. Описание работы Heroku [https://devcenter.heroku.com/articles/how-heroku-works](https://devcenter.heroku.com/articles/how-heroku-works).
1. Для деплоя на Heroku использовать docker, Dockerfile находится в корне проекта. Для деплоя на heroku требуется
   явно прописать `API_KEY` в .github/workflows/main.yml.
1. Для подключения БД на Heroku заходите через Dashboard в раздел Resources и в блоке Add-ons ищете Heroku Postgres. 
   Для получения адреса, пользователя и пароля переходите в саму БД и выбираете раздел Settings -> Database Credentials.
   В блоке settings -> config vars создаете переменные `DATABASE_HOST`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER`, `DATABASE_PASSWORD`.
   Там же надо прописать `SPRING_PROFILES_ACTIVE` heroku, чтобы при запуске использовался профиль heroku.
1. Unit-тесты на Spring:
    * [Web Layer tests](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-mvc-tests)
    * [Service tests](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications)
    * [Database tests](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test)
1. После успешного деплоя на Heroku, через newman запускаются интеграционные тесты. Интеграционные тесты можно проверить локально, для этого 
   нужно импортировать в Postman коллекцию [homework3.postman_collection.json](postman/homework3.postman_collection.json) и
   environment [[local] digital-habits.postman_environment.json](postman/%5Blocal%5D%20digital-habits.postman_environment.json).

