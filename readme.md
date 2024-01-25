# Учёт домов и жильцов

## Описание

Web-приложение для учёта домов и жильцов с REST API. Работает с двумя сущностями: House и Person.

### Сущности

#### House

- Поля: id, uuid, area, country, city, street, number, create_date.
- Множество жильцов и владельцев (0-n).
- create_date устанавливается при создании.

#### Person

- Поля: id, uuid, name, surname, sex, passport_series, passport_number, create_date, update_date.
- Обязан проживать в одном доме.
- Может владеть множеством домов.
- Сочетание passport_series и passport_number уникально.
- create_date устанавливается при создании.
- update_date изменяется при изменении информации о Person.

### Операции

- CRUD для House и Person с пагинацией (размер по умолчанию: 15).
- Дополнительные GET операции:
    1. Получить всех Person проживающих в House.
    2. Получить все House, владельцем которых является Person.
    3. Получить всех Person когда-либо проживавших в доме.
    4. Получить всех Person когда-либо владевших домом.
    5. Получить все House, где проживал Person.
    6. Получить все House, которыми когда-либо владел Person.

### Дополнения

- Добавлена миграция.
- Добавлен PATCH для Person и House.
- Добавлена сущность HouseHistory (id, house_id, person_id, date, type).
    - type [OWNER, TENANT].
    - Запись в HouseHistory добавляется при смене места жительства или владельца.
- Реализовано через триггер в БД.
- Добавлены кэши на сервисный слой House и Person.
- Добавлены Integration тесты для проверки работы кэша в многопоточной среде.
Для запуска Integration тестов необхлдимо установить docker и запустить контейнер при помощи команды:
  docker run --name test -e POSTGRES_PASSWORD=test -d postgres:13.3
- Используется testcontainers.
- Добавлен мультипроектный стартер с своим cache-starter через mavenLocal.

## Инструкции по запуску

1. Создайте базу данных postgresql c учетом настроек указанных в application.yml.
2. Запустите приложение project.
3. При запуске приложения Liquibase создаст все необходимые таблицы и заполнит их.

Внимание: Для запуска cache в качестве starter необходимо выполнить следующие пкнкты:
1. В файле [build.gradle](project%2Fbuild.gradle) project раскомментировать строку "implementation 'ru.clevertec.cache:cache:1.0.0'".
2. В файле [build.gradle](project%2Fbuild.gradle) project закомментировать строку "implementation project(':cache')".
3. В файле [settings.gradle](settings.gradle)  закомментировать строку "implementation project(':cache')".
4. В корне проекта cache выполнить комманду "gradle publishToMavenLocal". На локадьной машине должен быть установлен gradle.
5. Запустите приложение project.