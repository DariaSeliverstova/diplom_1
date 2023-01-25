# Процедура запуска авто-тестов

1. Открыть проект в IntelliJ IDEA.
2. Запустить Docker через команду docker-compose up;
3. Запустить приложение через java -jar aqa-shop.jar; 
4. Запустить тесты командой ./gradlew clean test; 
5. Просмотреть отчет в Allure через команду ./gradlew allureServe.
6. Посмотреть отчет Gradle через путь ./build/reports/tests/test/index.html.