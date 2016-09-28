
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.

## 1. Create a wallet instance
## 1.1 Offline Wallet
This will connect to a locally database. It can be used without internet.
```java
Wallet localWallet = new OfflineWallet();
```
## 1.1 Online Wallet
This will connect to a REST Web Server. It can be used only with internet.
```java
Wallet webWallet = new OnlineWallet();
```

## 2. Configure storage
### 2.1 Offline Wallet configure to local db
```java
public boolean configure(LocalConfig config)
```
> Example:
```java
Wallet wallet = new WalletPoket();
// Configure local Database
LocalConfig localConfig = new LocalConfig();
localConfig.DRIVER = "...";
// ...
wallet.configure(localConfig);
```
    
You need create db schema manually by execute this statements in your database.
```sql
CREATE TABLE X (
   @todo: complete this
);
```
You can also can change db schema - names of table and name of fields.
If you have a custom database.
Note: This is useful if you want to use more services that share one or more tables.
```java
public boolean configure(SchemaConfig config)
```
> Example:
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
    
### 2.2 Online Wallet configure to web server
```java
public boolean configure(ServerConfig config)
```
> Example:
```java
Wallet wallet = new WalletPoket();
// Configure REST Server
ServerConfig serverConfig = new ServerConfig();
serverConfig.DRIVER = "...";
// ...
wallet.configure(serverConfig);
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
public int **addCard**(String label);
```
> Example:
```java
int privateCardId = wallet.addCard("Economy 1");
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

