# Getting Started

## 1. Build & run tests

In the commandline, execute the following command to build and run all automated tests

```
./mvnw clean package
```

## 2. Run the application

After successfully build the service, run

```
./mvnw spring-boot:run
```

Upon successful launch, the following output should be seen

```
... [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
... [           main] o.e.b.TransactionViewDemoApplication     : Started TransactionViewDemoApplication in 3.531 seconds (process running for 3.803)
```

By default, an embedded Tomcat is launched via HTTP port 8080. The health of the service could be verified by going to
this URL:

```
http://localhost:8080/actuator/health

Expected output:
----------------
{
  "status": "UP"
}
```

For testing & demonstrative purposes, test data (stored in the embedded H2 in-memory relational database) is generated.

# API Reference

## 1. Get all bank accounts of the currently logged-in user

```
HTTP GET /bank/accounts
```

Where

- `accountName` = Name of the bank account
- `accountNumber` = the unique account number of the bank account
- `accountType` = _credit_, _transaction_ or _saving_ account
- `currencyCode` = 3-digit currency code, e.g. AUD or USD
- `availableBalance` = the current balance of the account at the `balanceLocalDate`
- `balanceLocalDate` = the date of the `availableBalance` in the "local timezone" of the current user

Sample output:

```
[
  {
    "accountName": "michael.truong",
    "accountNumber": "M7945173",
    "accountType": "CREDIT",
    "currencyCode": "AUD",
    "availableBalance": 91509.14,
    "balanceLocalDate": "2024-09-08"
  },
  ...
]
```

## 2. Get bank transactions by account

To fetch the most recent 10 transactions, invoke:

```
HTTP GET /bank/transactions/{accountNumber}
```

There are 2 optional request parameters which can be used to specify the page number `pageNumber` (starting from 0)
and the page size `pageSize` (by default 10).

For example, to fetch 15 transactions from the 3rd page, invoke:

```
HTTP GET /bank/transactions/{accountNumber}?pageNumber=2&pageSize=15
```

Sample output:

```
[
  {
    "accountNumber": "M7945173",
    "accountName": "michael.truong",
    "transactionDate": "2024-08-26",
    "currencyCode": "AUD",
    "debitAmount": -285.17,
    "creditAmount": null,
    "transactionType": "DEBIT",
    "transactionNarrative": "Withdrawn -285.1669023786916 (AUD)from account M7945173"
  },
  ...
]
```
