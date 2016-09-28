
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.

## 1. Create a wallet instance
```java
Wallet wallet = new WalletPoket();
```

## 2. Connect to storage
### 2.1 Configure local db/server
```java
public boolean configure(LocalConfig config)
```
```java
Wallet wallet = new WalletPoket();
// Configure local Database
LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "...";
// ...

wallet.configure(localConfig);
```
```java
public boolean configure(ServerConfig config)
```
```java
Wallet wallet = new WalletPoket();
// Configure REST Server
ServerConfig serverConfig = new ServerConfig();
serverConfig.DRIVER = "...";
// ...

wallet.configure(serverConfig);
```
### 2.2 Create database schema for local storage
You need create schema manually by execute this statements in your database.
```sql
CREATE TABLE X (
   @todo: complete this
);
```
You can also can change schema before - names of table and name of fields.
```java
public boolean configure(SchemaConfig config)
```
```java
Wallet wallet = new WalletPoket();

LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "...";
// ...
wallet.configure(localConfig);

SchemaConfig schemaConfig = new SchemaConfig();
schemaConfig.PROFILE_TABLE_PRE = "wal_";
schemaConfig.PROFILE_TABLE = "profile";
// ...
wallet.configure(schemaConfig);

```
Note: This is useful if you want to use more services that share one or more tables.

### 2.4 Connect to database. Only for local
```java
public boolean connect()
```
This will return true if connect successful. False oherwise.
```java
public boolean connected()
```
This will return true if you are connected to database. False oherwise.
```java
Wallet wallet = new WalletPoket();
LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "...";
try
{
   wallet.configure(localConfig);
}
catch(MissingFields e)
{
    System.out.println("Configure error: " + e.getMessage());
}
if (wallet.checkShema())
{
   wallet.createSchema();
}
wallet.connect();
```
### 2.5 Connect to server. Only for server
```java
public boolean connectivity()
```
This will return true if server is online. False otherwise.

## 3. Syncronization local with server
```java
public boolean sync()
```
This will be check if connected() and connectivity() is true. If not both are true, sync() return false.
Otherwise, it will send all unsynchronize data from local to server and vice versa.

## 4. Usage
### 4.1 Cards
public int **addCard**(int ownerId, String label);
```java
int ownerId = 1;// My Account ID
int privateCardId = wallet.addCard(ownerId, "Economy 1");
```
public int **addCard**(int ownerId, String label, BigDecimal initAmount);
```java
```
public boolean **removeCard**(int cardId);
```java
```
public IAccount **getCard**(int cardId);
```java
```
public List\<Card\> **getCards**();
```java
```

### 4.2 Transactions

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

### 4.3 Share

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

### 4.4 Distribution

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

### 4.5 Distribution Share

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

