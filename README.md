
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.


## 1. Create a wallet instance
```java
Wallet wallet = new WalletPoket();
```

## 2. Configure with storage solution
### 2.1 Connect to local database
public boolean **configure**(LocalConfig config) throws **MissingFields**
```java
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
```
### 2.2 Connect to web server
public boolean **configure**(ServerConfig config) throws **MissingFields**
```java
ServerConfig serverConfig = new ServerConfig();
serverConfig.DRIVER = "...";
try
{
   wallet.configure(serverConfig);
}
catch(MissingFields e)
{
    System.out.println("Configure error: " + e.getMessage());
}
```

---
## Wallet interface

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

---

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

---

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

---

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

---

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

