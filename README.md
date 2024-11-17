**Запуск проекта**
1. Создать базу данных H2 с типом хранения in memory и заполнить ее sql таблицами из папки
src/main/resources/db/changelog/changeset
2. Создать БД postgres.
3. В терминале Maven выполнить команду mvn clean compile, а затем mvn package
4. Запустить проект в Docker через терминал idea с помощью команды docker-compose up
---
**Прошу обновлять postman тесты при дальнейшей разработке проекта**
Тесты находятся в папке postman