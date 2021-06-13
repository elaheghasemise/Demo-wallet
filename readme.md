#Project:
Demo-wallet

#Description:
Demo-wallet is a small micro-service wallet to keep balance per player.The balance can be modified by debit/credit transactions.
Usually wallets supports multi currency and currency exchange per account but to limit the scope oh this service, currently, it just supports SEK as wallet currency, and any other currencies causes transactions failed.
Basically, Java 11, Spring boot, embedded Mongo-DB are used to develop this service.

Following Rest APIs provided in this service:
-----------------------------------------------------------------------------------
First you need to have an account in service, so you can create account by calling :
Post: "localhost:8080/wallet/create"
-----------------------------------------------------------------------------------
To get all wallet information, including balance, any time you want you can use:
Get: "localhost:8080/wallet/{accountNumber}"
-----------------------------------------------------------------------------------
To get balance of the existing account number separately, use:
Get: "localhost:8080/wallet/balance/{accountNumber}"
-----------------------------------------------------------------------------------
To list all accounts and wallets in the service:
Get: "localhost:8080/wallets"
-----------------------------------------------------------------------------------
Remove API  provided to remove all wallets in the service for test purposes:
Delete: "localhost:8080/wallet/delete/all"
-----------------------------------------------------------------------------------
To credit money in you account use:
Post: "localhost:8080/debit/transaction"
-----------------------------------------------------------------------------------
To debit money in you account use:
Post: "localhost:8080/credit/transaction"
-----------------------------------------------------------------------------------
To list transaction history per account use:
Post: "localhost:8080/list/transaction/{accountNumber}"


#How to install and run the project:
If you have java 11 install on your pc, simply you can build and run this service by following commands:

mvn clean install -DskipTests
mvn spring-boot:run

#How to Use the Project
You can find API calling methods in WalletClient.java
This class has written for test purpose.

#Tests
Simply you can run all unit tests in "\src\test\java\com\leovegas\wallet\service",
Unit tests load spring context internally

To run test in the directory "\src\test\java\com\leovegas\wallet\handler", you need to have 
the Demo-wall up and runs









