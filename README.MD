# Микросервисная архитектура на базе Spring Cloud.
## Используемые технологии:
* Java-17 (Kotlin);
* Gradle;
* Spring Boot, Spring Cloud, Spring Web, Spring Data JPA...;
* Postgresql;
* RabbitMQ, Kafka.
### Семь микросервисов:
* Planner-Server - EUREKA-SERVER, порт 8765;
* Planner-Config - обеспечение вынесения файлов настроек (например *.properties) из приложения в облако;
* Planner-Gateway - API Gateway (AG) является шлюзом, который обрабатывает входящие запросы и перенаправляет их на нужный микросервис;
* Planner-Entity - Не самостоятельный сервис. Содержит entity классы для клиентов сервиса;
* Planner-Utils - Не самостоятельный сервис. Утилитные объекты, AOP, resttemplate, webClient;
* Planner-Todo - Микросервис-клиент. Содержит основную логику: Category, Priority, Stat, Task,
слой репозиториев и сервисов. Поиск по Category, Priority и Task. Имеет свою базу данных с таблицами. В AMQP выступает как Consumer.
* Planner-User - Микросервис-клиент. Содержит логику работы с Users. Имеет свою независимую БД. В AMQP выступает как Supplier.


## AMQP (Advanced Message Queuing Protocol):
 ### message - сообщения, любая информация в любом случае;
* producer - (publisher) - издатель (источник), отправитель сообщений;
* consumer - (subscriber) - получатель сообщений (адресат, подписчик)
* exchange (обменник - буфер, куда попадают все сообщения и оттуда уже распределяются в очереди);
* queue - очередь, которая связана с получателем. Если в очередь поступает сообщение - получатель его обрабатывает;
* binding - связывание и настройка обменника с очередью.
--------------------
install Rabbitmq from Windows:
 in powerShell:
* Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
* choco install rabbitmq
* проверить -> http://localhost:15672/ login=guest password=guest
--------------------
## Перевод java-code на Kotlin (в Intellij Idea сочетание клавиш: Alt+Ctrl+Shift+K)
перевод Java Spring Boot на Kotlin начинается с добавления зависимостей
// kotlin plugins
* id 'org.jetbrains.kotlin.jvm' version '1.7.10'
* id "org.jetbrains.kotlin.plugin.spring" version "1.7.10"
* id "org.jetbrains.kotlin.plugin.jpa" version "1.7.10"
* id "org.jetbrains.kotlin.plugin.allopen" version "1.7.10"
<br>
// kotlin in spring application integration зависимости
* implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
* implementation ("org.jetbrains.kotlin:kotlin-reflect")
* implementation ("com.fasterxml.jackson.module:jackson-module-kotlin")
-------------------
# Добавление Kafka:
## Требования к системе: наличие Java-8+.
## Apache Kafka может работать как с ZooKeeper или
### 1) Шаг 1 - скачивание и установка нужного ПО.
 * https://kafka.apache.org/documentation/#quickstart
 * Scala 2.13  - kafka_2.13-3.5.1.tgz (asc, sha512) 
    <br>Добавить переменные окружения для Windows (если Windows)
## 2) Шаг 2 - запуск Zookeeper и Kafka server:
* cmd: kafka-server-start: ->
  USAGE: kafka-server-start server.properties (успешный ответ);
  * создать папку data в -> C:\tmp\kafka_2.13-3.5.0;
  * создать папки zoo и kafka -> C:\tmp\kafka_2.13-3.5.0\data (в этих папках будет храниться служебная информация работы kafka server).
  * зайти в папку C:\tmp\kafka_2.13-3.5.0\config, открыть папки:
    * /server, /zookeeper:
      * установить -> в zookeeper dataDir=C:/tmp/kafka_2.13-3.5.0/data/zoo (обратить внимание на "слеш");
      * установить -> в server log.dirs=C:/tmp/kafka_2.13-3.5.0/data/kafka
## Запуск:
* Сначала запуск Zookeeper (Это инструмент-координатор, действует как общая служба конфигурации в системе.):
 * zookeeper-server-start.bat C:\tmp\kafka_2.13-3.5.0\config\zookeeper.properties
* Запускаем сам Kafka server (в отдельном окне CMD):
 * kafka-server-start.bat C:\tmp\kafka_2.13-3.5.0\config\server.properties
<br> При успешном запуске Zookeeper и Kafka-server в папке ../data будут созданы системные файлы.
<br> Для изменения  логгирования зайти в папку C:\tmp\kafka_2.13-3.5.0\config\log4j.propertiesn и внести нужные изменения.
## 3) Шаг 3 Создание TOPIC (в CMD)
* kafka-topics --create --topic serdyuksb-test --bootstrap-server localhost:9092 
    * результат: Created topic serdyuksb-test
        * посмотреть существующие topics: kafka-topics --list --bootstrap-server localhost:9092
## 4) Шаг 4 Отправка сообщений в созданный TOPIC
* Подключение к топику для : kafka-console-producer --topic serdyuksb-test --bootstrap-server localhost:9092
    * после срабатывания данного скрипта можно отправлять сообщения через консоль.
## 5) Шаг 5 Считывание сообщений
* kafka-console-consumer --topic serdyuksb-test --from-beginning --bootstrap-server localhost:9092
-----------------------------------------------------