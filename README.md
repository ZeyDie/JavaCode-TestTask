# JavaCode-TestTask

Поддержка запросов
<br>POST `/api/v1/wallet/create/{walletId}` - создание кошелька по UUID с 0 балансом
<br>POST `/api/v1/wallet`- пополнение/снятие средств с кошелька с JSON запросом модели WalletRequestDto
<br>GET `/api/v1/wallets/{walletId}` - получение баланса кошелька по UUID

JDK 17
<br>Spring 3.4.2

Запуск Docker
<br>`docker-compose up --build`