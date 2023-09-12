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
## Перевод java-code на Kotlin