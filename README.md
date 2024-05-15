# Military District (Backend)

<p align="center">
   <a href="https://github.com/ptrvsrg/military-district-backend/graphs/contributors">
        <img alt="GitHub contributors" src="https://img.shields.io/github/contributors/ptrvsrg/military-district-backend?style=flat&label=Contributors&labelColor=222222&color=77D4FC"/>
   </a>
   <a href="https://github.com/ptrvsrg/military-district-backend/forks">
        <img alt="GitHub forks" src="https://img.shields.io/github/forks/ptrvsrg/military-district-backend?style=flat&label=Forks&labelColor=222222&color=77D4FC"/>
   </a>
   <a href="https://github.com/ptrvsrg/military-district-backend/stargazers">
        <img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/ptrvsrg/military-district-backend?style=flat&label=Stars&labelColor=222222&color=77D4FC"/>
   </a>
   <a href="https://github.com/ptrvsrg/military-district-backend/issues">
        <img alt="GitHub issues" src="https://img.shields.io/github/issues/ptrvsrg/military-district-backend?style=flat&label=Issues&labelColor=222222&color=77D4FC"/>
   </a>
   <a href="https://github.com/ptrvsrg/military-district-backend/pulls">
        <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr/ptrvsrg/military-district-backend?style=flat&label=Pull%20Requests&labelColor=222222&color=77D4FC"/>
   </a>
</p>

Military District - информационная система военного округа. Данная система содержит данные о дислокации военных частей,
воинской и офицерской структуре, воинских формированиях, военной технике и оружии.

## Технологии

+ Java 17
+ Spring Boot 3
+ Apollo Federation 2.3.5
+ Keycloak 24.0
+ PostgreSQL 16
+ Liquibase 4.27
+ Nginx 3.18
+ Prometheus v2.51.1
+ Grafana 10.3.4
+ PGAdmin4 8.5

## Архитектура

<img alt="GitHub pull requests" src="./assets/Архитектура.png"/>

Для базовых CRUD-операций реализованы 5 микросервисов-подграфов при помощи Apollo Federation. Роутер Apollo Router
используется для разложения Graphql-запросов и проксирования.

Микросервис отчетов отвечает за сборку данных сложных параметризованных SQL-запросов.

Доступ к микросервисам осуществляется через API шлюз.

Keycloak выступает в роли SSO-сервера и сервера авторизации в протоколе OAuth2.0. Он необходим для хранения информации о
пользователях, их правах в системе, а также за выдачу и валидацию разного рода токенов.

Prometheus и Grafana используются для получения и отображения метрик API шлюза, роутера, SSO-сервера и микросервисов.

PGAdmin4 позволяет следить за метриками кластера PostgreSQL, а также просматривать и модифицировать БД.

## Доступные команды

```shell
make clean                                      # Clean generated files
make build                                      # Build the JAR files
make build-images IMAGE_PREFIX=<image prefix>   # Build the Docker images
make supergraph                                 # Generate supergraph schema
make dev-env                                    # Create template .env.development file
make env                                        # Create template .env file
make dev-up                                     # Deploy to Docker for Development
make dev-down                                   # Stop and remove containers for Development
make up                                         # Deploy to Docker for Production
make down                                       # Stop and remove containers for Production
make help                                       # Display this message
```

## Запуск

1) Выполните команду `make env` и запомните значения переменных среды в сгенерированном файле `.env` (Опционально,
   в `docker-compose.yml` установлены значения по умолчанию)
2) Выполните команду `make up` и дождитесь загрузки необходимых образов, запуска контейнеров и окончания проверок
   здоровья.

## Вклад в проект

Если вы хотите внести свой вклад в проект, вы можете следовать этим шагам:

1. Создайте форк этого репозитория.
2. Внесите необходимые изменения.
3. Создайте pull request, описывая ваши изменения.