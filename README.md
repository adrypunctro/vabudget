
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.

## Usage

### Create a wallet instance
```java
Wallet wallet = new WalletPoket();
wallet.connectWith(
    DAOFactory.getDAOFactory(DAOFactory.MYSQL)
);
```

### Wallet interface

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

