# Mybank

Mybank is a Simple REST based demo project for transferring money between accounts.

## Getting Started

Inorder to lunch this project, run main method in Application class. This method besides starting server locally on port `4657`, populates initial demo data set.
Please look at `demo.sql` file to know this demo data set. It includes 4 customer each have an account. Some of the customers already have some amount of balance. 

This project currently focuses more on transferring money logic and it is not fully functional for every needed endpoint for handling domain model. 

Here is the list of some working endpoints: 

#### Getting balance for an accountId:

(GET) http://localhost:4567/mybank/transfer-management/balance/:accountId  
_(existing accountIds 1001..1004)_

#### Getting all customers information : 
(GET) http://localhost:4567/mybank/customer-management/customers  

#### Transferring money from one account to another

(POST) http://localhost:4567/mybank/transfer-management/transfer
The format of the request body id like this : 
`{
    "sourceAccountId" : 1001,
    "destinationAccountId" : 1002,
    "amount" : 10,
    "currency": "GBP",
    "etag": 82734923487
}`

Please note that inorder to provide correct information for this request body, you should always first get correct balance information for the source account : 
http://localhost:4567/mybank/transfer-management/balance/1001


## Assumptions

* It is assumed that the sender and receiver are already our bank customers.**

* If sender wants to send some amount in some specific currency, he should have enough balance in that currency. It means trying to transfer 1 GBP while sender has 1000 Euro Balance (and not any money in GBP) will fail.**

* Currency exchanging is not happening implicitly. For creating balance in some currency you should use exchange endpoint ( will be implemented soon :) ).**

* Receiver will receive money in a currency only if he has some balance in that currency. It means receiver also can put constraint on transaction and avoid receiving money in currencies he doesn't like.**

## Built With

* [Spark](http://sparkjava.com/documentation) - A micro framework for creating web applications
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Hojat Vaheb** 
