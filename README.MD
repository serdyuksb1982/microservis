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