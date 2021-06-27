[![Actions Status](https://github.com/AkaZver/tinkoff-report-to-taxes/workflows/Build/badge.svg)](https://github.com/AkaZver/tinkoff-report-to-taxes/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AkaZver_tinkoff-report-to-taxes&metric=alert_status)](https://sonarcloud.io/dashboard?id=AkaZver_tinkoff-report-to-taxes)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AkaZver_tinkoff-report-to-taxes&metric=security_rating)](https://sonarcloud.io/dashboard?id=AkaZver_tinkoff-report-to-taxes)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=AkaZver_tinkoff-report-to-taxes&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=AkaZver_tinkoff-report-to-taxes)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AkaZver_tinkoff-report-to-taxes&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=AkaZver_tinkoff-report-to-taxes)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=AkaZver_tinkoff-report-to-taxes&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=AkaZver_tinkoff-report-to-taxes)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AkaZver_tinkoff-report-to-taxes&metric=coverage)](https://sonarcloud.io/dashboard?id=AkaZver_tinkoff-report-to-taxes)

# Описание
Проект выполняет преобразование PDF-отчёта о выплатах дивидендов иностранными компаниями 
[брокера Тинькофф](https://www.tinkoff.ru/invest/) в форматы XLSX или CSV для более 
удобного заполнения налоговой декларации в [личном кабинете ФНС](https://lkfl2.nalog.ru/lkfl/)

# Запуск
Для запуска понадобится Java 8+, Gradle Wrapper уже лежит в проекте

Если хотите собрать проект в JAR-файл, то воспользуйтесь `gradlew assemble`

Первый аргумент для запуска — путь к PDF-файлу, второй — формат для преобразования

Варианты запуска:
- `run.bat`
- `gradlew run -PinputPath=X:\Путь\к\Файлу.pdf -PexportType=csv`
- `java -jar tinkoff-report-to-taxes.jar X:\Путь\к\Файлу.pdf xlsx`
