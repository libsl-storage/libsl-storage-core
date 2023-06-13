# Порядок запуска:

- Выбрать один из настроенных профилей (сейчас это `dev`);
- Запустить через `bootRun --args='--spring.profiles.active=<list of profiles>'`...
- ...Или собрать jar'ник через `bootJar`

Альтернативно:
`./gradlew bootRun -P<profile>`

# Конфиги:

Конфиги профилей запуска находятся в `src/main/resources/application.yaml`
