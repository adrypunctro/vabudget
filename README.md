
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.

## 0. Setup database
You need create db schema manually by execute this statements in your database.
```sql
CREATE TABLE wal_me (
   key VARCHAR(100) PRIMARY KEY,
   value VARCHAR(250)
);
INSERT INTO wal_me VALUES ('accountId','1'), ('isReadId','0');

CREATE TABLE wal_account (
   accountId INT,
   name VARCHAR(250)
);

CREATE TABLE wal_cards (
   cardId,
   ownerId,
   label,
   amount
);

CREATE TABLE wal_transactions (
   transId,
   cardId,
   personId,
   description,
   amount,
   date
);
```

## 1. Create a configure object
### 1.1 Offline Wallet configure to local db
```java
// Configure local Database
LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "com.mysql.jdbc.Driver";
localConfig.DBURL  = "jdbc:mysql://localhost/database_name";
localConfig.USER   = "username";
localConfig.PASS   = "password";
```
    
You can also can change db schema - names of table and name of fields.
If you have a custom database.
Note: This is useful if you want to use more services that share one or more tables.
```java
SchemaConfig mySchema = new SchemaConfig();
schemaConfig.PROFILE_TABLE_PRE = "wal_";
schemaConfig.PROFILE_TABLE = "profile";
// ...
```
    
### 1.2 Online Wallet configure to web server
```java
// Configure REST Server
ServerConfig serverConfig = new ServerConfig();
serverConfig.DRIVER = "...";
// ...
```
   
## 2. Create a wallet instance
## 2.1 Offline Wallet
This will connect to a locally database. It can be used without internet.
```java
// Configure local Database
LocalConfig myConfig = new LocalConfig();
// ... See 1.1 Offline Wallet configure to local db
SchemaConfig mySchema = new SchemaConfig();
// ... See 1.1 Offline Wallet configure to local db

// Init with default schema
Wallet wallet1 = new OfflineWallet(myConfig);
// Initi with mt schema
Wallet wallet2 = new OfflineWallet(myConfig, mySchema);
```
## 2.1 Online Wallet
This will connect to a REST Web Server. It can be used only with internet.
```java
// Configure REST Server
ServerConfig serverConfig = new ServerConfig();
// ... See 1.2 Online Wallet configure to web server

Wallet webWallet = new OnlineWallet(serverConfig);
```
    
## 3. Connect to storage
### 3.1 Offline Wallet connect to database.
```java
public boolean connect()
```
This will return true if connect successful. False oherwise.
```java
public boolean connected()
```
This will return true if you are connected to database. False oherwise.
> Example:
```java
Wallet wallet = new WalletPoket();
LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "...";
wallet.configure(localConfig);
if (wallet.checkShema())
{
   wallet.createSchema();
}
wallet.connect();
```
    
### 3.2 Online Wallet connect to server.
This will return true if server is online. False otherwise.
```java
public boolean connected()
```
Note: The Online Wallet don't have connect() method because the web server it supposed full time connected with database.

## 4. Syncronization between Offline Wallet and Online Wallet
```java
public static boolean sync(Wallet primaryWallet, Wallet secondaryWallet)
```
This will be check if connected() of both wallet is true. If not both are true, sync() return false.
Otherwise, it will send through and through unsynchronized data.
Note: (How work) Each table from database have a field named *sync* ENUM(0,1) and a field named *deleted* ENUM(0,1).
It will send all data with sync = 0. After execute the appropriate statement, sync become 1.
If we have a row with sync=1 and deleted=1, the row will be removed permanently.

## 5. Usage
Both, offline and online, have same interface.
### 5.1 Cards
```java
public int addCard(String label);
```
> Example:
```java
int privateCardId = wallet.addCard("Economy 1");
```
    
```java
public int addCard(String label, BigDecimal initAmount);
```
> Example:
```java
int privateCardId = wallet.addCard("Economy 1", new BigDecimal(200));
```
    
```java
public boolean removeCard(int cardId);
```
> Example:
```java
boolean wasRemoved = wallet.removeCard(5);
```
    
```java
public Card getCard(int cardId);
```
It return null if the card doesn't exists.
> Example:
```java
Card thisCard = wallet.getCard(2);
```
    
```java
public List<Card> getCards();
```
It return an empty list if no card was found.
> Example:
```java
List<Card> cards = wallet.getCards();
```
    
### 5.2 Transactions

public boolean **income**(int cardId, int userId, BigDecimal amount, String description, Date datetime);
```java
```
public boolean **incomeDistrib**(int distribId, int userId, BigDecimal amount, String description, Date datetime);
```java
```
public boolean **expense**(int cardId, int userId, BigDecimal amount, String description, Date datetime);
```java
```
public List\<Transaction\> **history**(int cardId);
```java
```

### 5.3 Share

public boolean **shareWith**(int cardId, int personId);
```java
```
public boolean **removeShare**(int cardId, int personId);
```java
```
public List\<Share\> **getShared**(int cardId);
```java
```
public boolean **sharedAccept**(int cardId, int personId);
```java
```
public boolean **sharedReject**(int cardId, int personId);
```java
```

### 5.4 Distribution

public int **addDistribution**(int ownerId, String label, Map<Integer, Integer> ratio);
```java
```
public boolean **removeDistribution**(int distribId);
```java
```
public Distribution **getDistribution**(int distribId);
```java
```
public List\<Distribution\> **getDistributions**(int personId);
```java
```

### 5.5 Distribution Share

public boolean **shareDistributionWith**(int distribId, int personId);
```java
```
public boolean **removeShareDistribution**(int distribId, int personId);
```java
```
public boolean **sharedDistributionAccept**(int distribId, int personId);
```java
```
public boolean **sharedDistributionReject**(int distribId, int personId);
```java
```

## Full example
```java
// Configure local Database
LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "com.mysql.jdbc.Driver";
localConfig.DBURL  = "jdbc:mysql://localhost/database_name";
localConfig.USER   = "username";
localConfig.PASS   = "password";

// Init local wallet with default schema
Wallet localWallet = new OfflineWallet(localConfig);

// Configure web server
WebConfig webConfig = new WebConfig();

// Init local wallet with default schema
Wallet webWallet = new OnlineWallet(webConfig);

// Create to local two cards
int privateCardId = localWallet.addCard("My private card");
int sharedCardId = localWallet.addCard("Shared card", new BigDecimal(100));

Card privateCard = localWallet.getCard(privateCardId);
Card sharedCard = localWallet.getCard(sharedCardId);

sharedCard.shareWith(2);

privateCard.income(new BigDecimal(150));
privateCard.income(new BigDecimal("1220.5"), "Big income.");
privateCard.income(new BigDecimal(88), "From past", new Date());
privateCard.expense(new BigDecimal(150));
sharedCard.expense(new BigDecimal("33.3"), "Small expense.");
sharedCard.expense(new BigDecimal(20), "Lowest expense.", new Date());

// Synchronize both wallets
Wallet.sync(localWallet, webWallet);
```

## UML Scheme
![alt text][uml-scheme]

[uml-scheme]: https://raw.githubusercontent.com/adrypunctro/vabudget/master/vabudget-uml-scheme.png
